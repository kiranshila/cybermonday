(ns cybermonday.lowering
  (:require
   [cybermonday.utils :refer [hiccup? make-hiccup-node gen-id]]
   [clojure.walk :as walk]))

(def default-tags
  "Deafult mappings from IR tags to HTML tags where transformation isn't required"
  {:markdown/bullet-list-item :li
   :markdown/ordered-list-item :li
   :markdown/hard-line-break :br
   :markdown/inline-math :pre
   :markdown/autolink :a
   :markdown/html-comment nil
   :markdown/soft-line-break nil
   :markdown/attributes nil
   :markdown/reference nil
   :markdown/table-separator nil})

(defmulti lower #(first %))

(defmethod lower :markdown/heading [[_ attrs & body :as node]]
  (make-hiccup-node
   (keyword (str "h" (:level attrs)))
   (dissoc
    (assoc attrs
           :id (if (nil? (:id attrs))
                 (gen-id node)
                 (:id attrs)))
    :level)
   body))

(defmethod lower :markdown/fenced-code-block [[_ attrs & body]]
  [:pre {}
   (make-hiccup-node
    :code (dissoc (assoc attrs :class (str "language-" (:language attrs))) :language) body)])

(defmethod lower :markdown/indented-code-block [[_ attrs & body]]
  [:pre attrs
   (make-hiccup-node
    :code body)])

(defmethod lower :markdown/table-cell [[_ attrs & body]]
  (make-hiccup-node
   (if (:header? attrs) :th :td)
   (if-let [align (:alignment attrs)]
     (dissoc (assoc attrs :align align) :alignment)
     {})
   body))

(defmethod lower :markdown/mail-link [[_ {:keys [address] :as attrs}]]
  [:a (dissoc (assoc attrs :href (str "mailto:" address)) :address)])

(defmethod lower :markdown/link-ref [[_ {:keys [reference]}]]
  (lower reference))

; FIXME pretty footnotes at bottom

(defmethod lower :markdown/footnote [[_ {:keys [id]}]]
  [:sup {:id (str "fnref-" id)}
   [:a {:href (str "#fn-" id)}]])

(defmethod lower :markdown/footnote-block [[_ {:keys [id content]}]]
  [:li {:id (str "fn-" id)}
   [:p
    [:span content]
    [:a {:href (str "#fnref-" id)} "↩"]]])

(defmethod lower :default [[tag attrs & body]]
  (if (contains? default-tags tag)
    (when-let [new-tag (default-tags tag)]
      (make-hiccup-node new-tag attrs body))
    (make-hiccup-node tag attrs body)))

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
  [ir]
  (walk/postwalk
   (fn [item]
     (if (hiccup? item)
       (lower item)
       item))
   ir))

(defn to-html-hiccup
  "Transforms a cybermonday IR into standard HTML hiccup"
  [ir]
  (-> (merge-attributes ir)
      lower-ir))
