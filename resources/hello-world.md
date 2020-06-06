---
title: "Hello World"
author: ["Kiran Shila"]
date: 2020-05-22
lastmod: 2020-06-04T22:18:17-04:00
tags: ["clojure", "org"]
draft: false
featured_image: "ox-hugo/hello-world-header.svg"
---

## This is a blog post in org-mode {#this-is-a-blog-post-in-org-mode}

This should render as normal content
I mean, check this shit out


### More shit {#more-shit}


#### Even more shit {#even-more-shit}


## Very cool, though, right? {#very-cool-though-right}

****What does this do****
_italic_
<span class="underline">underlined</span>
`verbatim`
~~strike\_through~~

Here is a list

1.  One


### Two {#two}

1.  Three

<!--more-->


## Some more stuff {#some-more-stuff}

And here is some code.

```jupyter-julia
using Plots
x = 1:0.1:10
y = sin.(x)
plot(x,y)
```

{{< figure src="/ox-hugo/hello-world-header.svg" >}}

```jupyter-julia
plot(rand(100),tanh.(rand(100)))
```

{{< figure src="/ox-hugo/test.svg" >}}

And some code in another language

```clojure
(defn [x]
  (str "Hello" x))
```

`not this` `some code`


```jupyter-python
[[1,2,3],[4,5,6],[7,8,0]]
```

| 1 | 2 | 3 |
|---|---|---|
| 4 | 5 | 6 |
| 7 | 8 | 0 |
