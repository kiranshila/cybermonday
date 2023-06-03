(ns cybermonday.parser
  (:require
   [cybermonday.utils :refer [make-hiccup-node html-comment-re]]
   ["unified" :refer [unified]]
   ["remark-math$default" :as math]
   ["remark-parse$default" :as rp]
   ["remark-footnotes$default" :as footnotes]
   ["html-entities" :as entities]
   ["remark-gfm$default" :as gfm]
   [clojure.string :as str]))

(def parser (.. (unified)
                (use rp)
                (use footnotes)
                (use math)
                (use gfm)))

(defn parse
  "Invoke the native parser"
  [md]
  (.parse parser md))

(def node-tags
  "The default mapping from Flexmark AST node to Hiccup tag"
  {"root" :div
   "paragraph" :p
   "emphasis" :em
   "thematicBreak" :hr
   "strong" :strong
   "blockquote" :blockquote
   "listItem" :li
   "delete" :del
   "break" :br})

(defn node-to-tag
  [node]
  (or (node-tags (.-type node))
      (throw (js/Error. (str "Got unknown AST node: " (.-type node))))))

(defmulti transform (fn [node _ _] (.-type node)))

(defn transform-children [this defs source]
  (map #(transform % defs source) (.-children this)))

(defmethod transform "text" [this _ _]
  (entities/decode (.-value this)))

(defmethod transform "inlineMath" [this _ _]
  (if-let [match (re-matches #"`(.*)`" (.-value this))]
    [:markdown/inline-math {} (second match)]
    (str "$" (.-value this) "$"))) ; return the string verbatim if we don't match flexmark

(defmethod transform "heading" [this defs source]
  (make-hiccup-node :markdown/heading
                    {:level (.-depth this)}
                    (transform-children this defs source)))

(defmethod transform "list" [this defs source]
  (let [ordered? (.-ordered this)
        li-type (if ordered?
                  :markdown/ordered-list-item
                  :markdown/bullet-list-item)]
    (make-hiccup-node
     (if ordered? :ol :ul)
     (for [child (.-children this)
           :let [checked? (.-checked child)
                 li-type (if (nil? checked?)
                           li-type
                           :markdown/task-list-item)]]
       (make-hiccup-node
        li-type
        (if (nil? checked?)
          {}
          {:checked? checked?
           :ordered? ordered?})
        (transform-children child defs source))))))

(defmethod transform "code" [this _ source]
  (let [start-pos (.. this -position -start -offset)
        lang (.-lang this)
        body (.-value this)]
    (case (aget source start-pos)
      "`"  [:markdown/fenced-code-block {:language lang} body]
      " "  [:markdown/indented-code-block {} body]
      "\t" [:markdown/indented-code-block {} body])))

(defmethod transform "math" [this _ _]
  (str "$$\n" (.-value this) "\n$$")) ; return in place, don't use this block math format to match flexmark

(defmethod transform "inlineCode" [this _ _]
  [:code {} (.-value this)])

(defmethod transform "link" [this defs source]
  (let [start-pos (.. this -position)
        url (.-url this)]
    (if (and start-pos
             (= \< (aget source (.. start-pos -start -offset)))) ; Autolink check
      (if (str/starts-with? url "mailto:")
        [:markdown/mail-link {:address (subs url 7)}]
        [:markdown/autolink {:href url}])
      (make-hiccup-node :a
                        {:href url
                         :title (.-title this)}
                        (transform-children this defs source)))))

(defmethod transform "table" [this defs source]
  (let [alignment (.-align this)
        rows (.-children this)]
    (letfn [(hiccup-each-cell [row header?]
              (for [[j cell] (map-indexed vector (.-children row))]
                (make-hiccup-node
                 :markdown/table-cell
                 {:header? header?
                  :alignment (get alignment j)}
                 (transform-children cell defs source))))]
      [:table {}
       [:thead {}
        (make-hiccup-node :tr (hiccup-each-cell (first rows) true))]
       (make-hiccup-node
        :tbody
        (for [row (rest rows)]
          (make-hiccup-node :tr (hiccup-each-cell row false))))])))

(defmethod transform "linkReference" [this defs source]
  (make-hiccup-node :markdown/link-ref
                    {:reference (defs (.-identifier this))}
                    (transform-children this defs source)))

(defmethod transform "imageReference" [this defs source]
  (make-hiccup-node :markdown/image-ref
                    {:reference (defs (.-identifier this))}
                    (transform-children this defs source)))

(defmethod transform "definition" [this _ _]
  [:markdown/reference {:title (.-title this)
                        :label (.-identifier this)
                        :url (.-url this)}])

(defmethod transform "image" [this _ _]
  [:img {:src (.-url this)
         :alt (.-alt this)
         :title (.-title this)}])

(defmethod transform "html" [this _ _]
  (let [body (.-value this)]
    (if-let [[_ comment] (re-matches html-comment-re body)]
      [:markdown/html-comment {} comment]
      [:markdown/html {} body])))

(defmethod transform "footnoteReference" [this _ _]
  [:markdown/footnote {:id (.-identifier this)}])

(defmethod transform "footnoteDefinition" [this defs source]
  ;FIXME to match behavior of flexmark
  [:markdown/footnote-block {:id (.-identifier this)
                             :content (make-hiccup-node :div (transform-children this defs source))}])

(defmethod transform :default [this defs source]
  (make-hiccup-node (node-to-tag this)
                    (transform-children this defs source)))

(defn collect-definitions [node]
  (if (= "definition" (.-type node))
    (let [def (transform node)]
      {(:label (second def)) def})
    (into {} (for [child (.-children node)]
               (collect-definitions child)))))

(defn to-hiccup [ast source]
  (transform ast (collect-definitions ast) source))
