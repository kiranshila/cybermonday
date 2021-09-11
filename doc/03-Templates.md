# Templates

Cybermonday pairs well with templating, but context is very important.
Cybermonday itself doesn't provide any templating abilities, there are many
excellent libraries out there for that such as
[pogonos](https://github.com/athos/pogonos) for mustache.

For example, mustache could be used in two ways:

1. You could template the markdown itself, by passing the entire document
   through pogonos
2. You could walk the resulting AST, transforming strings where appropriate.

The former has the use case of systematically creating markdown documents while
the latter is more of templating small parts of the document.

In the latter, you might want to blacklist some tags and only process text in
certain places, this can easily be accomplished with:

```clojure
(defn process-template [mdast template-vals]
  (clojure.walk/prewalk
   (fn [item]
     (if (cybermonday.utils/hiccup? item)
       (let [[tag attrs & body] item]
         (if (not= tag :code)
           (cybermonday.utils/make-hiccup-node
            tag
            attrs
            (map #(if (string? %)
                    (pogonos.core/render-string % template-vals)
                    %)
                 body))
           item))
       item))
   mdast))
```
