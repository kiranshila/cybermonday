(ns kiranshila.cybermonday
  (:require
   [instaparse.core :as insta]
   [clj-yaml.core :as yaml])
  (:gen-class))

(def header-map {:H1 :h1
                 :H2 :h2
                 :H3 :h3
                 :H4 :h4
                 :H5 :h5
                 :H6 :h6})

(def parse-md
  (insta/parser (clojure.java.io/resource "commonmark.bnf")))

(def test-text (slurp (clojure.java.io/resource "test_common.md")))

(def ast (parse-md test-text))

(defn h-to-hiccup [[_ & [header]]]
  [(get header-map (first header))
   (apply str (rest header))])

(defn p-to-hiccup [[_ & lines]]
  (->> (for [line lines]
         (apply str (rest line)))
       (cons :p)
       (into [])))

(defn ul-to-hiccup [[_ & lis]]
  [:ul (for [li lis]
         [:li (apply str (rest li))])])

(defn blocks-to-hiccup [blocks]
  (->> (for [block blocks]
         (case (first block)
           :Paragraph
           (p-to-hiccup block)
           :Header
           (h-to-hiccup block)
           :List
           (ul-to-hiccup block)))
       (cons :div)
       (into [])))

(defn parse-yaml [lines]
  (yaml/parse-string (clojure.string/join "\n" lines)))

(defn md-str-to-hiccup [md]
  (let [result (parse-md md)]
    (blocks-to-hiccup result)))

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
        (md-str-to-hiccup))))

#_(def result (with-open [rdr (clojure.java.io/reader (clojure.java.io/resource "test.md"))]
              (parse-markdown (line-seq rdr) "---")))
