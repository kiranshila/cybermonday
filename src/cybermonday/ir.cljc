(ns cybermonday.ir
  (:require
   [cybermonday.parser :as parser]
   [cybermonday.utils :refer [hiccup?]]
   [clojure.string :as str]
   [clojure.walk :as walk]))

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
                                (subs trimmed-value 1 (dec (.length trimmed-value)))
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
    (completing
     (fn [r input]
       (cond
         (and (vector? input)
              (some open-tag? input)) (do
                                        (vswap! state conj (parse-tag (get input 2))) ; [:markdown/html {} "tag"]
                                        r)
         (and (vector? input)
              (some close-tag? input)) (let [thing (peek @state)]
                                         (vswap! state pop)
                                         (if (empty? @state)
                                           (xf r thing)
                                           (do
                                             (vswap! state update (dec (count @state)) conj thing)
                                             r)))
         :else (if (empty? @state)
                 (xf r input)
                 (do
                   (vswap! state update (dec (count @state)) conj input)
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

;; Final IR postprocessing

(defn cleanup-whitespace
  "Removes excess whitespace from the resulting AST."
  [hiccup]
  (walk/postwalk
   (fn [item]
     (cond
       (string? item) (when (not (str/blank? item)) item)
       :else item))
   hiccup))

;; IR Generation

(defn md-to-ir
  "Given `md` as a string, generates a Cybermonday hiccup IR
  Inline HTML gets folded inplace and excess whitespace is removed"
  [md]
  (let [document (.parse parser/parser md)]
    (->> (parser/to-hiccup document md)
         process-inline-html
         cleanup-whitespace)))
