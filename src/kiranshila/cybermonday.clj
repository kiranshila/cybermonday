(ns kiranshila.cybermonday
  (:require
   [clj-yaml.core :as yaml]
   [hickory.core :as h]
   [clojure.string :as str])
  (:gen-class)
  (:import
   (com.vladsch.flexmark.util.ast Node Document)
   (com.vladsch.flexmark.parser Parser)
   (com.vladsch.flexmark.util.data MutableDataSet)
   (com.vladsch.flexmark.ast
    BlockQuote CodeBlock FencedCodeBlock Heading HtmlBlockBase IndentedCodeBlock
    BulletList OrderedList BulletListItem OrderedListItem Paragraph ThematicBreak
    Code Emphasis StrongEmphasis HardLineBreak HtmlEntity HtmlInlineBase Text ListItem
    SoftLineBreak BlockQuote Link LinkRef Reference AutoLink LinkNodeBase MailLink HtmlInline)
   (com.vladsch.flexmark.ext.tables
    TablesExtension TableBlock TableHead TableRow TableCell TableBody TableBody TableSeparator)
   (com.vladsch.flexmark.ext.gfm.strikethrough StrikethroughExtension Strikethrough)
   (com.vladsch.flexmark.ext.attributes AttributesExtension AttributesNode)))

(def options (.. (MutableDataSet.)
                 (set Parser/EXTENSIONS
                      [(TablesExtension/create)
                       (StrikethroughExtension/create)
                       (AttributesExtension/create)])
                 (toImmutable)))
(def parser (.build (Parser/builder options)))

(def node-tags
  {Paragraph :p
   Emphasis :em
   ThematicBreak :hr
   HardLineBreak :br
   Document :div
   StrongEmphasis :strong
   Strikethrough :del
   OrderedList :ol
   BulletList :ul
   BulletListItem :li
   OrderedListItem :li
   Code :code
   TableBlock :table
   TableHead :thead
   TableBody :tbody
   TableRow :tr
   BlockQuote :blockquote})

(defprotocol HiccupRepresentable
  (to-hiccup [this]))

(defn node-to-tag [node]
  (or (node-tags (type node))
      (prn node)))

(defn make-hiccup-node
  ([tag children]
   (apply vector tag (filter identity children)))
  ([tag attrs children]
   (apply vector tag attrs (filter identity children))))

(extend-protocol HiccupRepresentable
  Node
  (to-hiccup [this]
    (make-hiccup-node (node-to-tag this) (map to-hiccup (.getChildren this))))
  Text
  (to-hiccup [this]
    (str (.getChars this)))
  Heading
  (to-hiccup [this]
    (if (not-empty (.getAnchorRefId this))
      (make-hiccup-node (keyword (str "h" (.getLevel this))) {:id (str (.getAnchorRefId this))} (map to-hiccup (.getChildren this)))
      (make-hiccup-node (keyword (str "h" (.getLevel this))) (map to-hiccup (.getChildren this)))))
  ListItem
  (to-hiccup [this]
    (if (every? #(instance? Paragraph %) (.getChildren this))
      [:li (str/trim (str/join (map #(.getChars %) (.getChildren this))))]
      (make-hiccup-node (node-to-tag this) (map to-hiccup (.getChildren this)))))
  SoftLineBreak
  (to-hiccup [this]
    "\n")
  FencedCodeBlock
  (to-hiccup [this]
    [:pre (make-hiccup-node :code {:class (str (.getInfo this))} (map to-hiccup (.getChildren this)))])
  IndentedCodeBlock
  (to-hiccup [this]
    [:pre [:code (str (.getContentChars this))]])
  TableSeparator
  (to-hiccup [this] nil)
  TableCell
  (to-hiccup [this]
    (if-let [alignment (.getAlignment this)]
      (make-hiccup-node (if (.isHeader this) :th :td) {:align (str/lower-case (str alignment))} (map to-hiccup (.getChildren this)))
      (make-hiccup-node (if (.isHeader this) :th :td) (map to-hiccup (.getChildren this)))))
  HtmlBlockBase
  (to-hiccup [this]
    (h/as-hiccup (first (h/parse-fragment (str (.getContentChars this))))))
  Link
  (to-hiccup [this]
    (make-hiccup-node :a (if (empty? (.getTitle this))
                           {:href (str (.getUrl this))}
                           {:href (str (.getUrl this))
                            :title (str (.getTitle this))}) (map to-hiccup (.getChildren this))))
  Reference
  (to-hiccup [this] nil)
  LinkRef
  (to-hiccup [this]
    (let [ref (-> this
                  .getDocument
                  (.get Parser/REFERENCES)
                  (get (str (.getReference this))))]
      (make-hiccup-node :a (if (empty? (.getTitle ref))
                             {:href (str (.getUrl ref))}
                             {:href (str (.getUrl ref))
                              :title (str (.getTitle ref))}) (map to-hiccup (.getChildren this)))))
  AutoLink
  (to-hiccup [this]
    [:a {:href (str (.getUrl this))} (str (.getUrl this))])
  MailLink
  (to-hiccup [this]
    [:a {:href (str "mailto:" (.getText this))} (str (.getText this))])
  AttributesNode
  (to-hiccup [this] nil))

(defn md-to-hiccup [md]
  (let [document (.parse parser md)]
    (to-hiccup document)))

(defn parse-yaml [lines]
  (yaml/parse-string (str/join "\n" lines)))

(defn parse-toml [lines]
  {} ;FIXME
  )

(def meta-seps {"---" parse-yaml "+++" parse-toml})

(defn parse-metadata
  "Parses a markdown string into metadata (edn), returns an empty map if none"
  [md-str]
  (let [lines (str/split-lines md-str)]
    (if (contains? meta-seps (first lines))
      ((get meta-seps (first lines)) (take-while #(not= (first lines) %) (rest lines)))
      {})))

(defn parse-markdown
  "Parses a markdown string into hiccup, ignoring leading metadata"
  [md-str]
  (let [lines (str/split-lines md-str)
        md-lines (if (contains? meta-seps (first lines))
                   (->> (drop-while #(not= (first lines) %) (rest lines))
                        (drop 1))
                   lines)]
    (-> (str/join "\n" md-lines)
        (md-to-hiccup))))
