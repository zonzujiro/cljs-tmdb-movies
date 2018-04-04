(ns tmdb-movies.utils.either)

;; Either type
(defprotocol Either
  (left? [e])
  (right? [e])
  (left-value [e])
  (right-value [e]))

(defrecord Right [x]
  Either
  (left? [e] false)
  (right? [e] true)
  (left-value [e] nil)
  (right-value [e] x))

(defrecord Left [x]
  Either
  (left? [e] true)
  (right? [e] false)
  (left-value [e] x)
  (right-value [e] nil))

(defn left [x] (Left. x))
(defn right [x] (Right. x))
