(ns cybermonday.lowering
  (:require
   [cybermonday.utils :refer [hiccup?]]
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

(defmethod lower ::ir/heading [[_ {:keys [level id]} body]]
  [(keyword (str "h" level))
   {:id (if (nil? id)
          (-> body
              str/lower-case
              (str/replace #"\s+" "-"))
          id)}
   body])

(defmethod lower ::ir/fenced-code-block [[_ attrs body]]
  [:pre [:code {:class (str "language-" (:language attrs))} body]])

(defmethod lower ::ir/indented-code-block [[_ attrs body]]
  [:pre attrs [:code body]])

(defmethod lower ::ir/table-cell [[_ attrs body]]
  [(if (:header? attrs) :th :td)
   (when-let [align (:alignment attrs)]
     {:align align})
   body])

(defmethod lower ::ir/mail-link [[_ {:keys [address]}]]
  [:a {:href (str "mailto:" address)}])

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

(defmethod lower :default [[tag attrs body]]
  (if (contains? default-tags tag)
    (when-let [new-tag (default-tags tag)]
      [new-tag attrs body])
    [tag attrs body]))

(defn merge-attributes
  "Merges in explicit attributes into the attributes of the parent node"
  [ir]
  ; FIXME
  )

(defn to-html-hiccup
  "Transforms a cybermonday IR into standard HTML hiccup"
  [ir]
  (walk/postwalk
   (fn [item]
     (if (hiccup? item)
       (lower item)
       item))
   ir))
