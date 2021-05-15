# Cybermonday IR AST and Transformation

To fully capture the information available from the parse result from Flexmark,
the first pass tree-transformation from Flexmark is into an intermediate
representation (IR). Some nodes (say like `BulletListItem` and
`OrderedListItem`) would normally render to the same HTML tag (`:li`), which would
prevent the user from disambiguating between them and prevent special case
post-processing. Some nodes, given below, are unambiguous, so their
corresponding HTML tag is fed through, and as such, these IR AST nodes are
completely valid hiccup. However, we want to be able to still provide the
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
[:cybermonday.ir/keyword attr-map & body]
```

### Bullet List Item

```clojure
[:cybermonday.ir/bullet-list-item {} "Item"]
```

### Ordered List Item

```clojure
[:cybermonday.ir/ordered-list-item {} "Item"]
```

### Hard Line Break

```clojure
[:cybermonday.ir/hard-line-break {}]
```

### Soft Line Break

```clojure
[:cybermonday.ir/soft-line-break {}]
```

### Heading

`:id` (potentially `nil`) is a string of the heading id.
`:level` will take integer values 1-6.

```clojure
[:cybermonday.ir/heading {:level 1 :id "my-heading"} "Heading"]
```

### Fenced Code Block

`:language` (potentially `nil`) is a string of the tagged language.

```clojure
[:cybermonday.ir/fenced-code-block {:language "julia"} "code"]
```

### Indented Code Block

```clojure
[:cybermonday.ir/indented-code-block {} "code"]
```

### Table Separator

```clojure
[:cybermonday.ir/table-separator]
```

### Table Cell

`:header?` is a boolean indicating whether this cell is a header.
`:alignment` (potentially `nil`) will indicate cell alignment ("left", "right", or "center")

```clojure
[:cybermonday.ir/table-cell {:header? true :alignment "center"}]
```

### Link

`:href` specifies the link url
`:title` (potentially `nil`) contains the optional link title

```clojure
[:cybermonday.ir/link {:href "https://example.com" :title "A neat website"} "Click me"]
```

### Reference

`:title` (potentially `nil`) contains the optional link title
`:label` contains the reference label
`:href` contains the reference url

```clojure
[:cybermonday.ir/reference {:title "Title", :label "1", :href "/url"}]
```

### Link Reference

`:reference` (potentially `nil`) contains the `:cybermonday.ir/reference` AST node of the referenced
link.

```clojure
[:cybermonday.ir/link-ref
 {:reference
  [:cybermonday.ir/reference
   {:title "Foo"
    :label "1"
    :href "#baz"}]}
 "Bar"]
```

### Image

`:src` contains the image url
`:alt` contains the image alt text
`:title` (potentially `nil`) contains the optional link title

```clojure
[:cybermonday.ir/image {:src "cat.png" :alt "Witty alt-text" :title "More info"}]
```

### Auto Link

`:href` contains the link url

```clojure
[:cybermonday.ir/autolink {:href "www.foo.bar"}]
```

### Mail Link

`:address` contains the email address

```clojure
[:cybermonday.ir/mail-link {:address "you@example.com"}]
```

### HTML Comments

```clojure
[:cybermonday.ir/html-comment {} "<!--I'm a comment!-->"]
```

## Non Commonmark Extensions

### Inline Math

From the GitLab extension. Inline math is delimited with `$\`y=mx+b\`$`. As one
might want to choose the rendering backend, unlike GitLab - the default
translation to HTML will be as is as text.

```clojure
[:cybermonday.ir/inline-math {} "y=mx+b"]
```

### Attributes

Following the details from the [flexmark
spec](https://github.com/vsch/flexmark-java/wiki/Attributes-Extension). Cybermonday differs from the default
configuration as `ASSIGN_TEXT_ATTRIBUTES` is set to false. This implies that
inline attributes will always configure the parent element. This is due to the
fact that the way the Flexmark AST is built for the extended behavior, merging
attributes into leaves at the same level makes the transformation much more
difficult. If a need for this feature comes up, it might be worth looking into,
but the primary use case of heading `:id`s is unaffected.

```clojure
[:cybermonday.ir/attributes {:key "value"}]
```

### Definitions

From the [PHP Markdown Extra Definition
List](https://michelf.ca/projects/php-markdown/extra/#def-list).

#### Definition List, Term, and Items

The definition list contains the term and item children nodes. The term and
item bodies can of course contain additional hiccup.

```clojure
 [:cybermonday.ir/definition-list {}
  [:cybermonday.ir/definition-term {} "Foo"]
  [:cybermonday.ir/definition-item {} [:p {} "Bar"]]]
```

### Footnotes

Enables footnotes in the common format. Details [here](https://github.com/vsch/flexmark-java/wiki/Footnotes-Extension)

#### Footnote

`:id` contains the footnote id

```clojure
[:cybermonday.ir/footnote {:id "1"}]
```

#### Footnote Block

`:id` contains the footnote id
`:content` contains the footnote content

```clojure
[:cybermonday.ir/footnote-block {:id "1" :content "I'm a footnote"}]
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
   [cybermonday.ir :as ir]
   [cybermonday.lowering :refer [lower]]))

(defmethod lower ::ir/inline-math [[_ attrs & [math]]]
  (str "$$" math "$$"))
```

## HTML comment-based content divider

In some blog style exporters, an HTML comment of `<!--more-->` signifies where
to cut off a short blurb versus the entire article. Because HTML comments are
part of the parse tree, we can capture this specific result.

```clojure
(ns my-library
  (:require
   [clojure.string :as str]
   [cybermonday.ir :as ir]
   [cybermonday.lowering :refer [lower]]))

(defmethod lower ::ir/html-comment [[_ body]]
  (when (str/includes? body "<!--more-->")
    [:div {:class "content-split"}]))
```

## Anchor Links

A common feature in many markdown renderers is to place an anchor link on each
heading. Cybermonday generates ids for each header, so anchor links can be made
by customizing the `::ir/header` lowering.

```clojure
(ns my-library
  (:require
   [clojure.string :as str]
   [cybermonday.ir :as ir]
   [cybermonday.utils :refer [gen-id make-hiccup-node]]
   [cybermonday.lowering :refer [lower]]))

(defmethod lower ::ir/heading [[_ attrs & body :as node]]
  (make-hiccup-node
   (keyword (str "h" (:level attrs)))
   (dissoc
    (assoc attrs
           :id (if (nil? (:id attrs))
                 (gen-id node)
                 (:id attrs))
           :class "anchor"
           :href (str "#" id))
    :level)
   body))
```
