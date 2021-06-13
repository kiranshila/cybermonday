# Templates

Cybermonday has very basic support for templates with
[mustache](https://mustache.github.io/) syntax. This is basic in the sense that
it only works for mustache tags, such as `{{foo}}` transforms to a
`:markdown/mustache` IR node as `[:markdown/mustache {} "foo"]`. This feature is
disabled by default but can be enabled by setting `:parse-templates?` to `true`
in the `parse-md` opts map.

You could then easily implement templating with a lowering fn such as

```clojure
(def replacements {:foo "bar"})

(defn lower-mustache [[_ _ body]]
  ((keyword body) replacements))
```

However the default behavior, even when enabled is to leave the text as is.

This feature should be considered very alpha and prone to change as we perhaps
might want to pull in an actual implementation of mustache templating.
