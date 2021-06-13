(ns cybermonday.core
  (:require
   [cybermonday.utils :as utils]
   [cybermonday.templates :as templates]
   [cybermonday.lowering :as lowering]
   [cybermonday.ir :as ir]
   #?(:clj [clj-yaml.core :as yaml]
      :cljs ["yaml" :as yaml])))

(def frontmatter-re #"(?ms)(?:^---$(.*)^---$)?(.*)")

(def parse-yaml #?(:clj yaml/parse-string
                   :cljs yaml/parse))

(defn parse-md
  "Generates HTML hiccup from markdown and associated frontmatter
  See `cybermonday.lowering/to-html-hiccup` for opts map values.
  Set `:process-templates?` to true to process mustache templates"
  ([md opts]
   (let [[_ fm body] (re-matches frontmatter-re md)]
     {:frontmatter (when fm (parse-yaml fm))
      :body (cond-> body
              true ir/md-to-ir
              (:process-templates? opts) templates/parse-templates
              true (lowering/to-html-hiccup opts)
              true utils/cleanup-whitespace)}))
  ([md] (parse-md md nil)))
