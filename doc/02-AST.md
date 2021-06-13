# Cybermonday IR AST and Transformation

To fully capture the information available from the parse result from Flexmark
(or remark),
the first pass tree-transformation from Flexmark (or remark) is into an intermediate
representation (IR). Some nodes (say like `BulletListItem` and
`OrderedListItem`) would normally render to the same HTML tag (`:li`), which would
prevent the user from disambiguating between them and prevent special case
post-processing. Some nodes, given below, are unambiguous, so their
corresponding HTML tag is fed through, and as such, these IR AST nodes are
completely valid HTML hiccup. However, we want to be able to still provide the
ability to transform these as need be.

So, the AST nodes are broken into two categories:

## HTML nodes

These are the standard, unambiguous tags. Paragraph (`:p`), TableRow (`:tr`),
etc. These are standard HTML hiccup and are not namespaced keywords.

## IR Nodes

The rest of the AST is formed from namespaced keywords and a corresponding data
map and body. Cybermonday will provide default processing, but the corresponding
data layouts for each node are enumerated below to allow for custom processing
into final html hiccup.

Every IR node will have the form

```clojure
[:markdown/keyword attr-map & body]
```

Please note that some of these are Flexmark specific and not available in remark
(CLJS).

### Bullet List Item

```clojure
[:markdown/bullet-list-item {} "Item"]
```

### Ordered List Item

```clojure
[:markdown/ordered-list-item {} "Item"]
```

### Hard Line Break

```clojure
[:markdown/hard-line-break {}]
```

### Soft Line Break

```clojure
[:markdown/soft-line-break {}]
```

### Heading

`:id` (potentially `nil`) is a string of the heading id.
`:level` will take integer values 1-6.

```clojure
[:markdown/heading {:level 1 :id "my-heading"} "Heading"]
```

### Fenced Code Block

`:language` (potentially `nil`) is a string of the tagged language.

```clojure
[:markdown/fenced-code-block {:language "julia"} "code"]
```

### Indented Code Block

```clojure
[:markdown/indented-code-block {} "code"]
```

### Link

`:href` specifies the link url
`:title` (potentially `nil`) contains the optional link title

```clojure
[:a {:href "https://example.com" :title "A neat website"} "Click me"]
```

### Reference

`:title` (potentially `nil`) contains the optional link title
`:label` contains the reference label
`:url` contains the reference url

```clojure
[:markdown/reference {:title "Title", :label "1", :url "/url"}]
```

### Link Reference

`:reference` (potentially `nil`) contains the `:markdown/reference` AST node of the referenced
link.

```clojure
[:markdown/link-ref
 {:reference
  [:markdown/reference
   {:title "Foo"
    :label "1"
    :url "#baz"}]}
 "Bar"]
```

### Image Reference

`:reference` (potentially `nil`) contains the `:markdown/reference` AST node of the referenced
link. Same as link reference.

```clojure
[:markdown/image-ref
 {:reference
  [:markdown/reference
   {:title "Foo"
    :label "1"
    :url "#baz"}]}
 "Bar"]
```

### Image

`:src` contains the image url
`:alt` contains the image alt text
`:title` (potentially `nil`) contains the optional link title

```clojure
[:img {:src "cat.png" :alt "Witty alt-text" :title "More info"}]
```

### HTML Comments

```clojure
[:markdown/html-comment {} "I'm a comment!"]
```

## Non Commonmark Extensions

### Github Flavored Markdown

#### Task Lists

GitHub-style lists of check boxes
`:checked?` indicates the checked state
`:ordered?` indicates whether the list item is in an ordered context

```clojure
[:markdown/task-list-item
  {:checked? false, :ordered? false}
  [:p {} "Unchecked and unordered"]]
```

#### Auto Link

`:href` contains the link url

```clojure
[:markdown/autolink {:href "www.foo.bar"}]
```

#### Mail Link

`:address` contains the email address

```clojure
[:markdown/mail-link {:address "you@example.com"}]
```

#### Table Separator

Currently Java only

```clojure
[:markdown/table-separator]
```

#### Table Cell

`:header?` is a boolean indicating whether this cell is a header.
`:alignment` (potentially `nil`) will indicate cell alignment ("left", "right", or "center")

```clojure
[:markdown/table-cell {:header? true :alignment "center"}]
```

### GitLab Flavored Markdown

#### Inline Math

From the GitLab extension. Inline math is delimited with `$\`y=mx+b\`$`. As one might want to choose the rendering backend, unlike GitLab - the default translation to HTML will be as is as text in a `:pre` block.

There is no syntax for block math, so the "recommendation" is to use a fenced
code block with an appropriate language.

```clojure
[:markdown/inline-math {} "y=mx+b"]
```

### Generic Markdown Extensions

#### Footnotes

Enables footnotes in the common format. Details [here](https://github.com/vsch/flexmark-java/wiki/Footnotes-Extension)

#### Footnote

`:id` contains the footnote id

```clojure
[:markdown/footnote {:id "1"}]
```

#### Footnote Block

`:id` contains the footnote id
`:content` contains the footnote content

```clojure
[:markdown/footnote-block {:id "1" :content "I'm a footnote"}]
```

# Lowering Extension Examples

Here are a few examples of overrides for the default lowering functions to
extend functionality

## KaTeX math rendering

Using the Auto-render extension for KaTeX, replacing inline math with the proper
delimiters enables math rendering

```clojure
(ns my-library
  (:require
   [cybermonday.core :as cn]))

(defn parse-math [[_ _ & [math]]]
  (str "$$" math "$$"))

(cm/parse-md "$`y=mx+b`$" {:markdown/inline-math parse-math})
```

## HTML comment-based content divider

In some blog style exporters, an HTML comment of `<!--more-->` signifies where
to cut off a short blurb versus the entire article. Because HTML comments are
part of the parse tree, we can capture this specific result.

```clojure
(ns my-library
  (:require
   [clojure.string :as str]
   [cybermonday.core :as cm]))

(defn content-split [[_ _ body]]
  (when (str/includes? body "more")
    [:div {:class "content-split"}]))

(cm/parse-md "<!--more-->" {:markdown/html-comment content-split})
```

## Anchor Links

A common feature in many markdown renderers is to place an anchor link on each
heading. Cybermonday generates ids for each header, so anchor links can be made
by customizing the `:markdown/heading` lowering.

```clojure
(ns my-library
  (:require
   [clojure.string :as str]
   [cybermonday.utils :refer [gen-id make-hiccup-node]]
   [cybermonday.core :as cm]))

(defn lower-heading [[_ attrs & body :as node]]
  (make-hiccup-node
   (keyword (str "h" (:level attrs)))
   (dissoc
    (let [id (if (nil? (:id attrs))
               (gen-id node)
               (:id attrs))]
      (assoc attrs
             :id id
             :class "anchor"
             :href (str "#" id)))
    :level)
   body))

(parse-md "# A Heading" {:markdown/heading lower-heading})
```
