;; from https://github.com/juxt/snap/blob/master/src/snap/core.cljc
;; with minor modifications
(ns snap.core
  (:require
   [clojure.java.io :as io]
   [clojure.edn :as edn]
   [clojure.test :as t]
   [slingshot.slingshot :refer [throw+]]
   [zprint.core :as zp]))

(defn default-make-path
  [ns-kw]
  (str "test/__snapshots__/" (namespace ns-kw) "." (name ns-kw) ".edn"))

(defn match-snapshot
  "Accepts a unique namespaced keyword and a value.
   Creates a file (using the keyword for its name) if not already present, and writes the value to it.
   If a file with that name is already present it compares its content to the value using a test/is macro."
  ([k v] (match-snapshot {} k v))
  ([{:keys [make-path zprint? redo? fail-fast?]
     :or {make-path default-make-path
          zprint? true
          redo? false
          fail-fast? (System/getenv "CI")}}
    k
    v]
   (let [file-name (make-path k)
         file      (io/file file-name)]
     (cond
       (and (not redo?) (.exists file))
         (let [snapshot (-> file
                            io/reader
                            java.io.PushbackReader.
                            edn/read)]
           (t/is (= snapshot v) (str "Using snapshot at " file-name)))
       fail-fast?
         (throw+
           {:type ::missing-snapshot
            :message
              "No snapshot found and either `fail-fast?` or `CI` env var set."
            :file file-name})
       :else (do (io/make-parents file-name)
                 (with-open [ofile (io/writer file-name)]
                   (binding [*out* ofile] (if zprint? (zp/zprint v) (prn v))))
                 (t/is true "to avoid no assertions error"))))))
