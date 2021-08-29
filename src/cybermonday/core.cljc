(ns cybermonday.core
  (:require
   [cybermonday.utils :as utils]
   [cybermonday.templates :as templates]
   [cybermonday.lowering :as lowering]
   [cybermonday.ir :as ir]
   #?(:cljs [cljs-bean.core :refer [->clj]])
   #?(:clj  [clj-yaml.core :as yaml]
      :cljs ["yaml" :as yaml])))

(def frontmatter-re #"(?ms)(?:^---$(.*)^---$)?(.*)")

(def parse-yaml #?(:clj yaml/parse-string
                   :cljs (comp ->clj yaml/parse)))

(defn parse-front
  "Parse only the frontmatter of a markdown file. "
  [md]
  (let [[_ fm body] (re-matches frontmatter-re md)]
    (when fm (parse-yaml fm)))
  )

(defn parse-body
  "Parse only the body of a markdown file."
  ([md opts]
   (let [[_ fm body] (re-matches frontmatter-re md)]
     (cond-> body
       true ir/md-to-ir
       (:process-templates? opts) templates/parse-templates
       true (lowering/to-html-hiccup opts)
       true utils/cleanup-whitespace)))
  ([md] (parse-md md nil)))

(defn parse-md
  "Generates HTML hiccup from markdown and associated frontmatter
  See `cybermonday.lowering/to-html-hiccup` for opts map values.
  Set `:process-templates?` to true to process mustache templates"
  ([md opts]
   {:frontmatter (parse-front md)
    :body (parse-body md opts)})
  ([md] (parse-md md nil)))

