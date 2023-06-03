(ns cybermonday.ir
  (:require
   [cybermonday.parser :as parser]
   [cybermonday.utils :refer [hiccup? update-stack-top]]
   [clojure.string :as str]
   [clojure.walk :as walk]))

#?(:clj (set! *warn-on-reflection* true))

;; HTML Processing

(defn close-tag? [tag]
  (when (string? tag)
    (and (= \/ (second tag))
         (not (nil? (seq (re-matches #"<(.*)>" tag)))))))

(defn open-tag? [tag]
  (when (string? tag)
    (and (not= \/ (second tag))
         (not (nil? (seq (re-matches #"<(.*)>" tag)))))))

(defn contains-inner-html? [vec]
  (some #(= :markdown/html (first %)) (filter vector? vec)))

(defn html-attr-to-map [attr]
  (let [[key value] (str/split attr #"=")]
    (hash-map (keyword key) (let [trimmed-value (str/trim value)]
                              (if (= \" (first trimmed-value))
                                (subs trimmed-value 1 (dec (count trimmed-value)))
                                trimmed-value)))))

(defn parse-tag [tag]
  (let [[tag-name & attributes] (str/split (second (re-matches #"<(.*)>" tag)) #"\s+(?=\S+=)")]
    (if (open-tag? tag)
      (if (some? attributes)
        [(keyword tag-name) (apply merge (map html-attr-to-map attributes))]
        [(keyword tag-name)])
      [(keyword (apply str (rest tag-name)))])))

(defn fold-inline-html [xf]
  (let [state (volatile! [])]
    (fn
      ([] (xf))
      ([r] (xf r))
      ([r input]
       (cond
           ;; Open tag
         (and (vector? input)
              (some open-tag? input))
         (do
           (vswap! state conj (parse-tag (get input 2))) ; [:markdown/html {} "tag"]
           r)
           ;; Close tag
         (and (vector? input)
              (some close-tag? input))
         (let [thing (peek @state)]
           (vswap! state pop)
           (if (empty? @state)
             (xf r thing)
             (do
               (vswap! state update-stack-top conj thing)
               r)))
           ;; Everything else
         :else (if (empty? @state)
                 (xf r input)
                 (do
                   (vswap! state update-stack-top conj input)
                   r)))))))

(defn process-inline-html
  "Parses the html inline fragments into partial hiccup, and folds in the inner AST"
  [almost-hiccup]
  (walk/postwalk
   (fn [item]
     (if (hiccup? item)
       (if (contains-inner-html? item)
         (into [] fold-inline-html item)
         item)
       item))
   almost-hiccup))

;; IR Generation

(defn ast-to-ir
  "Trasnforms a backend-native ast to IR"
  [ast source]
  (process-inline-html (parser/to-hiccup ast source)))

(defn md-to-ir
  "Given `md` as a string, generates a Cybermonday hiccup IR
  Inline HTML gets folded inplace and excess whitespace is removed"
  [md]
  (ast-to-ir (parser/parse md) md))

(defn md-rdr-to-ir
  "Given `md` as an opened reader, generates a Cybermonday hiccup IR (JVM only)
  Inline HTML gets folded inplace and excess whitespace is removed"
  [md-rdr]
  #?(:clj (ast-to-ir (parser/parse-rdr md-rdr) md-rdr)
     :cljs (throw (js/Error. "Unimplemented"))))
