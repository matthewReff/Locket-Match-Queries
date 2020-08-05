(ns app.components.data-owner
  (:require
   [helix.core :refer [<> $]]
   [helix.hooks :as hook :include-macros true]
   [app.helix :refer [defnc]]
   [app.components.data-view :refer [DataView]]
   [cljs-bean.core :refer [->js]]
   ["emotion" :refer [css]]
   ["@blueprintjs/core" :refer [TagInput Button]]))

(defmulti reducer (fn [_ action] (:type action)))
(defmethod reducer ::search-field-change
  [state {new-members :payload}]
  (update state :search-field assoc :value new-members :submitted false))
(defmethod reducer ::search
  [{{members :value} :search-field :as state} _]
  (-> state
      (assoc :displayed (set members))
      (assoc-in [:search-field :submitted] true)))
(defmethod reducer ::reset
  [{{:keys [submitted]} :search-field :keys [displayed] :as state} _]
  (update state
          :search-field assoc
          :submitted (not submitted)
          :value (if submitted #js [] (->js displayed))))

(defnc
  DataOwner
  []
  (let [[{:keys [search-field displayed]} dispatch!]
        (hook/use-reducer reducer
                          {:search-field {:value #js []} :displayed #{}})

        any-filled? (or (seq displayed) (seq (:value search-field)))]
    (<> ($ :div
           {:class (css #js {:display "flex"})}
           ($ ^:native TagInput
              {:on-change #(dispatch! {:type ::search-field-change :payload %})
               :intent (if (:submitted search-field) "success" "primary")
               :values (:value search-field)
               :fill true
               :add-on-blur true
               :input-props #js {:type "number"}
               :left-icon "inherited-group"
               :right-element (when any-filled?
                                ($ ^:native Button
                                   {:icon (if (or (:submitted search-field)
                                                  (empty? displayed))
                                            "cross"
                                            "history")
                                    :on-click #(dispatch! {:type ::reset})}))})
           ($ ^:native Button
              {:on-click #(dispatch! {:type ::search})
               :disabled (or (not any-filled?) (:submitted search-field))}
              "Fetch"))
        ($ DataView {:members displayed}))))