(ns kiranshila.generate-test
  (:require
   [clojure.test :as t]
   [clojure.java.io :as io]
   [clojure.pprint :refer [pprint]]
   [clojure.walk :refer [postwalk]]
   [clojure.string :as s]
   [hickory.core :refer [parse-fragment as-hiccup]]))

(defn get-spec [uri file]
  (with-open [in (io/input-stream uri)
              out (io/output-stream file)]
    (io/copy in out)))

(get-spec "https://raw.githubusercontent.com/commonmark/commonmark-spec/0.29/spec.txt" "resources/spec.txt")

(def spec (clojure.java.io/resource "spec.txt"))
(def regex #"(?ms)^`{32}\s*example\n([\s\S]*?)^\.\n([\s\S]*?)^`{32}$")

(def test-ns `(ns kiranshila.cybermonday-generated-test
                (:require [clojure.test :refer :all]
                          [kiranshila.cybermonday :refer :all]
                          [kiranshila.generate-test :refer [test-hiccup]])))

(defmacro test-hiccup [name md-str & hiccup]
  `(testing ~name (is (= '~hiccup
                         (md-to-hiccup ~md-str)))))

(defn clean-predicate [x]
  (if-not (= x {})
    (if (string? x)
      (if-not (s/blank? x)
        true)
      true)))

(def clean-transducer
  (comp
   (filter clean-predicate)
   (map (fn [x]
          (if (string? x)
            (s/trim x)
            x)))))

(defn clean-hiccup [h]
  (postwalk (fn [element]
              (if (and (vector? element)
                       (not= :href (first element)))
                (into [] clean-transducer element)
                element)) h))

(defn html-to-hiccup [html]
  (->> (parse-fragment html)
       (map as-hiccup)
       (filter vector?)
       #_(map clean-hiccup)))

(defn spec-to-test [example md html]
  (letfn [(arrow-replace [s] (clojure.string/replace s #"→" "\t"))]
    `(test-hiccup
       ~(str "Example " example)
       ~(arrow-replace md)
       ~@(html-to-hiccup html))))

(defn write-test-file [filename]
  (let [spec-parsed (re-seq regex (slurp spec))
        out (io/writer filename)]
    (pprint test-ns out)
    (doseq [[i [_ md html]] (map-indexed vector spec-parsed)]
      (pprint (spec-to-test (inc i) md html) out))))

(write-test-file "test.clj")
