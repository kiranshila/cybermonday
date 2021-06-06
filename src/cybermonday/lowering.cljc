(ns cybermonday.lowering
  (:require
   [cybermonday.utils :refer [hiccup? make-hiccup-node gen-id]]
   [clojure.walk :as walk]
   [cybermonday.parser :as parser]))

(def default-tags
  "Deafult mappings from IR tags to HTML tags where transformation isn't required"
  {::parser/bullet-list-item :li
   ::parser/ordered-list-item :li
   ::parser/hard-line-break :br
   ::parser/definition-list :dl
   ::parser/definition-term :dd
   ::parser/definition-item :dt
   ::parser/inline-math :pre
   ::parser/image :image
   ::parser/link :a
   ::parser/autolink :a
   ::parser/html-comment nil
   ::parser/soft-line-break nil
   ::parser/attributes nil
   ::parser/reference nil
   ::parser/table-separator nil})

(defmulti lower #(first %))

(defmethod lower ::parser/heading [[_ attrs & body :as node]]
  (make-hiccup-node
   (keyword (str "h" (:level attrs)))
   (dissoc
    (assoc attrs
           :id (if (nil? (:id attrs))
                 (gen-id node)
                 (:id attrs)))
    :level)
   body))

(defmethod lower ::parser/fenced-code-block [[_ attrs & body]]
  [:pre {}
   (make-hiccup-node
    :code (dissoc (assoc attrs :class (str "language-" (:language attrs))) :language) body)])

(defmethod lower ::parser/indented-code-block [[_ attrs & body]]
  [:pre attrs
   (make-hiccup-node
    :code body)])

(defmethod lower ::parser/table-cell [[_ attrs & body]]
  (make-hiccup-node
   (if (:header? attrs) :th :td)
   (when-let [align (:alignment attrs)]
     (dissoc (assoc attrs :align align) :alignment))
   body))

(defmethod lower ::parser/mail-link [[_ {:keys [address] :as attrs}]]
  [:a (dissoc (assoc attrs :href (str "mailto:" address)) :address)])

(defmethod lower ::parser/link-ref [[_ {:keys [reference]}]]
  (lower reference))

; FIXME pretty footnotes at bottom

(defmethod lower ::parser/footnote [[_ {:keys [id]}]]
  [:sup {:id (str "fnref-" id)}
   [:a {:href (str "#fn-" id)}]])

(defmethod lower ::parser/footnote-block [[_ {:keys [id content]}]]
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
  (apply merge attrs (map second (filter #(= ::parser/attributes (first %)) body))))

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
