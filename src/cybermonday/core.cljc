(ns cybermonday.core
  (:require
   [clojure.string :as string]
   [cybermonday.utils :as utils]
   [cybermonday.lowering :as lowering]
   [cybermonday.ir :as ir]
   #?(:org.babashka/nbb [clojure.core :refer [js->clj] :rename {js->clj ->clj}]
      :cljs [cljs-bean.core :refer [->clj]])
   #?(:clj  [clj-yaml.core :as yaml]
      :cljs ["yaml" :as yaml]))
  #?(:clj (:import (java.io Reader))))

#?(:clj (set! *warn-on-reflection* true))

(def read-ahead-limit 256) ;; Maximum number of bytes to peek when we test for frontmatter
(def parse-yaml #?(:clj yaml/parse-string
                   :cljs (comp ->clj yaml/parse)))

#?(:clj
   (defn -parse-front-rdr
     "Parse frontmatter from a markdown reader, return the frontmatter and the reader to parse the remaining md"
     [^Reader md-rdr]
     (.mark  md-rdr read-ahead-limit)
     (let [lines (line-seq md-rdr)]
       (if (= (first lines) "---")
         {:frontmatter (->> (take-while (partial not= "---") (rest lines))
                            (string/join "\n")
                            parse-yaml)
          :body md-rdr}
         {:frontmatter nil
          :body (do (.reset md-rdr) md-rdr)}))))

(defn -parse-front-str
  "Parse frontmatter from a markdown string, return the frontmatter and the substring of the reamining md"
  [^String md]
  (if (string/starts-with? md "---\n")
    (let [idx (string/index-of md "---\n" 4)]
      {:frontmatter (parse-yaml (subs md 4 idx))
       :body (subs md idx)})
    {:frontmatter nil
     :body md}))

(defn parse-front
  "Parse only the frontmatter of a markdown file."
  [md]
  (:frontmatter #?(:clj (if (instance? Reader md)
                          (-parse-front-rdr md)
                          (-parse-front-str md))
                   :cljs (-parse-front-str md))))

(defn -parse-body-str
  ([md opts]
   (-> (ir/md-to-ir md)
       (lowering/to-html-hiccup opts)
       utils/cleanup))
  ([md] (-parse-body-str md nil)))

(defn -parse-body-rdr
  ([md-rdr opts]
   (-> (ir/md-rdr-to-ir md-rdr)
       (lowering/to-html-hiccup opts)
       utils/cleanup))
  ([md-rdr] (-parse-body-rdr md-rdr nil)))

(defn parse-md
  "Generates HTML hiccup from markdown and associated frontmatter
  See `cybermonday.lowering/to-html-hiccup` for opts map values."
  ([md opts]
   #?(:cljs (update (-parse-front-str md) :body #(-parse-body-str % opts))
      :clj (if (instance? Reader md)
             (update (-parse-front-rdr md) :body #(-parse-body-rdr % opts))
             (update (-parse-front-str md) :body #(-parse-body-str % opts)))))
  ([md] (parse-md md nil)))

(defn parse-body
  "Parse only the body of a markdown file."
  ([md opts]
   (:body (parse-md md opts)))
  ([md] (parse-body md nil)))
