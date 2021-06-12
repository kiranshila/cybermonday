(ns cybermonday.templates
  (:require
   [cybermonday.utils :refer [hiccup? make-hiccup-node]]
   [clojure.walk :as walk]
   [clojure.string :as str]))

(def blacklisted-tags #{:pre :code})

(defn parse-templates [ast]
  (walk/postwalk
   (fn [item]
     (if (hiccup? item)
       (let [[key attr & body] item]
         (if (not (contains? blacklisted-tags key))
           (make-hiccup-node
            key
            attr
            (apply
             concat
             (for [child body]
               (if (string? child)
                 (let [split (str/split child #"\{\{|\}\}")
                       items (partition-all 2 split)]
                   (letfn [(process [[not-template template]]
                             (if template
                               [not-template [:markdown/mustache {} template]]
                               [not-template]))]
                     (mapcat process items)))
                 child))))
           item))
       item))
   ast))
