(ns cybermonday.core
  (:require
   [cybermonday.lowering :refer [to-html-hiccup]]
   [cybermonday.ir :refer [md-to-ir]]
   #?(:clj [clj-yaml.core :as yaml]
      :cljs ["yaml" :as yaml])))

(def frontmatter-re #"(?ms)(?:^---$(.*)^---$)?(.*)")

(def parse-yaml #?(:clj yaml/parse-string
                   :cljs yaml/parse))

(defn parse-md
  "Generates HTML hiccup from markdown and associated frontmatter"
  [md]
  (let [[_ fm body] (re-matches frontmatter-re md)]
    {:frontmatter (when fm (parse-yaml fm))
     :body (->> body
                md-to-ir
                to-html-hiccup)}))
