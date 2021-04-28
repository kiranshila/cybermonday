(ns cybermonday.core
  (:require
   [cybermonday.lowering :refer [to-html-hiccup]]
   [cybermonday.ir :refer [md-to-ir]]
   [clojure.java.io :as io]))

(defn parse-md
  "Generates HTML hiccup from markdown"
  [md]
  (->> (md-to-ir md)
       to-html-hiccup))
