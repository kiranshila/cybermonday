---
title: "Test"
author: ["Kiran Shila"]
date: 2020-05-16
lastmod: 2020-05-16T14:23:22-04:00
tags: ["hugo", "org"]
categories: ["emacs"]
draft: true
weight: 2001
foo: "bar"
baz: "zoo"
alpha: 1
beta: "two words"
gamma: 10
menu:
  main:
    identifier: "test"
    weight: 2001
---

## This is a heading {#this-is-a-heading}

This is some content

- This is a list
- Another item in the list

More content

### Subheading {#subheading}

Some subheading content
`monospaced_text`
<span class="underline">underline</span>

```julia
import LinearAlgebra
+(1,2,3)
∇(x) = derive(x)
```

## Tables {#tables}

|    x     |                                  y | z             |
| :------: | ---------------------------------: | :------------ |
|   123    |                                456 | 789           |
|  Hello   |                              World | A Longer Line |
|    A     | More Long Lines to check alignment | 12.123        |
| &lambda; |                       \\(\gamma\\) |               |

## Math {#math}

\begin{align\*}
x &= 1\\\\\\
y &= \lambda
\end{align\*}
