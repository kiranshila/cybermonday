(ns cybermonday.parser
  (:require
   [cybermonday.utils :refer [make-hiccup-node html-comment-re]]
   [hickory.core :as h]
   [clojure.string :as str])
  (:import
   (com.vladsch.flexmark.util.ast Node Document)
   (com.vladsch.flexmark.parser Parser)
   (com.vladsch.flexmark.util.data MutableDataSet)
   (com.vladsch.flexmark.ast
    BlockQuote FencedCodeBlock Heading HtmlBlockBase IndentedCodeBlock
    BulletList OrderedList BulletListItem OrderedListItem Paragraph ThematicBreak
    Code Emphasis StrongEmphasis HardLineBreak HtmlEntity Text
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
   TableSeparator ::table-separator
   Code :code
   TableBlock :table
   TableHead :thead
   TableBody :tbody
   TableRow :tr
   BlockQuote :blockquote
   DefinitionList ::definition-list
   DefinitionTerm ::definition-term
   DefinitionItem ::definition-item})

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
  FencedCodeBlock
  (to-hiccup [this]
    (make-hiccup-node ::fenced-code-block
                      {:language (str (.getInfo this))}
                      (map-children-to-hiccup this)))
  IndentedCodeBlock
  (to-hiccup [this]
    [::indented-code-block {} (str (.getContentChars this))])
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
    (let [[_ comment] (re-matches html-comment-re (str (.getChars this)))]
      [::html-comment {} comment]))
  GitLabInlineMath
  (to-hiccup [this]
    [::inline-math {} (str (.getText this))])
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
