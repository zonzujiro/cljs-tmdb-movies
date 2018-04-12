(ns tmdb-movies.router
  (:require [rum.core :as rum] 
            [clojure.string :as string]))

(def url-param-regex #":\w+")

(def init-state 
  {:route nil
   :params {}})
    
(def router-state (atom init-state))

(defn match-path [href url]
  (let [pattern (re-pattern (string/replace href url-param-regex "(.*)"))]
   (re-matches pattern url)))

; TODO consider adding macros for vector find
(defn find-route [routes next-href]
  (first (filter #(not-empty (match-path (:href %) next-href)) routes)))

; (defn find-route [routes next-href]
;   (let [collect-routes (fn [acc route]
;                          (let [matched-path (match-path (:href route) next-href)]
;                           (if (not (nil? matched-path))
;                             (if (vector? (:children route))
;                              (conj acc route (collect-routes acc (:children route)))
;                              (conj acc route))
;                            acc)))]
;     (js/console.log (clj->js (reduce collect-routes [] routes)))))

(defn find-component [next-href acc route]
  (js/console.log (clj->js acc))
  (let [matched-path (match-path (:href route) next-href)]
   (if (not (nil? matched-path))
    ; (conj acc route)
    (if (vector? (:children route))
      (conj acc route (find-component next-href acc (:children route)))
      (conj acc route))
    acc)))

(defn find-routes [routes next-href]
  (reduce (partial find-component next-href) [] routes))
          
(defn extract-params [href url]
  (let [values (rest (match-path href url))
        keys (map #(keyword %) (re-seq url-param-regex href))]
   (zipmap keys values)))

(defn select-route [routes]
  (let [next-href (subs (aget js/window "location" "hash") 1)
        next-route (find-routes routes next-href)]
    (js/console.log (clj->js next-route))))
    ; (when-not (nil? next-route) 
    ;   (swap! router-state assoc :route next-route
    ;                             :params (extract-params (:href next-route) next-href)))))

(def init-route-mixin
  {:init (fn [state]
           (let [routes (first (:rum/args state))]
            (.addEventListener js/window "hashchange" #(select-route routes))
            (select-route routes))
          state)})
; TODO: Rewright to component with inner state
(rum/defcs Router
  < rum/reactive 
    init-route-mixin
  [routes]
  ; (js/console.log (clj->js @router-state))
  (let [Component (get-in (rum/react router-state) [:route :component])]
    [:div
      (when-not (nil? Component) (Component @router-state))]))
    
  
