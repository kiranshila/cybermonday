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
    SoftLineBreak BlockQuote Link LinkRef ImageRef Reference AutoLink MailLink Image HtmlInline
    HtmlCommentBlock HtmlEntity)
   (com.vladsch.flexmark.ext.tables
    TablesExtension TableBlock TableHead TableRow TableCell TableBody TableBody TableSeparator)
   (com.vladsch.flexmark.ext.footnotes FootnoteExtension Footnote FootnoteBlock)
   (com.vladsch.flexmark.ext.gfm.strikethrough StrikethroughExtension Strikethrough)
   (com.vladsch.flexmark.ext.gfm.tasklist TaskListExtension TaskListItem)
   (com.vladsch.flexmark.test.util AstCollectingVisitor)
   (com.vladsch.flexmark.ext.gitlab GitLabExtension GitLabInlineMath)
   (com.vladsch.flexmark.ext.toc TocExtension TocBlock)))

(def options
  "The default options for the Flexmark parser
  There shouldn't be a reason to change this"
  (.. (MutableDataSet.)
      (set Parser/EXTENSIONS
           [(TocExtension/create)
            (TablesExtension/create)
            (FootnoteExtension/create)
            (StrikethroughExtension/create)
            (TaskListExtension/create)
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
   HardLineBreak :br
   SoftLineBreak :markdown/soft-line-break
   Document :div
   StrongEmphasis :strong
   Strikethrough :del
   OrderedList :ol
   BulletList :ul
   BulletListItem :markdown/bullet-list-item
   OrderedListItem :markdown/ordered-list-item
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
  (to-hiccup [this _]))

(defn map-children-to-hiccup [node source]
  (map #(to-hiccup % source) (.getChildren node)))

(extend-protocol HiccupRepresentable
  Node
  (to-hiccup [this source]
    (make-hiccup-node (node-to-tag this)
                      (map-children-to-hiccup this source)))
  Text
  (to-hiccup [this _]
    (str (.getChars this)))
  Heading
  (to-hiccup [this source]
    (make-hiccup-node :markdown/heading
                      {:level (.getLevel this)}
                      (map-children-to-hiccup this source)))
  FencedCodeBlock
  (to-hiccup [this source]
    [:markdown/fenced-code-block
     {:language (let [lang (str (.getInfo this))]
                  (when (seq lang)
                    lang))}
     (str/trimr (str (.getContentChars this)))])
  IndentedCodeBlock
  (to-hiccup [this _]
    [:markdown/indented-code-block {} (str/trimr (str (.getContentChars this)))])
  TableCell
  (to-hiccup [this source]
    (make-hiccup-node :markdown/table-cell
                      {:header? (.isHeader this)
                       :alignment (not-empty (str/lower-case (str (.getAlignment this))))}
                      (map-children-to-hiccup this source)))
  HtmlBlockBase
  (to-hiccup [this _]
    (h/as-hiccup (first (h/parse-fragment (str (.getContentChars this))))))
  Link
  (to-hiccup [this source]
    (make-hiccup-node :a
                      {:href (str (.getUrl this))
                       :title (not-empty (str (.getTitle this)))}
                      (map-children-to-hiccup this source)))
  Reference
  (to-hiccup [this _]
    [:markdown/reference {:title (not-empty (str (.getTitle this)))
                          :label (str (.getReference this))
                          :url (str (.getUrl this))}])
  LinkRef
  (to-hiccup [this source]
    (make-hiccup-node :markdown/link-ref
                      {:reference (-> (.getDocument this)
                                      (.get Parser/REFERENCES)
                                      (get (str (.getReference this)))
                                      (#(to-hiccup % source)))}
                      (map-children-to-hiccup this source)))

  ImageRef
  (to-hiccup [this source]
    (make-hiccup-node :markdown/image-ref
                      {:reference (-> (.getDocument this)
                                      (.get Parser/REFERENCES)
                                      (get (str (.getReference this)))
                                      (#(to-hiccup % source)))}
                      (map-children-to-hiccup this source)))
  Image
  (to-hiccup [this _]
    [:img {:src (str (.getUrl this))
           :alt (str (.getText this))
           :title (not-empty (str (.getTitle this)))}])
  AutoLink
  (to-hiccup [this _]
    [:markdown/autolink {:href (str (.getUrl this))}])
  MailLink
  (to-hiccup [this _]
    [:markdown/mail-link {:address (str (.getText this))}])
  HtmlCommentBlock
  (to-hiccup [this _]
    (let [[_ comment] (re-matches html-comment-re (str (.getChars this)))]
      [:markdown/html-comment {} comment]))
  GitLabInlineMath
  (to-hiccup [this _]
    [:markdown/inline-math {} (str (.getText this))])
  HtmlEntity
  (to-hiccup [this _]
    (->> (.getChars this)
         str
         h/parse-fragment
         first
         str))
  HtmlInline
  (to-hiccup [this _]
    [:markdown/html {} (str (.getChars this))])
  Footnote
  (to-hiccup [this _]
    [:markdown/footnote {:id (str (.getText this))}])
  FootnoteBlock
  (to-hiccup [this _]
    [:markdown/footnote-block {:id (str (.getText this))
                               :content (str (.getFootnote this))}])
  TaskListItem
  (to-hiccup [this source]
    (make-hiccup-node
     :markdown/task-list-item
     {:checked? (.isItemDoneMarker this)
      :ordered? (.isOrderedItem this)}
     (map-children-to-hiccup this source)))
  TableSeparator
  (to-hiccup [this _]
    nil)
  TocBlock
  (to-hiccup [this _]
    [:markdown/table-of-contents {:style (str (.getStyle this))}])
  nil
  (to-hiccup [this _]
    nil))
