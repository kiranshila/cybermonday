(ns kiranshila.cybermonday
  (:require
   [instaparse.core :as insta]
   [clj-yaml.core :as yaml])
  (:gen-class))

(def parse-md
  (insta/parser (clojure.java.io/resource "commonmark.bnf")))

(def test-text (slurp (clojure.java.io/resource "test_common.md")))

(defn p-to-hiccup [[_ & lines]]
  (->> (for [line lines]
         (apply str (rest line)))
       (cons :p)
       (into [])))

(defn ul-to-hiccup [[_ & lis]]
  [:ul (for [li lis]
         [:li (apply str (rest li))])])

(defn blocks-to-hiccup [blocks]
  (for [block blocks]
    (case (first block))))

(defn parse-yaml [lines]
  (yaml/parse-string (clojure.string/join "\n" lines)))

(defn md-to-hiccup [md]
  (parse-md md))

(defn parse-metadata [lines sep]
  (let [[first-line & lines] lines]
    (if (= sep first-line)
      (parse-yaml (take-while #(not= sep %) lines))
      {})))

(defn parse-markdown [lines sep]
  (let [[first-line & rest-lines] lines
        result-lines (if (= sep first-line)
                       (drop 1 (drop-while #(not= sep %) rest-lines))
                       lines)]
    (-> (clojure.string/join "\n" result-lines)
        (str "\n")
        md-to-hiccup)))

(def result (with-open [rdr (clojure.java.io/reader (clojure.java.io/resource "test_common.md"))]
              (parse-markdown (line-seq rdr) "---")))
