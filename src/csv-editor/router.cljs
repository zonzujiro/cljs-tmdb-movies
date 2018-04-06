(ns tmdb-movies.router
  (:require [rum.core :as rum] 
            [tmdb-movies.app :refer [App]]
            [tmdb-movies.components.movie-page :refer [MoviePage]]))
; TODO: Move next-route to state
(def init-state 
 {:current-route "/"
  :current-component App
  :params {}})

(def routes 
  [{:component App
    :href "/"}

   {:component MoviePage
    :href "/movie/:id"}])

(def url-param-regex #":\w+")
    
(def router-state (atom init-state))

(defn href-to-regexp [url]
  (re-pattern (clojure.string/replace url url-param-regex "(.*)")))

(defn match-path [href url]
  (re-matches (href-to-regexp href) url))

; TODO consider adding macros for vector find
(defn find-route [routes next-href]
  (first (filter #(not-empty (match-path (:href %) next-href)) routes)))

(defn extract-params [href url]
  (let [values (rest (match-path href url))
        keys (map #(keyword %) (re-seq url-param-regex href))]
   (zipmap keys values)))

(defn on-hash-change []
  (let [next-href (subs (aget js/window "location" "hash") 1)
        next-route (find-route routes next-href)]
    (when-not (nil? next-route) 
      (swap! router-state assoc :current-component (:component next-route) 
                                :params (extract-params (:href next-route) next-href)))))

; (js/console.log (clj->js (equal-path? "/movie/:id/actor/:actorId" "/movie/16/actor/48")))

(.addEventListener js/window "hashchange" on-hash-change)

; TODO: Pass map with params
(rum/defcs Router < rum/reactive []
  ; (js/console.log (clj->js @router-state))
  (let [Component (:current-component (rum/react router-state))]
    [:div
      (Component {:params (:params (rum/react router-state))})]))
    
  
