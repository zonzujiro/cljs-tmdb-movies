(ns tmdb-movies.components.movie-page
  (:require [rum.core :as rum]
            [tmdb-movies.utils.api :refer [get-movie]]))

(def init-state 
  {:movie nil
   :error? false})

; (-> (get-movie)
;     (.then #(js/console.log %)))

(rum/defc MoviePage < rum/reactive 
  [movie]
  ; (js/console.log (clj->js movie))
  [:div
    [:p "This is movie page"]
    [:a {:href "#/"}
      "Go back"]])
