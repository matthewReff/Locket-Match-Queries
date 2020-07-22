(ns locket-match-queries.api.spec.match
  (:require
   [clojure.spec.alpha :as s]
   [locket-match-queries.api.spec.player :as player]
   [locket-match-queries.api.spec.hero :as hero]))

(s/def ::match_id int?)
(s/def ::radiant_win boolean?)
(s/def ::duration int?)
(s/def ::tower_status_dire int?)
(s/def ::tower_status_radiant int?)
(s/def ::barracks_status_dire int?)
(s/def ::barracks_status_radiant int?)
(s/def ::first_blood_time int?)
(s/def ::radiant_score int?)
(s/def ::dire_score int?)

(s/def ::is_pick boolean?)
(s/def ::order int?)
(s/def ::team #{0 1})
(s/def ::pick_ban (s/keys :req-un [::hero/hero_id ::is_pick ::order ::team]))
(s/def ::picks_bans (s/coll-of ::pick_ban))

(s/def ::match
  (s/keys :req-un [::match_id
                   ::radiant_win
                   ::duration
                   ::tower_status_dire
                   ::tower_status_radiant
                   ::barracks_status_dire
                   ::barracks_status_radiant
                   ::first_blood_time
                   ::radiant_score
                   ::dire_score
                   ::player/players
                   ::picks_bans]))
