(ns cybermonday.lowering
  (:require
   [cybermonday.utils :refer [hiccup? make-hiccup-node]]
   [clojure.walk :as walk]
   [cybermonday.ir :as ir]
   [clojure.string :as str]))

(def default-tags
  "Deafult mappings from IR tags to HTML tags where transformation isn't required"
  {::ir/bullet-list-item :li
   ::ir/ordered-list-item :li
   ::ir/hard-line-break :br
   ::ir/definition-list :dl
   ::ir/definition-term :dd
   ::ir/definition-item :dt
   ::ir/inline-math :pre
   ::ir/image :image
   ::ir/link :a
   ::ir/autolink :a
   ::ir/html-comment nil
   ::ir/soft-line-break nil
   ::ir/attributes nil
   ::ir/reference nil
   ::ir/table-separator nil})

(defmulti lower #(first %))

(defmethod lower ::ir/heading [[_ {:keys [level id] :as attrs} & body]]
  (make-hiccup-node
   (keyword (str "h" level))
   (dissoc
    (assoc attrs
           :id (if (nil? id)
                 (-> body
                     str/lower-case
                     (str/replace #"\s+" "-"))
                 id))
    :level)
   body))

(defmethod lower ::ir/fenced-code-block [[_ attrs & body]]
  [:pre {}
   (make-hiccup-node
    :code (dissoc (merge {:class (str "language-" (:language attrs))} attrs) :language) body)])

(defmethod lower ::ir/indented-code-block [[_ attrs & body]]
  [:pre attrs
   (make-hiccup-node
    :code body)])

(defmethod lower ::ir/table-cell [[_ attrs & body]]
  (make-hiccup-node
   (if (:header? attrs) :th :td)
   (when-let [align (:alignment attrs)]
     (dissoc (merge {:align align} attrs) :alignment))
   body))

(defmethod lower ::ir/mail-link [[_ {:keys [address] :as attrs}]]
  [:a (dissoc (merge {:href (str "mailto:" address)} attrs) :address)])

(defmethod lower ::ir/link-ref [[_ {:keys [reference]}]]
  (lower reference))

; FIXME pretty footnotes at bottom

(defmethod lower ::ir/footnote [[_ {:keys [id]}]]
  [:sup {:id (str "fnref-" id)}
   [:a {:href (str "#fn-" id)}]])

(defmethod lower ::ir/footnote-block [[_ {:keys [id content]}]]
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
  (apply merge attrs (map second (filterv #(= ::ir/attributes (first %)) body))))

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
