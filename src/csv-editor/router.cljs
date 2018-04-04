(ns tmdb-movies.router
  (:require [rum.core :as rum] 
            [tmdb-movies.app :refer [App]]))

(def init-state 
 {:current-route "/"
  :current-component App})

(def routes 
  [{:component App
    :href "/"}])

(def router-state (atom init-state))

(defn on-hash-change []
  (let [next-href (subs (aget js/window "location" "hash") 1)
        next-route (first (filter #(= next-href (:href %)) routes))]
    (when-not (nil? next-route) (swap! router-state assoc :current-component (:component next-route)))))

(.addEventListener js/window "hashchange" on-hash-change)

(rum/defcs Router < rum/reactive []
  (js/console.log (clj->js @router-state))
  (let [Component (:current-component (rum/react router-state))]
    [:div
      (Component)]))
    
  
