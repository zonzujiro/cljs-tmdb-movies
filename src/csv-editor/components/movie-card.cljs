(ns tmdb-movies.components.movie-card
  (:require [rum.core :as rum]))
  
(rum/defc MovieCard [movie]
  [:div.movie-card
    [:h4 (.-title movie)]
    [:a {:href (str "#/movie/" (.-id movie))}
     "Open movie"]])
