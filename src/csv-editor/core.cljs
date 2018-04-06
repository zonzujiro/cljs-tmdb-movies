(ns tmdb-movies.core
  (:require [rum.core :as rum]
            [tmdb-movies.app :refer [App]]
            [tmdb-movies.components.movie-page :refer [MoviePage]]
            [tmdb-movies.router :refer [Router]]))

(def routes 
  [{:component App
    :href "/"}
   {:component MoviePage
    :href "/movie/:id"}])

(rum/mount (Router routes)
           (. js/document (getElementById "app")))
