# Usage

A single top level function for parsing markdown is provided as `parse-md`. This
function yields a map with `:frontmatter` and `:body` where the frontmatter is
the (possibly nil) parsed YAML metadata from the file and the body is the HTML
hiccup.

To customize the lowering, see the AST transformations for IR node format. The
lowering step is done through a map from IR node type to lowering fn as
`:lower-fns` in the options map to `parse-md`.

Additionally, the options map accepts `:default-attrs` as a map from HTML tag
keyword to default attributes so you can set classes, styles, etc. for all nodes
of a given tag.

```clojure
(parse-md my-md-string {:lower-fns {:markdown/node lower-node-fn}
                        :default-attrs {:p {:style {:color :red}}}})
```

## ClojureScript

There is currently rudimentary support for ClojureScript using excellent
[remark](https://github.com/remarkjs/remark) JS library. As of this release, the
published build only works with
[shadow-cljs](https://github.com/thheller/shadow-cljs). I'm going to focus
trying to make it all work with other tools as well using CLJSJS, but I've
always found that to be difficult, and you're better off using shadow anyway.
