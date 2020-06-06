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
    SoftLineBreak BlockQuote Link LinkRef Reference AutoLink LinkNodeBase MailLink HtmlInline
    HtmlCommentBlock)
   (com.vladsch.flexmark.ext.tables
    TablesExtension TableBlock TableHead TableRow TableCell TableBody TableBody TableSeparator)
   (com.vladsch.flexmark.ext.gfm.strikethrough StrikethroughExtension Strikethrough)
   (com.vladsch.flexmark.ext.attributes AttributesExtension AttributesNode)
   (com.vladsch.flexmark.test.util AstCollectingVisitor)))

(def options (.. (MutableDataSet.)
                 (set Parser/EXTENSIONS
                      [(TablesExtension/create)
                       (StrikethroughExtension/create)
                       (AttributesExtension/create)])
                 (toImmutable)))

(def parser (.build (Parser/builder options)))

(defn print-ast [document]
  (println (.. (AstCollectingVisitor.)
               (collectAndGetAstText document))))

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
  (to-hiccup [this options]))

(defn node-to-tag [node]
  (or (node-tags (type node))
      (prn node)))

(defn make-hiccup-node
  ([tag children]
   (apply vector tag (filter identity children)))
  ([tag attrs children]
   (apply vector tag attrs (filter identity children))))

(defn map-children-to-hiccup [node options]
  (map #(to-hiccup % options) (.getChildren node)))

(defn highlight-js-code-block-no-jupyter [language child-hiccup]
  (let [lang (str/join (str/split language #"jupyter-"))]
    (if (not-empty lang)
      [:pre (make-hiccup-node :code {:class lang} child-hiccup)]
      [:pre (make-hiccup-node :code child-hiccup)])))

(defn hiccup? [element]
  (and (vector? element)
       (keyword? (first element))))

(defn close-tag? [tag]
  (when (string? tag)
    (and (= \/ (second tag))
         (not (empty? (re-matches #"<(.*)>" tag))))))

(defn open-tag? [tag]
  (when (string? tag)
    (and (not= \/ (second tag))
         (not (empty? (re-matches #"<(.*)>" tag))))))

(defn contains-open-tag? [vec]
  (when (vector? vec)
    (some open-tag? vec)))

(defn contains-close-tag? [vec]
  (when (vector? vec)
    (some close-tag? vec)))

(defn contains-inner-html? [vec]
  (when (some vector? vec)
    (some #(and (vector? %1) (open-tag? (second %1))) vec)))

(defn html-attr-to-map [attr]
  (let [[key value] (str/split attr #"=")]
    (hash-map (keyword key) (let [trimmed-value (str/trim value)]
                              (if (= \" (first trimmed-value))
                                (subs trimmed-value 1 (dec (.length trimmed-value)))
                                trimmed-value)))))

(defn parse-tag [tag]
  (let [[tag-name & attributes] (str/split (second (re-matches #"<(.*)>" tag)) #" ")]
    (if (open-tag? tag)
      (if (some? attributes)
        [(keyword tag-name) (apply merge (map html-attr-to-map attributes))]
        [(keyword tag-name)])
      [(keyword (apply str (rest tag-name)))])))

(extend-protocol HiccupRepresentable
  Node
  (to-hiccup [this options]
    (make-hiccup-node (node-to-tag this)
                      (map-children-to-hiccup this options)))
  Text
  (to-hiccup [this options]
    (str (.getChars this)))
  Heading
  (to-hiccup [this options]
    (if (not-empty (.getAnchorRefId this))
      (make-hiccup-node (keyword (str "h" (.getLevel this)))
                        {:id (str (.getAnchorRefId this))}
                        (map-children-to-hiccup this options))
      (make-hiccup-node (keyword (str "h" (.getLevel this)))
                        (map-children-to-hiccup this options))))
  ListItem
  (to-hiccup [this options]
    (if (every? #(instance? Paragraph %) (.getChildren this))
      [:li (str/trim (str/join (map #(.getChars %) (.getChildren this))))]
      (make-hiccup-node (node-to-tag this) (map-children-to-hiccup this options))))
  SoftLineBreak
  (to-hiccup [this options] "\n")
  FencedCodeBlock
  (to-hiccup [this options]
    ((:code-format options) (str (.getInfo this)) (map-children-to-hiccup this options)))
  IndentedCodeBlock
  (to-hiccup [this options]
    [:pre [:code (str (.getContentChars this))]])
  TableSeparator
  (to-hiccup [this options] nil)
  TableCell
  (to-hiccup [this options]
    (if-let [alignment (.getAlignment this)]
      (make-hiccup-node (if (.isHeader this) :th :td)
                        {:align (str/lower-case (str alignment))}
                        (map-children-to-hiccup this options))
      (make-hiccup-node (if (.isHeader this) :th :td)
                        (map-children-to-hiccup this options))))
  HtmlBlockBase
  (to-hiccup [this options]
    (h/as-hiccup (first (h/parse-fragment (str (.getContentChars this))))))
  Link
  (to-hiccup [this options]
    (make-hiccup-node :a (if (empty? (.getTitle this))
                           {:href (str (.getUrl this))}
                           {:href (str (.getUrl this))
                            :title (str (.getTitle this))})
                      (map-children-to-hiccup this options)))
  Reference
  (to-hiccup [this options] nil)
  LinkRef
  (to-hiccup [this options]
    (let [ref (-> this
                  .getDocument
                  (.get Parser/REFERENCES)
                  (get (str (.getReference this))))]
      (make-hiccup-node :a (if (empty? (.getTitle ref))
                             {:href (str (.getUrl ref))}
                             {:href (str (.getUrl ref))
                              :title (str (.getTitle ref))})
                        (map-children-to-hiccup this options))))
  AutoLink
  (to-hiccup [this options]
    [:a {:href (str (.getUrl this))} (str (.getUrl this))])
  MailLink
  (to-hiccup [this options]
    [:a {:href (str "mailto:" (.getText this))} (str (.getText this))])
  AttributesNode
  (to-hiccup [this options] nil)
  HtmlInline
  (to-hiccup [this options]
    (make-hiccup-node :hmtl (str (.getChars this)) (map-children-to-hiccup this options)))
  HtmlCommentBlock
  (to-hiccup [this options] nil))

(defn fold-inline-html [xf]
  (let [state (volatile! [])]
    (completing
     (fn [r input]
       (cond
         (contains-open-tag? input) (do
                                      (vswap! state conj (parse-tag (second input)))
                                      r)
         (contains-close-tag? input) (let [thing (peek @state)]
                                       (vswap! state pop)
                                       (if (empty? @state)
                                         (xf r thing)
                                         (do
                                           (vswap! state update (dec (count @state)) conj thing)
                                           r)))
         :else (if (empty? @state)
                 (xf r input)
                 (do
                   (vswap! state update (dec (count @state)) conj input)
                   r)))))))

(defn process-inline-html [almost-hiccup]
  (clojure.walk/postwalk (fn [item]
                           (if (hiccup? item)
                             (if (contains-inner-html? item)
                               (into [] fold-inline-html item)
                               item)
                             item))
                         almost-hiccup))

(defn cleanup-whitespace [hiccup]
  (clojure.walk/postwalk (fn [item]
                           (cond
                             (string? item) (when (not (str/blank? item))
                                              (str/trim item))
                             (vector? item) (filterv identity item)
                             :else item))
                         hiccup))

(defn md-to-hiccup [md options]
  (let [document (.parse parser md)]
    (cond->> (to-hiccup document options)
      (:inline-html options) process-inline-html
      (:remove-whitespace options) cleanup-whitespace)))

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
  [md-str & {:as options}]
  (let [lines (str/split-lines md-str)
        md-lines (if (contains? meta-seps (first lines))
                   (->> (drop-while #(not= (first lines) %) (rest lines))
                        (drop 1))
                   lines)]
    (-> (str/join "\n" md-lines)
        (md-to-hiccup (merge options {:code-format highlight-js-code-block-no-jupyter
                                      :inline-html true
                                      :remove-whitespace true})))))
