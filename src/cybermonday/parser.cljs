(ns cybermonday.parser
  (:require
   [cybermonday.utils :refer [make-hiccup-node html-comment-re]]
   ["remark" :as remark]
   ["unified" :as unified]
   ["remark-math" :as math]
   ["remark-parse" :as rp]
   ["remark-footnotes" :as footnotes]
   ["html-entities" :as entities]
   #_["mdast-util-definitions" :as definitions]
   ["remark-gfm" :as gfm]))

(def parser (.. (unified)
                (use rp)
                (use footnotes)
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
  (entities/decode (.-value this)))

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
    (make-hiccup-node
     :table
     (for [[i row] (map-indexed vector (.-children this))]
       (make-hiccup-node
        :tr
        (for [[j cell] (map-indexed vector (.-children row))]
          (make-hiccup-node ::table-cell
                            {:header? (= i 0)
                             :alignment (get alignment j)}
                            (map-children-to-hiccup cell))))))))

(defmethod to-hiccup "linkReference" [this]
  (make-hiccup-node ::link-ref
                    {:reference nil}
                    (map-children-to-hiccup this)))

(defmethod to-hiccup "definition" [this]
  [::reference {:title (.-title this)
                :label (.-label this)
                :href (.-url this)}])

(defmethod to-hiccup "html" [this]
  ;FIXME this needs work
  (let [body (.-value this)]
    (if-let [[_ comment] (re-matches html-comment-re body)]
      [::html-comment {} comment]
      [::html {} body])))

(defmethod to-hiccup "footnoteReference" [this]
  [::footnote {:id (.-identifier this)}])

(defmethod to-hiccup "footnoteDefinition" [this]
  ;FIXME to match behavior of flexmark
  [::footnote-block {:id (.-identifier this)
                     :content (make-hiccup-node :div (map-children-to-hiccup this))}])

(defmethod to-hiccup :default [this]
  (make-hiccup-node (node-to-tag this)
                    (map-children-to-hiccup this)))
