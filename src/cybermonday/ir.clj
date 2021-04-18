(ns cybermonday.ir
  (:require
   [cybermonday.utils :refer [make-hiccup-node hiccup?]]
   [hickory.core :as h]
   [clojure.string :as str]
   [clojure.walk :as walk])
  (:import
   (com.vladsch.flexmark.util.ast Node Document)
   (com.vladsch.flexmark.parser Parser)
   (com.vladsch.flexmark.util.data MutableDataSet)
   (com.vladsch.flexmark.ast
    BlockQuote FencedCodeBlock Heading HtmlBlockBase IndentedCodeBlock
    BulletList OrderedList BulletListItem OrderedListItem Paragraph ThematicBreak
    Code Emphasis StrongEmphasis HardLineBreak HtmlEntity Text ListItem
    SoftLineBreak BlockQuote Link LinkRef Reference AutoLink MailLink Image HtmlInline
    HtmlCommentBlock HtmlEntity)
   (com.vladsch.flexmark.ext.tables
    TablesExtension TableBlock TableHead TableRow TableCell TableBody TableBody TableSeparator)
   (com.vladsch.flexmark.ext.attributes AttributesExtension AttributesNode)
   (com.vladsch.flexmark.ext.definition DefinitionExtension DefinitionList DefinitionTerm DefinitionItem)
   (com.vladsch.flexmark.ext.footnotes FootnoteExtension Footnote FootnoteBlock)
   (com.vladsch.flexmark.ext.gfm.strikethrough StrikethroughExtension Strikethrough)
   (com.vladsch.flexmark.test.util AstCollectingVisitor)
   (com.vladsch.flexmark.ext.gitlab GitLabExtension GitLabInlineMath)))

(def options
  "The default options for the Flexmark parser
  There shouldn't be a reason to change this"
  (.. (MutableDataSet.)
      (set AttributesExtension/ASSIGN_TEXT_ATTRIBUTES false)
      (set Parser/EXTENSIONS
           [(TablesExtension/create)
            (FootnoteExtension/create)
            (DefinitionExtension/create)
            (AttributesExtension/create)
            (StrikethroughExtension/create)
            (GitLabExtension/create)])
      (toImmutable)))

(def parser
  "The instance of the Flexmark parser.
  Can be called like `(.parse parser document-string)` to yeild a `document` Flexmark parse object"
  (.build (Parser/builder options)))

(defn print-ast
  "Utility function to print the AST. Consumes the `document` from the parser"
  [document]
  (println (.. (AstCollectingVisitor.)
               (collectAndGetAstText document))))

(def node-tags
  "The default mapping from Flexmark AST node to Hiccup tag"
  {Paragraph :p
   Emphasis :em
   ThematicBreak :hr
   HardLineBreak ::hard-line-break
   SoftLineBreak ::soft-line-break
   Document :div
   StrongEmphasis :strong
   Strikethrough :del
   OrderedList :ol
   BulletList :ul
   BulletListItem ::bullet-list-item
   OrderedListItem ::ordered-list-item
   Code :code
   TableBlock :table
   TableHead :thead
   TableBody :tbody
   TableRow :tr
   BlockQuote :blockquote})

(defn node-to-tag
  "Gets the default tag for this `node` or throws an error if we encounter a node we aren't handling."
  [node]
  (or (node-tags (type node))
      (throw (Exception. (str "Got unknown AST node: " node)))))

(defprotocol HiccupRepresentable
  "Provides the protocol for `(to-hiccup)` method. Dispatches  on AST node type"
  (to-hiccup [this]))

(defn map-children-to-hiccup [node]
  (map #(to-hiccup %) (.getChildren node)))

(extend-protocol HiccupRepresentable
  Node
  (to-hiccup [this]
    (make-hiccup-node (node-to-tag this)
                      (map-children-to-hiccup this)))
  Text
  (to-hiccup [this]
    (str (.getChars this)))
  Heading
  (to-hiccup [this]
    (make-hiccup-node ::heading
                      {:level (.getLevel this)
                       :id (not-empty (.getAnchorRefId this))}
                      (map-children-to-hiccup this)))
  ListItem
  (to-hiccup [this]
    (if (every? #(instance? Paragraph %) (.getChildren this))
      [(node-to-tag this) (str/trim (str/join (map #(.getChars %) (.getChildren this))))]
      (make-hiccup-node (node-to-tag this) (map-children-to-hiccup this))))
  FencedCodeBlock
  (to-hiccup [this]
    (make-hiccup-node ::fenced-code-block
                      {:language (str (.getInfo this))}
                      (map-children-to-hiccup this)))
  IndentedCodeBlock
  (to-hiccup [this]
    [::indented-code-block (str (.getContentChars this))])
  TableSeparator
  (to-hiccup [this] [::table-separator])
  TableCell
  (to-hiccup [this]
    (make-hiccup-node ::table-cell
                      {:header? (.isHeader this)
                       :alignment (not-empty (str/lower-case (str (.getAlignment this))))}
                      (map-children-to-hiccup this)))
  HtmlBlockBase
  (to-hiccup [this]
    (h/as-hiccup (first (h/parse-fragment (str (.getContentChars this))))))
  Link
  (to-hiccup [this]
    (make-hiccup-node ::link
                      {:href (str (.getUrl this))
                       :title (not-empty (str (.getTitle this)))}
                      (map-children-to-hiccup this)))
  Reference
  (to-hiccup [this]
    [::reference {:title (not-empty (str (.getTitle this)))
                  :label (str (.getReference this))
                  :href (str (.getUrl this))}])
  LinkRef
  (to-hiccup [this]
    (make-hiccup-node ::link-ref
                      {:reference (-> (.getDocument this)
                                      (.get Parser/REFERENCES)
                                      (get (str (.getReference this)))
                                      to-hiccup)}
                      (map-children-to-hiccup this)))
  Image
  (to-hiccup [this]
    [::image {:src (str (.getUrl this))
              :alt (str (.getText this))
              :title (not-empty (str (.getTitle this)))}])
  AutoLink
  (to-hiccup [this]
    [::autolink {:href (str (.getUrl this))}])
  MailLink
  (to-hiccup [this]
    [::mail-link {:address (str (.getText this))}])
  HtmlCommentBlock
  (to-hiccup [this]
    [::html-comment (str (.getChars this))])
  GitLabInlineMath
  (to-hiccup [this]
    [::inline-math (str (.getText this))])
  HtmlEntity
  (to-hiccup [this]
    (->> (.getChars this)
         str
         h/parse-fragment
         first
         str))
  HtmlInline
  (to-hiccup [this]
    [::html (str (.getChars this))])
  AttributesNode
  (to-hiccup [this]
    [::attributes (into {} (for [attribute (.getChildren this)
                                 :let [name (str (.getName attribute))
                                       value (str (.getValue attribute))]]
                             (cond
                               (.isId attribute) [:id value]
                               (.isClass attribute) [:class value]
                               :else [(keyword name) (if (not-empty value) value true)])))])
  DefinitionList
  (to-hiccup [this]
    (make-hiccup-node ::definition-list (map-children-to-hiccup this)))
  DefinitionTerm
  (to-hiccup [this]
    (make-hiccup-node ::definition-term (map-children-to-hiccup this)))
  DefinitionItem
  (to-hiccup [this]
    (make-hiccup-node ::definition-item (map-children-to-hiccup this)))
  Footnote
  (to-hiccup [this]
    [::footnote {:id (str (.getText this))}])
  FootnoteBlock
  (to-hiccup [this]
    [::footnote-block {:id (str (.getText this))
                       :content (str (.getFootnote this))}])
  nil
  (to-hiccup [this]
    nil))

;; HTML Processing

(defn close-tag? [tag]
  (when (string? tag)
    (and (= \/ (second tag))
         (seq (re-matches #"<(.*)>" tag)))))

(defn open-tag? [tag]
  (when (string? tag)
    (and (not= \/ (second tag))
         (seq (re-matches #"<(.*)>" tag)))))

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
  (let [[tag-name & attributes] (str/split (second (re-matches #"<(.*)>" tag)) #"\s+(?=\S+=)")]
    (if (open-tag? tag)
      (if (some? attributes)
        [(keyword tag-name) (apply merge (map html-attr-to-map attributes))]
        [(keyword tag-name)])
      [(keyword (apply str (rest tag-name)))])))

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

(defn process-inline-html
  "Parses the HtmlInline fragments into partial hiccup, and folds in the inner AST"
  [almost-hiccup]
  (walk/postwalk
   (fn [item]
     (if (hiccup? item)
       (if (contains-inner-html? item)
         (into [] fold-inline-html item)
         item)
       item))
   almost-hiccup))

;; Final IR postprocessing

(defn cleanup-whitespace
  "Removes excess whitespace from the resulting AST."
  [hiccup]
  (walk/postwalk
   (fn [item]
     (cond
       (string? item) (when (not (str/blank? item)) item)
       :else item))
   hiccup))

;; IR Generation

(defn md-to-ir
  "Given `md` as a string, generates a Cybermonday hiccup IR
  Inline HTML gets folded inplace and excess whitespace is removed"
  [md]
  (let [document (.parse parser md)]
    (->> (to-hiccup document)
         process-inline-html
         cleanup-whitespace)))
