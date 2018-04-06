(ns tmdb-movies.router
  (:require [rum.core :as rum] 
            [tmdb-movies.app :refer [App]]
            [tmdb-movies.components.movie-page :refer [MoviePage]]))

(def init-state 
 {:current-route "/"
  :current-component App})

(def routes 
  [{:component App
    :href "/"}
    
   {:component MoviePage
    :href "/movie/:id"}])
    
(def router-state (atom init-state))

(defn url-to-regexp [url]
  (re-pattern (clojure.string/replace url #":\w+" "(.*)")))

(defn equal-path? [href url]
  (re-matches (url-to-regexp href) url))

; TODO consider adding macros for vector find
(defn find-route [routes next-href]
  (first (filter #(not-empty (equal-path? (:href %) next-href)) routes)))

(defn on-hash-change []
  (let [next-href (subs (aget js/window "location" "hash") 1)
        next-route (find-route routes next-href)]
    (js/console.log (clj->js (find-route routes next-href)))
    (when-not (nil? next-route) (swap! router-state assoc :current-component (:component next-route)))))

; (js/console.log (clj->js (equal-path? "/movie/:id/actor/:actorId" "/movie/16/actor/48")))

(.addEventListener js/window "hashchange" on-hash-change)

(rum/defcs Router < rum/reactive []
  ; (js/console.log (clj->js @router-state))
  (let [Component (:current-component (rum/react router-state))]
    [:div
      (Component)]))
    
  
