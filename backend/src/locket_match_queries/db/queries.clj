(ns locket-match-queries.db.queries
  (:require
   [next.jdbc :as jdbc]
   [next.jdbc.specs]
   [clojure.spec.alpha :as s]
   [locket-match-queries.api.spec.hero :as hero]
   [locket-match-queries.db.system :refer [sql-format]]
   [slingshot.slingshot :refer [throw+]]
   [honeysql.helpers :as h]
   [locket-match-queries.api.spec.match :as match]
   [locket-match-queries.api.spec.player :as player]
   [locket-match-queries.api.spec.item :as item]
   [locket-match-queries.api.spec.util :as util]))

(defn get-heroes
  [db]
  (jdbc/execute! (db)
                 (-> (h/select [:hero-id :id] :name)
                     (h/from :hero)
                     sql-format)
                 jdbc/unqualified-snake-kebab-opts))
(s/fdef get-heroes
  :args (s/cat :db :next.jdbc.specs/connectable)
  :ret (s/coll-of ::hero/hero))

(s/def :num/non-neg?
  (s/and number?
         (s/or :pos pos?
               :zero zero?)))

(defn get-matches-with
  [db members]
  (throw+ {:type :not-implemented :args members}))

(s/def ::match (s/keys :req-un [::match/id ::match/duration]))
(s/fdef get-matches-with
  :args (s/cat :db :next.jdbc.specs/connectable
               :members (s/coll-of ::player/id))
  :ret (s/coll-of ::match))

(defn get-frequent-heroes-of
  [db members]
  (throw+ {:type :not-implemented :args members}))

(s/fdef get-frequent-heroes-of
  :args (s/cat :db :next.jdbc.specs/connectable
               :members (s/coll-of ::player/id))
  :ret (s/coll-of (s/keys :req-un [::hero/hero])))


(defn get-player-successes-with
  [db members hero-id]
  (throw+ {:type :not-implemented :args [members hero-id]}))

(s/def :player/display string?)
(s/def ::player (s/keys :req-un [::player/id] :opt-un [:player/display]))
(s/def :success/total ::util/uint16)
(s/def :success/winrate ::util/percent)
(s/def :success/wins ::util/uint16)
(s/def ::success
  (s/keys :req-un [::player :success/total :success/winrate :success/wins]))

(s/fdef get-player-successes-with
  :args (s/cat :db :next.jdbc.specs/connectable
               :members (s/coll-of ::player/id)
               :hero-id ::hero/id)
  :ret (s/coll-of ::success))

(defn get-items-for
  [db player-id match-id]
  (throw+ {:type :not-implemented :args [player-id match-id]}))

(s/fdef get-items-for
  :args (s/cat :db :next.jdbc.specs/connectable
               :player-id ::player/id
               :match-id ::match/id)
  :ret (s/coll-of ::item/item))

(defn get-player-info-of-from
  [db members match-id]
  (throw+ {:type :not-implemented :args [members match-id]}))

(s/fdef get-player-info-of-from
  :args (s/cat :db :next.jdbc.specs/connectable
               :members (s/coll-of ::player/id)
               :match-id ::match/id)
  :ret (s/coll-of (s/keys :req-un [::hero/hero ::player])))

(defn get-player-display
  [db player-id]
  (throw+ {:type :not-implemented :arg player-id}))

(s/fdef get-player-display
  :args (s/cat :db :next.jdbc.specs/connectable
               :player-id ::player/id)
  :ret :player/display)
