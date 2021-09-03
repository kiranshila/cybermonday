# Cybermonday Test Document

This document will hopefully cover all the different AST node types that are
available in commonmark + the enabled features in Cybermonday.

## Easy stuff

Here are two different kinds of lists:

An ordered list

1. Foo
2. Bar
3. Baz

And unordered list

- Test1
- Test2
- Test3

We can nest the lists

1. First item
2. Second item
3. Third item
   1. Indented item
   2. Indented item
4. Fourth item

- First item
- Second item
- Third item
  - Indented item
  - Indented item
- Fourth item

1. First item
2. Second item
3. Third item
   - Indented item
   - Indented item
4. Fourth item

All heading levels

# Heading level 1

## Heading level 2

### Heading level 3

#### Heading level 4

##### Heading level 5

###### Heading level 6

####### This shouldn't be a heading

Alternative heading syntax
Heading level 1
===============

## Heading level 2

This should be one paragraph.
Even though they are on different lines

This however is a new paragraph.

We can create an hr with

---

Here is text with **Bold**, **bold**, _italic_, _italic_, **_bold_italic_**.

> This is a quote

> This is a quote with _markdown_ in it.

> This is a quote with _markdown_ and <p class=my-class>HTML</p> in it

> This is a quote
>
> > With another quote in it

![An Image!](image_url.png)

Here is some code

```julia
using LinearAlgebra
```

Also some `inline code stuff`.

# Links

My favorite search engine is [Duck Duck Go](https://duckduckgo.com).

My favorite search engine is [Duck Duck Go](https://duckduckgo.com "The best search engine for privacy").

<https://www.markdownguide.org>
<fake@example.com>

You can markdown links too: I love supporting the **[EFF](https://eff.org)**.
[hobbit-hole][1]

Here is an image link:
[![An old rock in the desert](/assets/images/shiprock.jpg "Shiprock, New Mexico by Beau Rogers")](https://www.flickr.com/photos/beaurogers/31833779864/in/photolist-Qv3rFw-34mt9F-a9Cmfy-5Ha3Zi-9msKdv-o3hgjr-hWpUte-4WMsJ1-KUQ8N-deshUb-vssBD-6CQci6-8AFCiD-zsJWT-nNfsgB-dPDwZJ-bn9JGn-5HtSXY-6CUhAL-a4UTXB-ugPum-KUPSo-fBLNm-6CUmpy-4WMsc9-8a7D3T-83KJev-6CQ2bK-nNusHJ-a78rQH-nw3NvT-7aq2qf-8wwBso-3nNceh-ugSKP-4mh4kh-bbeeqH-a7biME-q3PtTf-brFpgb-cg38zw-bXMZc-nJPELD-f58Lmo-bXMYG-bz8AAi-bxNtNT-bXMYi-bXMY6-bXMYv)

We should be able to escape stuff like: You use backticks (\`code\`) to write
inline code.

# Non-commonmark stuff

<!--HTML comments are tracked nodes as well-->

Checkboxes from github

- [x] #739
- [ ] https://github.com/octo-org/octo-repo/issues/740
- [ ] Add delight to the experience when all tasks are complete :tada:

Tables

| First Header | Second Header |
| ------------ | ------------- |
| Content Cell | Content Cell  |
| Content Cell | Content Cell  |

We can do inline math with $`y=mx+b`$

We can do block math with just a code block

```math
E=mc^2
```

Finally, we can do footnotes

Paragraph with a footnote reference[^1]

[^1]: Footnote text added at the bottom of the document

[1]: https://en.wikipedia.org/wiki/Hobbit#Lifestyle
