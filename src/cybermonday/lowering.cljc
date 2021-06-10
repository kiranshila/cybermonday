(ns cybermonday.lowering
  (:require
   [cybermonday.utils :refer [hiccup? make-hiccup-node gen-id]]
   [clojure.walk :as walk]))

(def default-tags
  "Deafult mappings from IR tags to HTML tags where transformation isn't required"
  {:markdown/bullet-list-item :li
   :markdown/ordered-list-item :li
   :markdown/hard-line-break :br
   :markdown/inline-math :code
   :markdown/autolink :a
   :markdown/html-comment nil
   :markdown/soft-line-break nil
   :markdown/attributes nil
   :markdown/reference nil
   :markdown/table-separator nil})

(defn lower-heading [[_ attrs & body :as node]]
  (make-hiccup-node
   (keyword (str "h" (:level attrs)))
   (dissoc
    (assoc attrs
           :id (if (nil? (:id attrs))
                 (gen-id node)
                 (:id attrs)))
    :level)
   body))

(defn lower-fenced-code-block [[_ attrs & body]]
  [:pre {}
   (make-hiccup-node
    :code (dissoc (assoc attrs :class (str "language-" (:language attrs))) :language) body)])

(defn lower-indented-code-block [[_ attrs & body]]
  [:pre attrs
   (make-hiccup-node
    :code body)])

(defn lower-table-cell [[_ attrs & body]]
  (make-hiccup-node
   (if (:header? attrs) :th :td)
   (if-let [align (:alignment attrs)]
     (dissoc (assoc attrs :align align) :alignment)
     {})
   body))

(defn lower-mail-link [[_ {:keys [address] :as attrs}]]
  [:a (dissoc (assoc attrs :href (str "mailto:" address)) :address)])

; FIXME pretty footnotes at bottom

(defn lower-footnote [[_ {:keys [id]}]]
  [:sup {:id (str "fnref-" id)}
   [:a {:href (str "#fn-" id)}]])

(defn lower-footnote-block [[_ {:keys [id content]}]]
  [:li {:id (str "fn-" id)}
   [:p
    [:span content]
    [:a {:href (str "#fnref-" id)} "↩"]]])

(defn lower-link-ref [[_ {:keys [reference]} body]]
  (when reference
    [:a {:href (:url (second reference))
         :title (:title (second reference))} body]))

(defn lower-image-ref [[_ {:keys [reference]} body]]
  (when reference
    [:img {:src (:url (second reference))
           :title (:title (second reference))} body]))

(defn lower-task-list-item [[_ {:keys [checked?]} & body]]
  [:li {}
   [make-hiccup-node
    :input
    {:checked checked?
     :disabled true
     :type "checkbox"}
    body]])

(defn lower-fallback [[tag attrs & body]]
  (if (contains? default-tags tag)
    (when-let [new-tag (default-tags tag)]
      (make-hiccup-node new-tag attrs body))
    (make-hiccup-node tag attrs body)))

(def default-lowering
  "Mapping from the IR nodes to transformation fns"
  {:markdown/heading lower-heading
   :markdown/fenced-code-block lower-fenced-code-block
   :markdown/indented-code-block lower-indented-code-block
   :markdown/table-cell lower-table-cell
   :markdown/mail-link lower-mail-link
   :markdown/footnote lower-footnote
   :markdown/footnote-block lower-footnote-block
   :markdown/task-list-item lower-task-list-item
   :markdown/link-ref lower-link-ref
   :markdown/image-ref lower-image-ref})

(defn attributes
  "Returns the attributes map of a given node, merging children attributes IR nodes"
  [[_ attrs & body]]
  (apply merge attrs (map second (filter #(= :markdown/attributes (first %)) body))))

(defn merge-attributes
  "Walks the IR tree and merges in attributes"
  [ir]
  (walk/postwalk
   (fn [item]
     (if (hiccup? item)
       (assoc item 1 (attributes item))
       item))
   ir))

(defn lower-ir
  "Transforms the IR tree by lowering nodes to their HTML representation"
  ([ir lowering-map]
   (let [final-map (conj default-lowering lowering-map)]
     (walk/prewalk
      (fn [item]
        (if (hiccup? item)
          (let [tag (first item)]
            (if-let [transform-fn (tag final-map)]
              (transform-fn item)
              (if (contains? default-tags tag)
                (lower-fallback item)
                item)))
          item))
      ir)))
  ([ir] (lower-ir ir default-lowering)))

(defn deep-merge
  "Recursively merges maps and vectors"
  [& contents]
  (cond
    (every? vector? contents) (reduce into contents)
    (some map? contents) (apply merge-with deep-merge contents)))

(defn apply-post-attrs
  "Merges in attributes for the final HTML ast from given map of html keywords and attributes"
  [hiccup attr-map]
  (walk/postwalk
   (fn [item]
     (if (hiccup? item)
       (let [[tag attrs & body] item]
         (apply vector tag (deep-merge attrs (attr-map tag)) body))
       item))
   hiccup))

(defn to-html-hiccup
  "Transforms a cybermonday IR into standard HTML hiccup
  In an optional options map:
  `:lower-fns` supplies a mapping from IR keyword to lowering fn
  `default-attrs` supplies a mapping from HTML keyword to default node attributes"
  [ir & [{:keys [lower-fns default-attrs]}]]
  (-> (merge-attributes ir)
      (lower-ir (or lower-fns default-lowering))
      (apply-post-attrs (or default-attrs {}))))
