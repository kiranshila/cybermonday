# cybermonday

FIXME: my new application.

## Installation

Download from https://github.com/kiranshila/cybermonday.

## Usage

FIXME: explanation

Run the project directly:

    $ clojure -m kiranshila.cybermonday

Run the project's tests (they'll fail until you edit them):

    $ clojure -A:test:runner

Build an uberjar:

    $ clojure -A:uberjar

Run that uberjar:

    $ java -jar cybermonday.jar

## Major Caveats

The inline html parser is really rudimentary. Weird edge cases most certainly will not work. They pretty much must follow the `<tag foo="bar"> Content </tag>` syntax to be properly rendered. However, the inline hmtl parsing happens after the markdown parsing, so having markdown within the inline html is totally valid.

Another breaking example is if the attributes of a tag contain `=` in the value of the attribute. I'm splitting the attributes up by the `=`, so any random `=` that doesn't separate the key and the value will break the parser.

## Shortcodes

I wanted the support of a couple hugo shortcodes, so I added a shortcode processor as well. There are caveats with this though: Mainly, markdown inside the shortcodes just won't work.

### Bugs

...

### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2020 Kiranshila

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
