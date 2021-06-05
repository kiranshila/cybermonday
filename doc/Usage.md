# Usage

A single top level function for parsing markdown is provided as `parse-md`. This
function yields a map with `:frontmatter` and `:body` where the frontmatter is
the (possibly nil) parsed YAML metadata from the file and the body is the HTML
hiccup.

To customize the parsing, see the AST transformations. The lowering step is done
through a multimethod, for which the cybermonday IR nodes can be individually customized.
