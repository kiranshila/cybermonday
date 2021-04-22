# cybermonday
> A markdown (extended CommonMark) to Clojure data (hiccup) parser.

[![Clojars Project](https://img.shields.io/clojars/v/com.github.kiranshila/cybermonday.svg)](https://clojars.org/com.github.kiranshila/cybermonday)
[![cljdoc badge](https://cljdoc.org/badge/com.github.kiranshila/cybermonday)](https://cljdoc.org/d/com.github.kiranshila/cybermonday/CURRENT)


I've been frustrated with the space of Markdown manipulation in Clojure. Most
libraries provide parsing to raw html, which is fine if you have a
straightforward way to include that in whatever you are targeting. If however,
you would want to manipulate the AST of the markdown directly, or convert it
into a format that frontend frameworks (like Reagent) can consume, you would
have to convert the HTML to clojure data (like Hiccup). There are a few html to
hiccup parsers, but as anyone who has tried to parse html will tell you, there
are edge cases that can break the whole thing. It also seems backwards to go
from markdown to html to hiccup to html when you consider the entire rendering
pipeline.

To overcome this, I wrote `cybermonday`! It was originally going to be a parser
for blackfriday markdown, but as I realized the markdown spec is insane, it made
more sense to wrap the excellent Flexmark java-based markdown parser. At the
most basic level, `cybermonday` provides a top level function `parse-md`
that gives you a nice, reagent-renderable representation of your source. This
includes all of the CommonMark spec as well as the best features of popular extensions such as tables,
strikethroughs, footnotes, generic attributes, definitions, math, and more! This
even supports inline html and html around markdown-formated text.

However, `cybermonday` also provides access to a hiccup representation of the
Flexmark AST and the multimethod to provide the final pass transformation from
Flexmark to HTML. This allows the user to customize how the raw markdown AST
gets transformed into html, allowing for easy extension and customization.

I'm using this library on my blog at kiranshila.com - please let me know if you
run into any issues.

For more details, check out the docs!

## Major Caveats

The inline html parser is really rudimentary. Please be gentle. They pretty much must follow the `<tag foo="bar"> Content </tag>` syntax to be properly rendered.

Another breaking example is if the attributes of a tag contain `=` in the value of the attribute. I'm splitting the attributes up by the `=`, so any random `=` that doesn't separate the key and the value will break the parser.

## License

Copyright © 2021 Kiran Shila

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
