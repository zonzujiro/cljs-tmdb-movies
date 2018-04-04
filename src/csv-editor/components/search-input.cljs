(ns tmdb-movies.components.search-input
  (:require [rum.core :as rum]))

(rum/defc SearchInput [{:keys [handle-submit handle-change value error?]}]
  (let [on-submit #(-> (.preventDefault %) (handle-submit))
        on-change #(-> % .-target .-value handle-change)]
    [:form.city-input {:on-submit on-submit}
      [:input {:type "text" :name "city" :value value :on-change on-change}]]))
