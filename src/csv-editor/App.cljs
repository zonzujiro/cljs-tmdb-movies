(ns tmdb-movies.app
  (:require [rum.core :as rum]
            [tmdb-movies.utils.either :refer [right? right-value left-value]]
            [tmdb-movies.utils.api :refer [get-popular-movies]]
            [tmdb-movies.components.movie-card :refer [MovieCard]]))
  
(def init-state 
  {:popular []
   :error? false})

(def app-state (atom init-state))

(defn handle-fetch [e]
  (if (right? e)
    (swap! app-state assoc :popular (.-results (right-value e)))
    (swap! app-state assoc :error? true)))

(-> (get-popular-movies)
    (.then #(handle-fetch %)))

(rum/defc App < rum/reactive []
  (let [popular (:popular (rum/react app-state))]
    ; (js/console.log (clj->js popular))
    [:div.root-container
      [:h1 "Movies"]
      [:div
        (map #(rum/with-key (MovieCard %) (.-id %)) popular)]
      [:div.content]]))
