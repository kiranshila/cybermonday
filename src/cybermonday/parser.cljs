(ns cybermonday.parser
  (:require
   [cybermonday.utils :refer [make-hiccup-node]]
   ["remark" :as remark]
   ["unified" :as unified]
   ["remark-math" :as math]
   ["remark-parse" :as rp]
   ["remark-gfm" :as gfm]
   ["remark-deflist" :as deflist]))

(def parser (.. (unified)
                (use rp)
                (use deflist)
                (use math)
                (use gfm)))

(def node-tags
  "The default mapping from Flexmark AST node to Hiccup tag"
  {"root" :div
   "paragraph" :p
   "emphasis" :em
   "thematicBreak" :hr
   "strong" :strong
   "blockquote" :blockquote
   "listItem" :li
   "delete" :del})

(defn node-to-tag
  [node]
  (or (node-tags (.-type node))
      (throw (js/Error. (str "Got unknown AST node: " (.-type node))))))

(defmulti to-hiccup #(.-type %))

(defn map-children-to-hiccup [this]
  (map #(to-hiccup %) (.-children this)))

(defmethod to-hiccup "text" [this]
  (.-value this))

(defmethod to-hiccup "heading" [this]
  (make-hiccup-node ::heading
                    {:level (.-depth this)}
                    (map-children-to-hiccup this)))

(defmethod to-hiccup "list" [this]
  (make-hiccup-node (if (.-ordered this) :ol :ul)
                    (map-children-to-hiccup this)))

(defmethod to-hiccup "code" [this]
  [:code
   {:language (.-lang this)}
   (.-value this)])

(defmethod to-hiccup "inlineMath" [this]
  [::inline-math {} (.-value this)])

(defmethod to-hiccup "link" [this]
  (make-hiccup-node ::link
                    {:href (.-url this)
                     :title (.-title this)}
                    (map-children-to-hiccup this)))

(defmethod to-hiccup "table" [this]
  (let [alignment (.-align this)]
    [:table {}
     (for [[i row] (map-indexed vector (.-children this))]
       [:tr (for [[j cell] (map-indexed vector (.-children row))]
              (make-hiccup-node ::table-cell
                                {:header? (= i 0)
                                 :alignment (get alignment j)}
                                (map-children-to-hiccup cell)))])]))

(defmethod to-hiccup :default [this]
  (make-hiccup-node (node-to-tag this)
                    (map-children-to-hiccup this)))
