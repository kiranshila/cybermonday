(ns cybermonday.core
  (:require
   [cybermonday.lowering :refer [to-html-hiccup]]
   [cybermonday.ir :refer [md-to-ir]]
   [clj-yaml.core :as yaml]))

(def frontmatter-re #"(?ms)(?:^---$(.*)^---$)?(.*)")

(defn parse-md
  "Generates HTML hiccup from markdown and associated frontmatter"
  [md]
  (let [[_ fm body] (re-matches frontmatter-re md)]
    {:frontmatter (when fm (yaml/parse-string fm))
     :body (->> body
                md-to-ir
                to-html-hiccup)}))
