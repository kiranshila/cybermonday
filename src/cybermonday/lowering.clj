(ns cybermonday.lowering
  (:require
   [cybermonday.utils :refer [hiccup?]]
   [clojure.walk :as walk]
   [cybermonday.ir :as ir]
   [clojure.string :as str]))

(defmulti lower #(first %))

(defmethod lower ::ir/bullet-list-item [node]
  (assoc node 0 :li))

(defmethod lower ::ir/ordered-list-item [node]
  (assoc node 0 :li))

(defmethod lower ::ir/hard-line-break [_]
  [:br])

(defmethod lower ::ir/soft-line-break [_]
  nil)

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

(defmethod lower ::ir/indented-code-block [_ body]
  [:pre [:code body]])

(defmethod lower ::ir/table-separator [_]
  nil)

(defmethod lower ::ir/table-cell [[_ attrs body]]
  [(if (:header? attrs) :th :td)
   (when-let [align (:alignment attrs)]
     {:align align})
   body])

(defmethod lower ::ir/link [node]
  (assoc node 0 :a))

(defmethod lower ::ir/autolink [node]
  (assoc node 0 :a))

(defmethod lower ::ir/mail-link [[_ {:keys [address]}]]
  [:a {:href (str "mailto:" address)}])

(defmethod lower ::ir/html-comment [_]
  nil)

(defmethod lower ::ir/reference [_]
  nil)

(defmethod lower ::ir/link-ref [[_ {:keys [reference]}]]
  (lower reference))

(defmethod lower ::ir/image [node]
  (assoc node 0 :image))

(defmethod lower ::ir/attributes [_]
  nil)

(defmethod lower ::ir/definition-list [node]
  (assoc node 0 :dl))

(defmethod lower ::ir/definition-term [node]
  (assoc node 0 :dd))

(defmethod lower ::ir/definition-item [node]
  (assoc node 0 :dt))

(defmethod lower ::ir/inline-math [node]
  (assoc node 0 :pre))

; FIXME pretty footnotes at bottom

(defmethod lower ::ir/footnote [[_ {:keys [id]}]]
  [:sup {:id (str "fnref-" id)}
   [:a {:href (str "#fn-" id)}]])

(defmethod lower ::ir/footnote-block [[_ {:keys [id content]}]]
  [:li {:id (str "fn-" id)}
   [:p
    [:span content]
    [:a {:href (str "#fnref-" id)} "↩"]]])

(defmethod lower :default [node]
  node)

(defn merge-attributes
  "Merges in explicit attributes into the attributes of the parent node"
  [ir])

(defn to-html-hiccup
  "Transforms a cybermonday IR into standard HTML hiccup"
  [ir]
  (walk/postwalk
   (fn [item]
     (if (hiccup? item)
       (lower item)
       item))
   ir))
