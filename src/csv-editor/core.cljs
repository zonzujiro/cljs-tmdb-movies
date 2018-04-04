(ns tmdb-movies.core
  (:require [rum.core :as rum]
            [tmdb-movies.router :refer [Router]]))

(rum/mount (Router)
           (. js/document (getElementById "app")))
