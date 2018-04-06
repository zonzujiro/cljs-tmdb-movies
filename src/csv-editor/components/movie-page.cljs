(ns tmdb-movies.components.movie-page
  (:require [rum.core :as rum]))

(rum/defc MoviePage [movie]
  (js/console.log (clj->js movie))
  [:div])
