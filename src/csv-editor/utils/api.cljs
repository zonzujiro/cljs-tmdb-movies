(ns tmdb-movies.utils.api
  (:require [tmdb-movies.utils.either :refer [right left right?]]
            [clojure.string :as string]))

(def api-key "e2d8067a58cbd9179bb4ab08c43b41c7")
(def base-url "https://api.themoviedb.org/3")

(defn stringify-query [query]
  (str "?" (string/join "&" (map #(string/join "=" %) query))))

(defn fetch [url query]
  (-> (js/fetch (str base-url url (stringify-query query)))
      (.then #(.json %))
      (.then #(right %))
      (.catch #(left %))))

(defn get-params [city]
  {"q" city 
   "APPID" api-key})

(defn get-popular-movies []
  (fetch "/movie/popular" {"api_key" api-key}))

(defn get-today-forecast [city]
  (-> (js/Promise.all [(fetch "/weather" (get-params city)) (fetch "/forecast" (get-params city))])
      (.then #(into [] %))
      (.then #(if (every? right? %)
                (right %)
                (left %)))))

           