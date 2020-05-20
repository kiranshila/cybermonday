(clojure.core/ns
 kiranshila.cybermonday-generated-test
 (:require
  [clojure.test :refer :all]
  [kiranshila.cybermonday :refer :all]
  [kiranshila.generate-test
   :refer
   [kiranshila.generate-test/test-hiccup]]))
(kiranshila.generate-test/test-hiccup
 "Example 1"
 "\tfoo\tbaz\t\tbim\n"
 [:pre [:code "foo→baz→→bim"]])
(kiranshila.generate-test/test-hiccup
 "Example 2"
 "  \tfoo\tbaz\t\tbim\n"
 [:pre [:code "foo→baz→→bim"]])
(kiranshila.generate-test/test-hiccup
 "Example 3"
 "    a\ta\n    ὐ\ta\n"
 [:pre [:code "a→a\nὐ→a"]])
(kiranshila.generate-test/test-hiccup
 "Example 4"
 "  - foo\n\n\tbar\n"
 [:ul [:li [:p "foo"] [:p "bar"]]])
(kiranshila.generate-test/test-hiccup
 "Example 5"
 "- foo\n\n\t\tbar\n"
 [:ul [:li [:p "foo"] [:pre [:code "bar"]]]])
(kiranshila.generate-test/test-hiccup
 "Example 6"
 ">\t\tfoo\n"
 [:blockquote [:pre [:code "foo"]]])
(kiranshila.generate-test/test-hiccup
 "Example 7"
 "-\t\tfoo\n"
 [:ul [:li [:pre [:code "foo"]]]])
(kiranshila.generate-test/test-hiccup
 "Example 8"
 "    foo\n\tbar\n"
 [:pre [:code "foo\nbar"]])
(kiranshila.generate-test/test-hiccup
 "Example 9"
 " - foo\n   - bar\n\t - baz\n"
 [:ul [:li "foo" [:ul [:li "bar" [:ul [:li "baz"]]]]]])
(kiranshila.generate-test/test-hiccup
 "Example 10"
 "#\tFoo\n"
 [:h1 "Foo"])
(kiranshila.generate-test/test-hiccup "Example 11" "*\t*\t*\t\n" [:hr])
(kiranshila.generate-test/test-hiccup
 "Example 12"
 "- `one\n- two`\n"
 [:ul [:li "`one"] [:li "two`"]])
(kiranshila.generate-test/test-hiccup
 "Example 13"
 "***\n---\n___\n"
 [:hr]
 [:hr]
 [:hr])
(kiranshila.generate-test/test-hiccup "Example 14" "+++\n" [:p "+++"])
(kiranshila.generate-test/test-hiccup "Example 15" "===\n" [:p "==="])
(kiranshila.generate-test/test-hiccup
 "Example 16"
 "--\n**\n__\n"
 [:p "--\n**\n__"])
(kiranshila.generate-test/test-hiccup
 "Example 17"
 " ***\n  ***\n   ***\n"
 [:hr]
 [:hr]
 [:hr])
(kiranshila.generate-test/test-hiccup
 "Example 18"
 "    ***\n"
 [:pre [:code "***"]])
(kiranshila.generate-test/test-hiccup
 "Example 19"
 "Foo\n    ***\n"
 [:p "Foo\n***"])
(kiranshila.generate-test/test-hiccup
 "Example 20"
 "_____________________________________\n"
 [:hr])
(kiranshila.generate-test/test-hiccup "Example 21" " - - -\n" [:hr])
(kiranshila.generate-test/test-hiccup
 "Example 22"
 " **  * ** * ** * **\n"
 [:hr])
(kiranshila.generate-test/test-hiccup
 "Example 23"
 "-     -      -      -\n"
 [:hr])
(kiranshila.generate-test/test-hiccup
 "Example 24"
 "- - - -    \n"
 [:hr])
(kiranshila.generate-test/test-hiccup
 "Example 25"
 "_ _ _ _ a\n\na------\n\n---a---\n"
 [:p "_ _ _ _ a"]
 [:p "a------"]
 [:p "---a---"])
(kiranshila.generate-test/test-hiccup
 "Example 26"
 " *-*\n"
 [:p [:em "-"]])
(kiranshila.generate-test/test-hiccup
 "Example 27"
 "- foo\n***\n- bar\n"
 [:ul [:li "foo"]]
 [:hr]
 [:ul [:li "bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 28"
 "Foo\n***\nbar\n"
 [:p "Foo"]
 [:hr]
 [:p "bar"])
(kiranshila.generate-test/test-hiccup
 "Example 29"
 "Foo\n---\nbar\n"
 [:h2 "Foo"]
 [:p "bar"])
(kiranshila.generate-test/test-hiccup
 "Example 30"
 "* Foo\n* * *\n* Bar\n"
 [:ul [:li "Foo"]]
 [:hr]
 [:ul [:li "Bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 31"
 "- Foo\n- * * *\n"
 [:ul [:li "Foo"] [:li [:hr]]])
(kiranshila.generate-test/test-hiccup
 "Example 32"
 "# foo\n## foo\n### foo\n#### foo\n##### foo\n###### foo\n"
 [:h1 "foo"]
 [:h2 "foo"]
 [:h3 "foo"]
 [:h4 "foo"]
 [:h5 "foo"]
 [:h6 "foo"])
(kiranshila.generate-test/test-hiccup
 "Example 33"
 "####### foo\n"
 [:p "####### foo"])
(kiranshila.generate-test/test-hiccup
 "Example 34"
 "#5 bolt\n\n#hashtag\n"
 [:p "#5 bolt"]
 [:p "#hashtag"])
(kiranshila.generate-test/test-hiccup
 "Example 35"
 "\\## foo\n"
 [:p "## foo"])
(kiranshila.generate-test/test-hiccup
 "Example 36"
 "# foo *bar* \\*baz\\*\n"
 [:h1 "foo" [:em "bar"] "*baz*"])
(kiranshila.generate-test/test-hiccup
 "Example 37"
 "#                  foo                     \n"
 [:h1 "foo"])
(kiranshila.generate-test/test-hiccup
 "Example 38"
 " ### foo\n  ## foo\n   # foo\n"
 [:h3 "foo"]
 [:h2 "foo"]
 [:h1 "foo"])
(kiranshila.generate-test/test-hiccup
 "Example 39"
 "    # foo\n"
 [:pre [:code "# foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 40"
 "foo\n    # bar\n"
 [:p "foo\n# bar"])
(kiranshila.generate-test/test-hiccup
 "Example 41"
 "## foo ##\n  ###   bar    ###\n"
 [:h2 "foo"]
 [:h3 "bar"])
(kiranshila.generate-test/test-hiccup
 "Example 42"
 "# foo ##################################\n##### foo ##\n"
 [:h1 "foo"]
 [:h5 "foo"])
(kiranshila.generate-test/test-hiccup
 "Example 43"
 "### foo ###     \n"
 [:h3 "foo"])
(kiranshila.generate-test/test-hiccup
 "Example 44"
 "### foo ### b\n"
 [:h3 "foo ### b"])
(kiranshila.generate-test/test-hiccup
 "Example 45"
 "# foo#\n"
 [:h1 "foo#"])
(kiranshila.generate-test/test-hiccup
 "Example 46"
 "### foo \\###\n## foo #\\##\n# foo \\#\n"
 [:h3 "foo ###"]
 [:h2 "foo ###"]
 [:h1 "foo #"])
(kiranshila.generate-test/test-hiccup
 "Example 47"
 "****\n## foo\n****\n"
 [:hr]
 [:h2 "foo"]
 [:hr])
(kiranshila.generate-test/test-hiccup
 "Example 48"
 "Foo bar\n# baz\nBar foo\n"
 [:p "Foo bar"]
 [:h1 "baz"]
 [:p "Bar foo"])
(kiranshila.generate-test/test-hiccup
 "Example 49"
 "## \n#\n### ###\n"
 [:h2]
 [:h1]
 [:h3])
(kiranshila.generate-test/test-hiccup
 "Example 50"
 "Foo *bar*\n=========\n\nFoo *bar*\n---------\n"
 [:h1 "Foo" [:em "bar"]]
 [:h2 "Foo" [:em "bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 51"
 "Foo *bar\nbaz*\n====\n"
 [:h1 "Foo" [:em "bar\nbaz"]])
(kiranshila.generate-test/test-hiccup
 "Example 52"
 "  Foo *bar\nbaz*\t\n====\n"
 [:h1 "Foo" [:em "bar\nbaz"]])
(kiranshila.generate-test/test-hiccup
 "Example 53"
 "Foo\n-------------------------\n\nFoo\n=\n"
 [:h2 "Foo"]
 [:h1 "Foo"])
(kiranshila.generate-test/test-hiccup
 "Example 54"
 "   Foo\n---\n\n  Foo\n-----\n\n  Foo\n  ===\n"
 [:h2 "Foo"]
 [:h2 "Foo"]
 [:h1 "Foo"])
(kiranshila.generate-test/test-hiccup
 "Example 55"
 "    Foo\n    ---\n\n    Foo\n---\n"
 [:pre [:code "Foo\n---\n\nFoo"]]
 [:hr])
(kiranshila.generate-test/test-hiccup
 "Example 56"
 "Foo\n   ----      \n"
 [:h2 "Foo"])
(kiranshila.generate-test/test-hiccup
 "Example 57"
 "Foo\n    ---\n"
 [:p "Foo\n---"])
(kiranshila.generate-test/test-hiccup
 "Example 58"
 "Foo\n= =\n\nFoo\n--- -\n"
 [:p "Foo\n= ="]
 [:p "Foo"]
 [:hr])
(kiranshila.generate-test/test-hiccup
 "Example 59"
 "Foo  \n-----\n"
 [:h2 "Foo"])
(kiranshila.generate-test/test-hiccup
 "Example 60"
 "Foo\\\n----\n"
 [:h2 "Foo\\"])
(kiranshila.generate-test/test-hiccup
 "Example 61"
 "`Foo\n----\n`\n\n<a title=\"a lot\n---\nof dashes\"/>\n"
 [:h2 "`Foo"]
 [:p "`"]
 [:h2 "&lt;a title=&quot;a lot"]
 [:p "of dashes&quot;/&gt;"])
(kiranshila.generate-test/test-hiccup
 "Example 62"
 "> Foo\n---\n"
 [:blockquote [:p "Foo"]]
 [:hr])
(kiranshila.generate-test/test-hiccup
 "Example 63"
 "> foo\nbar\n===\n"
 [:blockquote [:p "foo\nbar\n==="]])
(kiranshila.generate-test/test-hiccup
 "Example 64"
 "- Foo\n---\n"
 [:ul [:li "Foo"]]
 [:hr])
(kiranshila.generate-test/test-hiccup
 "Example 65"
 "Foo\nBar\n---\n"
 [:h2 "Foo\nBar"])
(kiranshila.generate-test/test-hiccup
 "Example 66"
 "---\nFoo\n---\nBar\n---\nBaz\n"
 [:hr]
 [:h2 "Foo"]
 [:h2 "Bar"]
 [:p "Baz"])
(kiranshila.generate-test/test-hiccup
 "Example 67"
 "\n====\n"
 [:p "===="])
(kiranshila.generate-test/test-hiccup
 "Example 68"
 "---\n---\n"
 [:hr]
 [:hr])
(kiranshila.generate-test/test-hiccup
 "Example 69"
 "- foo\n-----\n"
 [:ul [:li "foo"]]
 [:hr])
(kiranshila.generate-test/test-hiccup
 "Example 70"
 "    foo\n---\n"
 [:pre [:code "foo"]]
 [:hr])
(kiranshila.generate-test/test-hiccup
 "Example 71"
 "> foo\n-----\n"
 [:blockquote [:p "foo"]]
 [:hr])
(kiranshila.generate-test/test-hiccup
 "Example 72"
 "\\> foo\n------\n"
 [:h2 "&gt; foo"])
(kiranshila.generate-test/test-hiccup
 "Example 73"
 "Foo\n\nbar\n---\nbaz\n"
 [:p "Foo"]
 [:h2 "bar"]
 [:p "baz"])
(kiranshila.generate-test/test-hiccup
 "Example 74"
 "Foo\nbar\n\n---\n\nbaz\n"
 [:p "Foo\nbar"]
 [:hr]
 [:p "baz"])
(kiranshila.generate-test/test-hiccup
 "Example 75"
 "Foo\nbar\n* * *\nbaz\n"
 [:p "Foo\nbar"]
 [:hr]
 [:p "baz"])
(kiranshila.generate-test/test-hiccup
 "Example 76"
 "Foo\nbar\n\\---\nbaz\n"
 [:p "Foo\nbar\n---\nbaz"])
(kiranshila.generate-test/test-hiccup
 "Example 77"
 "    a simple\n      indented code block\n"
 [:pre [:code "a simple\n  indented code block"]])
(kiranshila.generate-test/test-hiccup
 "Example 78"
 "  - foo\n\n    bar\n"
 [:ul [:li [:p "foo"] [:p "bar"]]])
(kiranshila.generate-test/test-hiccup
 "Example 79"
 "1.  foo\n\n    - bar\n"
 [:ol [:li [:p "foo"] [:ul [:li "bar"]]]])
(kiranshila.generate-test/test-hiccup
 "Example 80"
 "    <a/>\n    *hi*\n\n    - one\n"
 [:pre [:code "&lt;a/&gt;\n*hi*\n\n- one"]])
(kiranshila.generate-test/test-hiccup
 "Example 81"
 "    chunk1\n\n    chunk2\n  \n \n \n    chunk3\n"
 [:pre [:code "chunk1\n\nchunk2\n\n\n\nchunk3"]])
(kiranshila.generate-test/test-hiccup
 "Example 82"
 "    chunk1\n      \n      chunk2\n"
 [:pre [:code "chunk1\n  \n  chunk2"]])
(kiranshila.generate-test/test-hiccup
 "Example 83"
 "Foo\n    bar\n\n"
 [:p "Foo\nbar"])
(kiranshila.generate-test/test-hiccup
 "Example 84"
 "    foo\nbar\n"
 [:pre [:code "foo"]]
 [:p "bar"])
(kiranshila.generate-test/test-hiccup
 "Example 85"
 "# Heading\n    foo\nHeading\n------\n    foo\n----\n"
 [:h1 "Heading"]
 [:pre [:code "foo"]]
 [:h2 "Heading"]
 [:pre [:code "foo"]]
 [:hr])
(kiranshila.generate-test/test-hiccup
 "Example 86"
 "        foo\n    bar\n"
 [:pre [:code "foo\nbar"]])
(kiranshila.generate-test/test-hiccup
 "Example 87"
 "\n    \n    foo\n    \n\n"
 [:pre [:code "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 88"
 "    foo  \n"
 [:pre [:code "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 89"
 "```\n<\n >\n```\n"
 [:pre [:code "&lt;\n &gt;"]])
(kiranshila.generate-test/test-hiccup
 "Example 90"
 "~~~\n<\n >\n~~~\n"
 [:pre [:code "&lt;\n &gt;"]])
(kiranshila.generate-test/test-hiccup
 "Example 91"
 "``\nfoo\n``\n"
 [:p [:code "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 92"
 "```\naaa\n~~~\n```\n"
 [:pre [:code "aaa\n~~~"]])
(kiranshila.generate-test/test-hiccup
 "Example 93"
 "~~~\naaa\n```\n~~~\n"
 [:pre [:code "aaa\n```"]])
(kiranshila.generate-test/test-hiccup
 "Example 94"
 "````\naaa\n```\n``````\n"
 [:pre [:code "aaa\n```"]])
(kiranshila.generate-test/test-hiccup
 "Example 95"
 "~~~~\naaa\n~~~\n~~~~\n"
 [:pre [:code "aaa\n~~~"]])
(kiranshila.generate-test/test-hiccup
 "Example 96"
 "```\n"
 [:pre [:code]])
(kiranshila.generate-test/test-hiccup
 "Example 97"
 "`````\n\n```\naaa\n"
 [:pre [:code "```\naaa"]])
(kiranshila.generate-test/test-hiccup
 "Example 98"
 "> ```\n> aaa\n\nbbb\n"
 [:blockquote [:pre [:code "aaa"]]]
 [:p "bbb"])
(kiranshila.generate-test/test-hiccup
 "Example 99"
 "```\n\n  \n```\n"
 [:pre [:code]])
(kiranshila.generate-test/test-hiccup
 "Example 100"
 "```\n```\n"
 [:pre [:code]])
(kiranshila.generate-test/test-hiccup
 "Example 101"
 " ```\n aaa\naaa\n```\n"
 [:pre [:code "aaa\naaa"]])
(kiranshila.generate-test/test-hiccup
 "Example 102"
 "  ```\naaa\n  aaa\naaa\n  ```\n"
 [:pre [:code "aaa\naaa\naaa"]])
(kiranshila.generate-test/test-hiccup
 "Example 103"
 "   ```\n   aaa\n    aaa\n  aaa\n   ```\n"
 [:pre [:code "aaa\n aaa\naaa"]])
(kiranshila.generate-test/test-hiccup
 "Example 104"
 "    ```\n    aaa\n    ```\n"
 [:pre [:code "```\naaa\n```"]])
(kiranshila.generate-test/test-hiccup
 "Example 105"
 "```\naaa\n  ```\n"
 [:pre [:code "aaa"]])
(kiranshila.generate-test/test-hiccup
 "Example 106"
 "   ```\naaa\n  ```\n"
 [:pre [:code "aaa"]])
(kiranshila.generate-test/test-hiccup
 "Example 107"
 "```\naaa\n    ```\n"
 [:pre [:code "aaa\n    ```"]])
(kiranshila.generate-test/test-hiccup
 "Example 108"
 "``` ```\naaa\n"
 [:p [:code] "aaa"])
(kiranshila.generate-test/test-hiccup
 "Example 109"
 "~~~~~~\naaa\n~~~ ~~\n"
 [:pre [:code "aaa\n~~~ ~~"]])
(kiranshila.generate-test/test-hiccup
 "Example 110"
 "foo\n```\nbar\n```\nbaz\n"
 [:p "foo"]
 [:pre [:code "bar"]]
 [:p "baz"])
(kiranshila.generate-test/test-hiccup
 "Example 111"
 "foo\n---\n~~~\nbar\n~~~\n# baz\n"
 [:h2 "foo"]
 [:pre [:code "bar"]]
 [:h1 "baz"])
(kiranshila.generate-test/test-hiccup
 "Example 112"
 "```ruby\ndef foo(x)\n  return 3\nend\n```\n"
 [:pre [:code {:class "language-ruby"} "def foo(x)\n  return 3\nend"]])
(kiranshila.generate-test/test-hiccup
 "Example 113"
 "~~~~    ruby startline=3 $%@#$\ndef foo(x)\n  return 3\nend\n~~~~~~~\n"
 [:pre [:code {:class "language-ruby"} "def foo(x)\n  return 3\nend"]])
(kiranshila.generate-test/test-hiccup
 "Example 114"
 "````;\n````\n"
 [:pre [:code {:class "language-;"}]])
(kiranshila.generate-test/test-hiccup
 "Example 115"
 "``` aa ```\nfoo\n"
 [:p [:code "aa"] "foo"])
(kiranshila.generate-test/test-hiccup
 "Example 116"
 "~~~ aa ``` ~~~\nfoo\n~~~\n"
 [:pre [:code {:class "language-aa"} "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 117"
 "```\n``` aaa\n```\n"
 [:pre [:code "``` aaa"]])
(kiranshila.generate-test/test-hiccup
 "Example 118"
 "<table><tr><td>\n<pre>\n**Hello**,\n\n_world_.\n</pre>\n</td></tr></table>\n"
 [:table
  [:tbody [:tr [:td [:pre "**Hello**," [:p [:em "world"] "."]] [:p]]]]])
(kiranshila.generate-test/test-hiccup
 "Example 119"
 "<table>\n  <tr>\n    <td>\n           hi\n    </td>\n  </tr>\n</table>\n\nokay.\n"
 [:table [:tbody [:tr [:td "hi"]]]]
 [:p "okay."])
(kiranshila.generate-test/test-hiccup
 "Example 120"
 " <div>\n  *hello*\n         <foo><a>\n"
 [:div "*hello*" [:foo [:a]]])
(kiranshila.generate-test/test-hiccup "Example 121" "</div>\n*foo*\n")
(kiranshila.generate-test/test-hiccup
 "Example 122"
 "<DIV CLASS=\"foo\">\n\n*Markdown*\n\n</DIV>\n"
 [:div {:class "foo"} [:p [:em "Markdown"]]])
(kiranshila.generate-test/test-hiccup
 "Example 123"
 "<div id=\"foo\"\n  class=\"bar\">\n</div>\n"
 [:div {:id "foo", :class "bar"}])
(kiranshila.generate-test/test-hiccup
 "Example 124"
 "<div id=\"foo\" class=\"bar\n  baz\">\n</div>\n"
 [:div {:id "foo", :class "bar\n  baz"}])
(kiranshila.generate-test/test-hiccup
 "Example 125"
 "<div>\n*foo*\n\n*bar*\n"
 [:div "*foo*" [:p [:em "bar"]]])
(kiranshila.generate-test/test-hiccup
 "Example 126"
 "<div id=\"foo\"\n*hi*\n")
(kiranshila.generate-test/test-hiccup "Example 127" "<div class\nfoo\n")
(kiranshila.generate-test/test-hiccup
 "Example 128"
 "<div *???-&&&-<---\n*foo*\n")
(kiranshila.generate-test/test-hiccup
 "Example 129"
 "<div><a href=\"bar\">*foo*</a></div>\n"
 [:div [:a {:href "bar"} "*foo*"]])
(kiranshila.generate-test/test-hiccup
 "Example 130"
 "<table><tr><td>\nfoo\n</td></tr></table>\n"
 [:table [:tbody [:tr [:td "foo"]]]])
(kiranshila.generate-test/test-hiccup
 "Example 131"
 "<div></div>\n``` c\nint x = 33;\n```\n"
 [:div])
(kiranshila.generate-test/test-hiccup
 "Example 132"
 "<a href=\"foo\">\n*bar*\n</a>\n"
 [:a {:href "foo"} "*bar*"])
(kiranshila.generate-test/test-hiccup
 "Example 133"
 "<Warning>\n*bar*\n</Warning>\n"
 [:warning "*bar*"])
(kiranshila.generate-test/test-hiccup
 "Example 134"
 "<i class=\"foo\">\n*bar*\n</i>\n"
 [:i {:class "foo"} "*bar*"])
(kiranshila.generate-test/test-hiccup "Example 135" "</ins>\n*bar*\n")
(kiranshila.generate-test/test-hiccup
 "Example 136"
 "<del>\n*foo*\n</del>\n"
 [:del "*foo*"])
(kiranshila.generate-test/test-hiccup
 "Example 137"
 "<del>\n\n*foo*\n\n</del>\n"
 [:del [:p [:em "foo"]]])
(kiranshila.generate-test/test-hiccup
 "Example 138"
 "<del>*foo*</del>\n"
 [:p [:del [:em "foo"]]])
(kiranshila.generate-test/test-hiccup
 "Example 139"
 "<pre language=\"haskell\"><code>\nimport Text.HTML.TagSoup\n\nmain :: IO ()\nmain = print $ parseTags tags\n</code></pre>\nokay\n"
 [:pre
  {:language "haskell"}
  [:code
   "import Text.HTML.TagSoup\n\nmain :: IO ()\nmain = print $ parseTags tags"]]
 [:p "okay"])
(kiranshila.generate-test/test-hiccup
 "Example 140"
 "<script type=\"text/javascript\">\n// JavaScript example\n\ndocument.getElementById(\"demo\").innerHTML = \"Hello JavaScript!\";\n</script>\nokay\n"
 [:script
  {:type "text/javascript"}
  "// JavaScript example\n\ndocument.getElementById(\"demo\").innerHTML = \"Hello JavaScript!\";"]
 [:p "okay"])
(kiranshila.generate-test/test-hiccup
 "Example 141"
 "<style\n  type=\"text/css\">\nh1 {color:red;}\n\np {color:blue;}\n</style>\nokay\n"
 [:style {:type "text/css"} "h1 {color:red;}\n\np {color:blue;}"]
 [:p "okay"])
(kiranshila.generate-test/test-hiccup
 "Example 142"
 "<style\n  type=\"text/css\">\n\nfoo\n"
 [:style {:type "text/css"} "foo"])
(kiranshila.generate-test/test-hiccup
 "Example 143"
 "> <div>\n> foo\n\nbar\n"
 [:blockquote [:div "foo"]]
 [:p "bar"])
(kiranshila.generate-test/test-hiccup
 "Example 144"
 "- <div>\n- foo\n"
 [:ul [:li [:div]] [:li "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 145"
 "<style>p{color:red;}</style>\n*foo*\n"
 [:style "p{color:red;}"]
 [:p [:em "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 146"
 "<!-- foo -->*bar*\n*baz*\n"
 [:p [:em "baz"]])
(kiranshila.generate-test/test-hiccup
 "Example 147"
 "<script>\nfoo\n</script>1. *bar*\n"
 [:script "foo"])
(kiranshila.generate-test/test-hiccup
 "Example 148"
 "<!-- Foo\n\nbar\n   baz -->\nokay\n"
 [:p "okay"])
(kiranshila.generate-test/test-hiccup
 "Example 149"
 "<?php\n\n  echo '>';\n\n?>\nokay\n"
 [:p "okay"])
(kiranshila.generate-test/test-hiccup "Example 150" "<!DOCTYPE html>\n")
(kiranshila.generate-test/test-hiccup
 "Example 151"
 "<![CDATA[\nfunction matchwo(a,b)\n{\n  if (a < b && a < 0) then {\n    return 1;\n\n  } else {\n\n    return 0;\n  }\n}\n]]>\nokay\n"
 [:p "okay"])
(kiranshila.generate-test/test-hiccup
 "Example 152"
 "  <!-- foo -->\n\n    <!-- foo -->\n"
 [:pre [:code "&lt;!-- foo --&gt;"]])
(kiranshila.generate-test/test-hiccup
 "Example 153"
 "  <div>\n\n    <div>\n"
 [:div [:pre [:code "&lt;div&gt;"]]])
(kiranshila.generate-test/test-hiccup
 "Example 154"
 "Foo\n<div>\nbar\n</div>\n"
 [:p "Foo"]
 [:div "bar"])
(kiranshila.generate-test/test-hiccup
 "Example 155"
 "<div>\nbar\n</div>\n*foo*\n"
 [:div "bar"])
(kiranshila.generate-test/test-hiccup
 "Example 156"
 "Foo\n<a href=\"bar\">\nbaz\n"
 [:p "Foo" [:a {:href "bar"} "baz"]]
 [:a {:href "bar"}])
(kiranshila.generate-test/test-hiccup
 "Example 157"
 "<div>\n\n*Emphasized* text.\n\n</div>\n"
 [:div [:p [:em "Emphasized"] "text."]])
(kiranshila.generate-test/test-hiccup
 "Example 158"
 "<div>\n*Emphasized* text.\n</div>\n"
 [:div "*Emphasized* text."])
(kiranshila.generate-test/test-hiccup
 "Example 159"
 "<table>\n\n<tr>\n\n<td>\nHi\n</td>\n\n</tr>\n\n</table>\n"
 [:table [:tbody [:tr [:td "Hi"]]]])
(kiranshila.generate-test/test-hiccup
 "Example 160"
 "<table>\n\n  <tr>\n\n    <td>\n      Hi\n    </td>\n\n  </tr>\n\n</table>\n"
 [:pre [:code "&lt;td&gt;\n  Hi\n&lt;/td&gt;"]]
 [:table [:tbody [:tr]]])
(kiranshila.generate-test/test-hiccup
 "Example 161"
 "[foo]: /url \"title\"\n\n[foo]\n"
 [:p [:a {:href "/url", :title "title"} "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 162"
 "   [foo]: \n      /url  \n           'the title'  \n\n[foo]\n"
 [:p [:a {:href "/url", :title "the title"} "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 163"
 "[Foo*bar\\]]:my_(url) 'title (with parens)'\n\n[Foo*bar\\]]\n"
 [:p [:a {:href "my_(url)", :title "title (with parens)"} "Foo*bar]"]])
(kiranshila.generate-test/test-hiccup
 "Example 164"
 "[Foo bar]:\n<my url>\n'title'\n\n[Foo bar]\n"
 [:p [:a {:href "my%20url", :title "title"} "Foo bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 165"
 "[foo]: /url '\ntitle\nline1\nline2\n'\n\n[foo]\n"
 [:p [:a {:href "/url", :title "title\nline1\nline2"} "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 166"
 "[foo]: /url 'title\n\nwith blank line'\n\n[foo]\n"
 [:p "[foo]: /url 'title"]
 [:p "with blank line'"]
 [:p "[foo]"])
(kiranshila.generate-test/test-hiccup
 "Example 167"
 "[foo]:\n/url\n\n[foo]\n"
 [:p [:a {:href "/url"} "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 168"
 "[foo]:\n\n[foo]\n"
 [:p "[foo]:"]
 [:p "[foo]"])
(kiranshila.generate-test/test-hiccup
 "Example 169"
 "[foo]: <>\n\n[foo]\n"
 [:p [:a {:href ""} "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 170"
 "[foo]: <bar>(baz)\n\n[foo]\n"
 [:p "[foo]:" [:bar "(baz)"]]
 [:p "[foo]"])
(kiranshila.generate-test/test-hiccup
 "Example 171"
 "[foo]: /url\\bar\\*baz \"foo\\\"bar\\baz\"\n\n[foo]\n"
 [:p [:a {:href "/url%5Cbar*baz", :title "foo\"bar\\baz"} "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 172"
 "[foo]\n\n[foo]: url\n"
 [:p [:a {:href "url"} "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 173"
 "[foo]\n\n[foo]: first\n[foo]: second\n"
 [:p [:a {:href "first"} "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 174"
 "[FOO]: /url\n\n[Foo]\n"
 [:p [:a {:href "/url"} "Foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 175"
 "[ΑΓΩ]: /φου\n\n[αγω]\n"
 [:p [:a {:href "/%CF%86%CE%BF%CF%85"} "αγω"]])
(kiranshila.generate-test/test-hiccup "Example 176" "[foo]: /url\n")
(kiranshila.generate-test/test-hiccup
 "Example 177"
 "[\nfoo\n]: /url\nbar\n"
 [:p "bar"])
(kiranshila.generate-test/test-hiccup
 "Example 178"
 "[foo]: /url \"title\" ok\n"
 [:p "[foo]: /url &quot;title&quot; ok"])
(kiranshila.generate-test/test-hiccup
 "Example 179"
 "[foo]: /url\n\"title\" ok\n"
 [:p "&quot;title&quot; ok"])
(kiranshila.generate-test/test-hiccup
 "Example 180"
 "    [foo]: /url \"title\"\n\n[foo]\n"
 [:pre [:code "[foo]: /url &quot;title&quot;"]]
 [:p "[foo]"])
(kiranshila.generate-test/test-hiccup
 "Example 181"
 "```\n[foo]: /url\n```\n\n[foo]\n"
 [:pre [:code "[foo]: /url"]]
 [:p "[foo]"])
(kiranshila.generate-test/test-hiccup
 "Example 182"
 "Foo\n[bar]: /baz\n\n[bar]\n"
 [:p "Foo\n[bar]: /baz"]
 [:p "[bar]"])
(kiranshila.generate-test/test-hiccup
 "Example 183"
 "# [Foo]\n[foo]: /url\n> bar\n"
 [:h1 [:a {:href "/url"} "Foo"]]
 [:blockquote [:p "bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 184"
 "[foo]: /url\nbar\n===\n[foo]\n"
 [:h1 "bar"]
 [:p [:a {:href "/url"} "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 185"
 "[foo]: /url\n===\n[foo]\n"
 [:p "===" [:a {:href "/url"} "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 186"
 "[foo]: /foo-url \"foo\"\n[bar]: /bar-url\n  \"bar\"\n[baz]: /baz-url\n\n[foo],\n[bar],\n[baz]\n"
 [:p
  [:a {:href "/foo-url", :title "foo"} "foo"]
  ","
  [:a {:href "/bar-url", :title "bar"} "bar"]
  ","
  [:a {:href "/baz-url"} "baz"]])
(kiranshila.generate-test/test-hiccup
 "Example 187"
 "[foo]\n\n> [foo]: /url\n"
 [:p [:a {:href "/url"} "foo"]]
 [:blockquote])
(kiranshila.generate-test/test-hiccup "Example 188" "[foo]: /url\n")
(kiranshila.generate-test/test-hiccup
 "Example 189"
 "aaa\n\nbbb\n"
 [:p "aaa"]
 [:p "bbb"])
(kiranshila.generate-test/test-hiccup
 "Example 190"
 "aaa\nbbb\n\nccc\nddd\n"
 [:p "aaa\nbbb"]
 [:p "ccc\nddd"])
(kiranshila.generate-test/test-hiccup
 "Example 191"
 "aaa\n\n\nbbb\n"
 [:p "aaa"]
 [:p "bbb"])
(kiranshila.generate-test/test-hiccup
 "Example 192"
 "  aaa\n bbb\n"
 [:p "aaa\nbbb"])
(kiranshila.generate-test/test-hiccup
 "Example 193"
 "aaa\n             bbb\n                                       ccc\n"
 [:p "aaa\nbbb\nccc"])
(kiranshila.generate-test/test-hiccup
 "Example 194"
 "   aaa\nbbb\n"
 [:p "aaa\nbbb"])
(kiranshila.generate-test/test-hiccup
 "Example 195"
 "    aaa\nbbb\n"
 [:pre [:code "aaa"]]
 [:p "bbb"])
(kiranshila.generate-test/test-hiccup
 "Example 196"
 "aaa     \nbbb     \n"
 [:p "aaa" [:br] "bbb"])
(kiranshila.generate-test/test-hiccup
 "Example 197"
 "  \n\naaa\n  \n\n# aaa\n\n  \n"
 [:p "aaa"]
 [:h1 "aaa"])
(kiranshila.generate-test/test-hiccup
 "Example 198"
 "> # Foo\n> bar\n> baz\n"
 [:blockquote [:h1 "Foo"] [:p "bar\nbaz"]])
(kiranshila.generate-test/test-hiccup
 "Example 199"
 "># Foo\n>bar\n> baz\n"
 [:blockquote [:h1 "Foo"] [:p "bar\nbaz"]])
(kiranshila.generate-test/test-hiccup
 "Example 200"
 "   > # Foo\n   > bar\n > baz\n"
 [:blockquote [:h1 "Foo"] [:p "bar\nbaz"]])
(kiranshila.generate-test/test-hiccup
 "Example 201"
 "    > # Foo\n    > bar\n    > baz\n"
 [:pre [:code "&gt; # Foo\n&gt; bar\n&gt; baz"]])
(kiranshila.generate-test/test-hiccup
 "Example 202"
 "> # Foo\n> bar\nbaz\n"
 [:blockquote [:h1 "Foo"] [:p "bar\nbaz"]])
(kiranshila.generate-test/test-hiccup
 "Example 203"
 "> bar\nbaz\n> foo\n"
 [:blockquote [:p "bar\nbaz\nfoo"]])
(kiranshila.generate-test/test-hiccup
 "Example 204"
 "> foo\n---\n"
 [:blockquote [:p "foo"]]
 [:hr])
(kiranshila.generate-test/test-hiccup
 "Example 205"
 "> - foo\n- bar\n"
 [:blockquote [:ul [:li "foo"]]]
 [:ul [:li "bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 206"
 ">     foo\n    bar\n"
 [:blockquote [:pre [:code "foo"]]]
 [:pre [:code "bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 207"
 "> ```\nfoo\n```\n"
 [:blockquote [:pre [:code]]]
 [:p "foo"]
 [:pre [:code]])
(kiranshila.generate-test/test-hiccup
 "Example 208"
 "> foo\n    - bar\n"
 [:blockquote [:p "foo\n- bar"]])
(kiranshila.generate-test/test-hiccup "Example 209" ">\n" [:blockquote])
(kiranshila.generate-test/test-hiccup
 "Example 210"
 ">\n>  \n> \n"
 [:blockquote])
(kiranshila.generate-test/test-hiccup
 "Example 211"
 ">\n> foo\n>  \n"
 [:blockquote [:p "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 212"
 "> foo\n\n> bar\n"
 [:blockquote [:p "foo"]]
 [:blockquote [:p "bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 213"
 "> foo\n> bar\n"
 [:blockquote [:p "foo\nbar"]])
(kiranshila.generate-test/test-hiccup
 "Example 214"
 "> foo\n>\n> bar\n"
 [:blockquote [:p "foo"] [:p "bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 215"
 "foo\n> bar\n"
 [:p "foo"]
 [:blockquote [:p "bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 216"
 "> aaa\n***\n> bbb\n"
 [:blockquote [:p "aaa"]]
 [:hr]
 [:blockquote [:p "bbb"]])
(kiranshila.generate-test/test-hiccup
 "Example 217"
 "> bar\nbaz\n"
 [:blockquote [:p "bar\nbaz"]])
(kiranshila.generate-test/test-hiccup
 "Example 218"
 "> bar\n\nbaz\n"
 [:blockquote [:p "bar"]]
 [:p "baz"])
(kiranshila.generate-test/test-hiccup
 "Example 219"
 "> bar\n>\nbaz\n"
 [:blockquote [:p "bar"]]
 [:p "baz"])
(kiranshila.generate-test/test-hiccup
 "Example 220"
 "> > > foo\nbar\n"
 [:blockquote [:blockquote [:blockquote [:p "foo\nbar"]]]])
(kiranshila.generate-test/test-hiccup
 "Example 221"
 ">>> foo\n> bar\n>>baz\n"
 [:blockquote [:blockquote [:blockquote [:p "foo\nbar\nbaz"]]]])
(kiranshila.generate-test/test-hiccup
 "Example 222"
 ">     code\n\n>    not code\n"
 [:blockquote [:pre [:code "code"]]]
 [:blockquote [:p "not code"]])
(kiranshila.generate-test/test-hiccup
 "Example 223"
 "A paragraph\nwith two lines.\n\n    indented code\n\n> A block quote.\n"
 [:p "A paragraph\nwith two lines."]
 [:pre [:code "indented code"]]
 [:blockquote [:p "A block quote."]])
(kiranshila.generate-test/test-hiccup
 "Example 224"
 "1.  A paragraph\n    with two lines.\n\n        indented code\n\n    > A block quote.\n"
 [:ol
  [:li
   [:p "A paragraph\nwith two lines."]
   [:pre [:code "indented code"]]
   [:blockquote [:p "A block quote."]]]])
(kiranshila.generate-test/test-hiccup
 "Example 225"
 "- one\n\n two\n"
 [:ul [:li "one"]]
 [:p "two"])
(kiranshila.generate-test/test-hiccup
 "Example 226"
 "- one\n\n  two\n"
 [:ul [:li [:p "one"] [:p "two"]]])
(kiranshila.generate-test/test-hiccup
 "Example 227"
 " -    one\n\n     two\n"
 [:ul [:li "one"]]
 [:pre [:code "two"]])
(kiranshila.generate-test/test-hiccup
 "Example 228"
 " -    one\n\n      two\n"
 [:ul [:li [:p "one"] [:p "two"]]])
(kiranshila.generate-test/test-hiccup
 "Example 229"
 "   > > 1.  one\n>>\n>>     two\n"
 [:blockquote [:blockquote [:ol [:li [:p "one"] [:p "two"]]]]])
(kiranshila.generate-test/test-hiccup
 "Example 230"
 ">>- one\n>>\n  >  > two\n"
 [:blockquote [:blockquote [:ul [:li "one"]] [:p "two"]]])
(kiranshila.generate-test/test-hiccup
 "Example 231"
 "-one\n\n2.two\n"
 [:p "-one"]
 [:p "2.two"])
(kiranshila.generate-test/test-hiccup
 "Example 232"
 "- foo\n\n\n  bar\n"
 [:ul [:li [:p "foo"] [:p "bar"]]])
(kiranshila.generate-test/test-hiccup
 "Example 233"
 "1.  foo\n\n    ```\n    bar\n    ```\n\n    baz\n\n    > bam\n"
 [:ol
  [:li
   [:p "foo"]
   [:pre [:code "bar"]]
   [:p "baz"]
   [:blockquote [:p "bam"]]]])
(kiranshila.generate-test/test-hiccup
 "Example 234"
 "- Foo\n\n      bar\n\n\n      baz\n"
 [:ul [:li [:p "Foo"] [:pre [:code "bar\n\n\nbaz"]]]])
(kiranshila.generate-test/test-hiccup
 "Example 235"
 "123456789. ok\n"
 [:ol {:start "123456789"} [:li "ok"]])
(kiranshila.generate-test/test-hiccup
 "Example 236"
 "1234567890. not ok\n"
 [:p "1234567890. not ok"])
(kiranshila.generate-test/test-hiccup
 "Example 237"
 "0. ok\n"
 [:ol {:start "0"} [:li "ok"]])
(kiranshila.generate-test/test-hiccup
 "Example 238"
 "003. ok\n"
 [:ol {:start "3"} [:li "ok"]])
(kiranshila.generate-test/test-hiccup
 "Example 239"
 "-1. not ok\n"
 [:p "-1. not ok"])
(kiranshila.generate-test/test-hiccup
 "Example 240"
 "- foo\n\n      bar\n"
 [:ul [:li [:p "foo"] [:pre [:code "bar"]]]])
(kiranshila.generate-test/test-hiccup
 "Example 241"
 "  10.  foo\n\n           bar\n"
 [:ol {:start "10"} [:li [:p "foo"] [:pre [:code "bar"]]]])
(kiranshila.generate-test/test-hiccup
 "Example 242"
 "    indented code\n\nparagraph\n\n    more code\n"
 [:pre [:code "indented code"]]
 [:p "paragraph"]
 [:pre [:code "more code"]])
(kiranshila.generate-test/test-hiccup
 "Example 243"
 "1.     indented code\n\n   paragraph\n\n       more code\n"
 [:ol
  [:li
   [:pre [:code "indented code"]]
   [:p "paragraph"]
   [:pre [:code "more code"]]]])
(kiranshila.generate-test/test-hiccup
 "Example 244"
 "1.      indented code\n\n   paragraph\n\n       more code\n"
 [:ol
  [:li
   [:pre [:code "indented code"]]
   [:p "paragraph"]
   [:pre [:code "more code"]]]])
(kiranshila.generate-test/test-hiccup
 "Example 245"
 "   foo\n\nbar\n"
 [:p "foo"]
 [:p "bar"])
(kiranshila.generate-test/test-hiccup
 "Example 246"
 "-    foo\n\n  bar\n"
 [:ul [:li "foo"]]
 [:p "bar"])
(kiranshila.generate-test/test-hiccup
 "Example 247"
 "-  foo\n\n   bar\n"
 [:ul [:li [:p "foo"] [:p "bar"]]])
(kiranshila.generate-test/test-hiccup
 "Example 248"
 "-\n  foo\n-\n  ```\n  bar\n  ```\n-\n      baz\n"
 [:ul
  [:li "foo"]
  [:li [:pre [:code "bar"]]]
  [:li [:pre [:code "baz"]]]])
(kiranshila.generate-test/test-hiccup
 "Example 249"
 "-   \n  foo\n"
 [:ul [:li "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 250"
 "-\n\n  foo\n"
 [:ul [:li]]
 [:p "foo"])
(kiranshila.generate-test/test-hiccup
 "Example 251"
 "- foo\n-\n- bar\n"
 [:ul [:li "foo"] [:li] [:li "bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 252"
 "- foo\n-   \n- bar\n"
 [:ul [:li "foo"] [:li] [:li "bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 253"
 "1. foo\n2.\n3. bar\n"
 [:ol [:li "foo"] [:li] [:li "bar"]])
(kiranshila.generate-test/test-hiccup "Example 254" "*\n" [:ul [:li]])
(kiranshila.generate-test/test-hiccup
 "Example 255"
 "foo\n*\n\nfoo\n1.\n"
 [:p "foo\n*"]
 [:p "foo\n1."])
(kiranshila.generate-test/test-hiccup
 "Example 256"
 " 1.  A paragraph\n     with two lines.\n\n         indented code\n\n     > A block quote.\n"
 [:ol
  [:li
   [:p "A paragraph\nwith two lines."]
   [:pre [:code "indented code"]]
   [:blockquote [:p "A block quote."]]]])
(kiranshila.generate-test/test-hiccup
 "Example 257"
 "  1.  A paragraph\n      with two lines.\n\n          indented code\n\n      > A block quote.\n"
 [:ol
  [:li
   [:p "A paragraph\nwith two lines."]
   [:pre [:code "indented code"]]
   [:blockquote [:p "A block quote."]]]])
(kiranshila.generate-test/test-hiccup
 "Example 258"
 "   1.  A paragraph\n       with two lines.\n\n           indented code\n\n       > A block quote.\n"
 [:ol
  [:li
   [:p "A paragraph\nwith two lines."]
   [:pre [:code "indented code"]]
   [:blockquote [:p "A block quote."]]]])
(kiranshila.generate-test/test-hiccup
 "Example 259"
 "    1.  A paragraph\n        with two lines.\n\n            indented code\n\n        > A block quote.\n"
 [:pre
  [:code
   "1.  A paragraph\n    with two lines.\n\n        indented code\n\n    &gt; A block quote."]])
(kiranshila.generate-test/test-hiccup
 "Example 260"
 "  1.  A paragraph\nwith two lines.\n\n          indented code\n\n      > A block quote.\n"
 [:ol
  [:li
   [:p "A paragraph\nwith two lines."]
   [:pre [:code "indented code"]]
   [:blockquote [:p "A block quote."]]]])
(kiranshila.generate-test/test-hiccup
 "Example 261"
 "  1.  A paragraph\n    with two lines.\n"
 [:ol [:li "A paragraph\nwith two lines."]])
(kiranshila.generate-test/test-hiccup
 "Example 262"
 "> 1. > Blockquote\ncontinued here.\n"
 [:blockquote
  [:ol [:li [:blockquote [:p "Blockquote\ncontinued here."]]]]])
(kiranshila.generate-test/test-hiccup
 "Example 263"
 "> 1. > Blockquote\n> continued here.\n"
 [:blockquote
  [:ol [:li [:blockquote [:p "Blockquote\ncontinued here."]]]]])
(kiranshila.generate-test/test-hiccup
 "Example 264"
 "- foo\n  - bar\n    - baz\n      - boo\n"
 [:ul
  [:li "foo" [:ul [:li "bar" [:ul [:li "baz" [:ul [:li "boo"]]]]]]]])
(kiranshila.generate-test/test-hiccup
 "Example 265"
 "- foo\n - bar\n  - baz\n   - boo\n"
 [:ul [:li "foo"] [:li "bar"] [:li "baz"] [:li "boo"]])
(kiranshila.generate-test/test-hiccup
 "Example 266"
 "10) foo\n    - bar\n"
 [:ol {:start "10"} [:li "foo" [:ul [:li "bar"]]]])
(kiranshila.generate-test/test-hiccup
 "Example 267"
 "10) foo\n   - bar\n"
 [:ol {:start "10"} [:li "foo"]]
 [:ul [:li "bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 268"
 "- - foo\n"
 [:ul [:li [:ul [:li "foo"]]]])
(kiranshila.generate-test/test-hiccup
 "Example 269"
 "1. - 2. foo\n"
 [:ol [:li [:ul [:li [:ol {:start "2"} [:li "foo"]]]]]])
(kiranshila.generate-test/test-hiccup
 "Example 270"
 "- # Foo\n- Bar\n  ---\n  baz\n"
 [:ul [:li [:h1 "Foo"]] [:li [:h2 "Bar"] "baz"]])
(kiranshila.generate-test/test-hiccup
 "Example 271"
 "- foo\n- bar\n+ baz\n"
 [:ul [:li "foo"] [:li "bar"]]
 [:ul [:li "baz"]])
(kiranshila.generate-test/test-hiccup
 "Example 272"
 "1. foo\n2. bar\n3) baz\n"
 [:ol [:li "foo"] [:li "bar"]]
 [:ol {:start "3"} [:li "baz"]])
(kiranshila.generate-test/test-hiccup
 "Example 273"
 "Foo\n- bar\n- baz\n"
 [:p "Foo"]
 [:ul [:li "bar"] [:li "baz"]])
(kiranshila.generate-test/test-hiccup
 "Example 274"
 "The number of windows in my house is\n14.  The number of doors is 6.\n"
 [:p
  "The number of windows in my house is\n14.  The number of doors is 6."])
(kiranshila.generate-test/test-hiccup
 "Example 275"
 "The number of windows in my house is\n1.  The number of doors is 6.\n"
 [:p "The number of windows in my house is"]
 [:ol [:li "The number of doors is 6."]])
(kiranshila.generate-test/test-hiccup
 "Example 276"
 "- foo\n\n- bar\n\n\n- baz\n"
 [:ul [:li [:p "foo"]] [:li [:p "bar"]] [:li [:p "baz"]]])
(kiranshila.generate-test/test-hiccup
 "Example 277"
 "- foo\n  - bar\n    - baz\n\n\n      bim\n"
 [:ul [:li "foo" [:ul [:li "bar" [:ul [:li [:p "baz"] [:p "bim"]]]]]]])
(kiranshila.generate-test/test-hiccup
 "Example 278"
 "- foo\n- bar\n\n<!-- -->\n\n- baz\n- bim\n"
 [:ul [:li "foo"] [:li "bar"]]
 [:ul [:li "baz"] [:li "bim"]])
(kiranshila.generate-test/test-hiccup
 "Example 279"
 "-   foo\n\n    notcode\n\n-   foo\n\n<!-- -->\n\n    code\n"
 [:ul [:li [:p "foo"] [:p "notcode"]] [:li [:p "foo"]]]
 [:pre [:code "code"]])
(kiranshila.generate-test/test-hiccup
 "Example 280"
 "- a\n - b\n  - c\n   - d\n  - e\n - f\n- g\n"
 [:ul
  [:li "a"]
  [:li "b"]
  [:li "c"]
  [:li "d"]
  [:li "e"]
  [:li "f"]
  [:li "g"]])
(kiranshila.generate-test/test-hiccup
 "Example 281"
 "1. a\n\n  2. b\n\n   3. c\n"
 [:ol [:li [:p "a"]] [:li [:p "b"]] [:li [:p "c"]]])
(kiranshila.generate-test/test-hiccup
 "Example 282"
 "- a\n - b\n  - c\n   - d\n    - e\n"
 [:ul [:li "a"] [:li "b"] [:li "c"] [:li "d\n- e"]])
(kiranshila.generate-test/test-hiccup
 "Example 283"
 "1. a\n\n  2. b\n\n    3. c\n"
 [:ol [:li [:p "a"]] [:li [:p "b"]]]
 [:pre [:code "3. c"]])
(kiranshila.generate-test/test-hiccup
 "Example 284"
 "- a\n- b\n\n- c\n"
 [:ul [:li [:p "a"]] [:li [:p "b"]] [:li [:p "c"]]])
(kiranshila.generate-test/test-hiccup
 "Example 285"
 "* a\n*\n\n* c\n"
 [:ul [:li [:p "a"]] [:li] [:li [:p "c"]]])
(kiranshila.generate-test/test-hiccup
 "Example 286"
 "- a\n- b\n\n  c\n- d\n"
 [:ul [:li [:p "a"]] [:li [:p "b"] [:p "c"]] [:li [:p "d"]]])
(kiranshila.generate-test/test-hiccup
 "Example 287"
 "- a\n- b\n\n  [ref]: /url\n- d\n"
 [:ul [:li [:p "a"]] [:li [:p "b"]] [:li [:p "d"]]])
(kiranshila.generate-test/test-hiccup
 "Example 288"
 "- a\n- ```\n  b\n\n\n  ```\n- c\n"
 [:ul [:li "a"] [:li [:pre [:code "b"]]] [:li "c"]])
(kiranshila.generate-test/test-hiccup
 "Example 289"
 "- a\n  - b\n\n    c\n- d\n"
 [:ul [:li "a" [:ul [:li [:p "b"] [:p "c"]]]] [:li "d"]])
(kiranshila.generate-test/test-hiccup
 "Example 290"
 "* a\n  > b\n  >\n* c\n"
 [:ul [:li "a" [:blockquote [:p "b"]]] [:li "c"]])
(kiranshila.generate-test/test-hiccup
 "Example 291"
 "- a\n  > b\n  ```\n  c\n  ```\n- d\n"
 [:ul [:li "a" [:blockquote [:p "b"]] [:pre [:code "c"]]] [:li "d"]])
(kiranshila.generate-test/test-hiccup
 "Example 292"
 "- a\n"
 [:ul [:li "a"]])
(kiranshila.generate-test/test-hiccup
 "Example 293"
 "- a\n  - b\n"
 [:ul [:li "a" [:ul [:li "b"]]]])
(kiranshila.generate-test/test-hiccup
 "Example 294"
 "1. ```\n   foo\n   ```\n\n   bar\n"
 [:ol [:li [:pre [:code "foo"]] [:p "bar"]]])
(kiranshila.generate-test/test-hiccup
 "Example 295"
 "* foo\n  * bar\n\n  baz\n"
 [:ul [:li [:p "foo"] [:ul [:li "bar"]] [:p "baz"]]])
(kiranshila.generate-test/test-hiccup
 "Example 296"
 "- a\n  - b\n  - c\n\n- d\n  - e\n  - f\n"
 [:ul
  [:li [:p "a"] [:ul [:li "b"] [:li "c"]]]
  [:li [:p "d"] [:ul [:li "e"] [:li "f"]]]])
(kiranshila.generate-test/test-hiccup
 "Example 297"
 "`hi`lo`\n"
 [:p [:code "hi"] "lo`"])
(kiranshila.generate-test/test-hiccup
 "Example 298"
 "\\!\\\"\\#\\$\\%\\&\\'\\(\\)\\*\\+\\,\\-\\.\\/\\:\\;\\<\\=\\>\\?\\@\\[\\\\\\]\\^\\_\\`\\{\\|\\}\\~\n"
 [:p "!&quot;#$%&amp;'()*+,-./:;&lt;=&gt;?@[\\]^_`{|}~"])
(kiranshila.generate-test/test-hiccup
 "Example 299"
 "\\\t\\A\\a\\ \\3\\φ\\«\n"
 [:p "\\→\\A\\a\\ \\3\\φ\\«"])
(kiranshila.generate-test/test-hiccup
 "Example 300"
 "\\*not emphasized*\n\\<br/> not a tag\n\\[not a link](/foo)\n\\`not code`\n1\\. not a list\n\\* not a list\n\\# not a heading\n\\[foo]: /url \"not a reference\"\n\\&ouml; not a character entity\n"
 [:p
  "*not emphasized*\n&lt;br/&gt; not a tag\n[not a link](/foo)\n`not code`\n1. not a list\n* not a list\n# not a heading\n[foo]: /url &quot;not a reference&quot;\n&amp;ouml; not a character entity"])
(kiranshila.generate-test/test-hiccup
 "Example 301"
 "\\\\*emphasis*\n"
 [:p "\\" [:em "emphasis"]])
(kiranshila.generate-test/test-hiccup
 "Example 302"
 "foo\\\nbar\n"
 [:p "foo" [:br] "bar"])
(kiranshila.generate-test/test-hiccup
 "Example 303"
 "`` \\[\\` ``\n"
 [:p [:code "\\[\\`"]])
(kiranshila.generate-test/test-hiccup
 "Example 304"
 "    \\[\\]\n"
 [:pre [:code "\\[\\]"]])
(kiranshila.generate-test/test-hiccup
 "Example 305"
 "~~~\n\\[\\]\n~~~\n"
 [:pre [:code "\\[\\]"]])
(kiranshila.generate-test/test-hiccup
 "Example 306"
 "<http://example.com?find=\\*>\n"
 [:p
  [:a
   {:href "http://example.com?find=%5C*"}
   "http://example.com?find=\\*"]])
(kiranshila.generate-test/test-hiccup
 "Example 307"
 "<a href=\"/bar\\/)\">\n"
 [:a {:href "/bar\\/)"}])
(kiranshila.generate-test/test-hiccup
 "Example 308"
 "[foo](/bar\\* \"ti\\*tle\")\n"
 [:p [:a {:href "/bar*", :title "ti*tle"} "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 309"
 "[foo]\n\n[foo]: /bar\\* \"ti\\*tle\"\n"
 [:p [:a {:href "/bar*", :title "ti*tle"} "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 310"
 "``` foo\\+bar\nfoo\n```\n"
 [:pre [:code {:class "language-foo+bar"} "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 311"
 "&nbsp; &amp; &copy; &AElig; &Dcaron;\n&frac34; &HilbertSpace; &DifferentialD;\n&ClockwiseContourIntegral; &ngE;\n"
 [:p "  &amp; © Æ Ď\n¾ ℋ ⅆ\n∲ ≧̸"])
(kiranshila.generate-test/test-hiccup
 "Example 312"
 "&#35; &#1234; &#992; &#0;\n"
 [:p "# Ӓ Ϡ �"])
(kiranshila.generate-test/test-hiccup
 "Example 313"
 "&#X22; &#XD06; &#xcab;\n"
 [:p "&quot; ആ ಫ"])
(kiranshila.generate-test/test-hiccup
 "Example 314"
 "&nbsp &x; &#; &#x;\n&#987654321;\n&#abcdef0;\n&ThisIsNotDefined; &hi?;\n"
 [:p
  "&amp;nbsp &amp;x; &amp;#; &amp;#x;\n&amp;#987654321;\n&amp;#abcdef0;\n&amp;ThisIsNotDefined; &amp;hi?;"])
(kiranshila.generate-test/test-hiccup
 "Example 315"
 "&copy\n"
 [:p "&amp;copy"])
(kiranshila.generate-test/test-hiccup
 "Example 316"
 "&MadeUpEntity;\n"
 [:p "&amp;MadeUpEntity;"])
(kiranshila.generate-test/test-hiccup
 "Example 317"
 "<a href=\"&ouml;&ouml;.html\">\n"
 [:a {:href "öö.html"}])
(kiranshila.generate-test/test-hiccup
 "Example 318"
 "[foo](/f&ouml;&ouml; \"f&ouml;&ouml;\")\n"
 [:p [:a {:href "/f%C3%B6%C3%B6", :title "föö"} "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 319"
 "[foo]\n\n[foo]: /f&ouml;&ouml; \"f&ouml;&ouml;\"\n"
 [:p [:a {:href "/f%C3%B6%C3%B6", :title "föö"} "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 320"
 "``` f&ouml;&ouml;\nfoo\n```\n"
 [:pre [:code {:class "language-föö"} "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 321"
 "`f&ouml;&ouml;`\n"
 [:p [:code "f&amp;ouml;&amp;ouml;"]])
(kiranshila.generate-test/test-hiccup
 "Example 322"
 "    f&ouml;f&ouml;\n"
 [:pre [:code "f&amp;ouml;f&amp;ouml;"]])
(kiranshila.generate-test/test-hiccup
 "Example 323"
 "&#42;foo&#42;\n*foo*\n"
 [:p "*foo*" [:em "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 324"
 "&#42; foo\n\n* foo\n"
 [:p "* foo"]
 [:ul [:li "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 325"
 "foo&#10;&#10;bar\n"
 [:p "foo\n\nbar"])
(kiranshila.generate-test/test-hiccup
 "Example 326"
 "&#9;foo\n"
 [:p "→foo"])
(kiranshila.generate-test/test-hiccup
 "Example 327"
 "[a](url &quot;tit&quot;)\n"
 [:p "[a](url &quot;tit&quot;)"])
(kiranshila.generate-test/test-hiccup
 "Example 328"
 "`foo`\n"
 [:p [:code "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 329"
 "`` foo ` bar ``\n"
 [:p [:code "foo ` bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 330"
 "` `` `\n"
 [:p [:code "``"]])
(kiranshila.generate-test/test-hiccup
 "Example 331"
 "`  ``  `\n"
 [:p [:code "``"]])
(kiranshila.generate-test/test-hiccup
 "Example 332"
 "` a`\n"
 [:p [:code "a"]])
(kiranshila.generate-test/test-hiccup
 "Example 333"
 "` b `\n"
 [:p [:code " b "]])
(kiranshila.generate-test/test-hiccup
 "Example 334"
 "` `\n`  `\n"
 [:p [:code " "] [:code]])
(kiranshila.generate-test/test-hiccup
 "Example 335"
 "``\nfoo\nbar  \nbaz\n``\n"
 [:p [:code "foo bar   baz"]])
(kiranshila.generate-test/test-hiccup
 "Example 336"
 "``\nfoo \n``\n"
 [:p [:code "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 337"
 "`foo   bar \nbaz`\n"
 [:p [:code "foo   bar  baz"]])
(kiranshila.generate-test/test-hiccup
 "Example 338"
 "`foo\\`bar`\n"
 [:p [:code "foo\\"] "bar`"])
(kiranshila.generate-test/test-hiccup
 "Example 339"
 "``foo`bar``\n"
 [:p [:code "foo`bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 340"
 "` foo `` bar `\n"
 [:p [:code "foo `` bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 341"
 "*foo`*`\n"
 [:p "*foo" [:code "*"]])
(kiranshila.generate-test/test-hiccup
 "Example 342"
 "[not a `link](/foo`)\n"
 [:p "[not a" [:code "link](/foo"] ")"])
(kiranshila.generate-test/test-hiccup
 "Example 343"
 "`<a href=\"`\">`\n"
 [:p [:code "&lt;a href=&quot;"] "&quot;&gt;`"])
(kiranshila.generate-test/test-hiccup
 "Example 344"
 "<a href=\"`\">`\n"
 [:p [:a {:href "`"} "`"]]
 [:a {:href "`"}])
(kiranshila.generate-test/test-hiccup
 "Example 345"
 "`<http://foo.bar.`baz>`\n"
 [:p [:code "&lt;http://foo.bar."] "baz&gt;`"])
(kiranshila.generate-test/test-hiccup
 "Example 346"
 "<http://foo.bar.`baz>`\n"
 [:p [:a {:href "http://foo.bar.%60baz"} "http://foo.bar.`baz"] "`"])
(kiranshila.generate-test/test-hiccup
 "Example 347"
 "```foo``\n"
 [:p "```foo``"])
(kiranshila.generate-test/test-hiccup
 "Example 348"
 "`foo\n"
 [:p "`foo"])
(kiranshila.generate-test/test-hiccup
 "Example 349"
 "`foo``bar``\n"
 [:p "`foo" [:code "bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 350"
 "*foo bar*\n"
 [:p [:em "foo bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 351"
 "a * foo bar*\n"
 [:p "a * foo bar*"])
(kiranshila.generate-test/test-hiccup
 "Example 352"
 "a*\"foo\"*\n"
 [:p "a*&quot;foo&quot;*"])
(kiranshila.generate-test/test-hiccup
 "Example 353"
 "* a *\n"
 [:p "* a *"])
(kiranshila.generate-test/test-hiccup
 "Example 354"
 "foo*bar*\n"
 [:p "foo" [:em "bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 355"
 "5*6*78\n"
 [:p "5" [:em "6"] "78"])
(kiranshila.generate-test/test-hiccup
 "Example 356"
 "_foo bar_\n"
 [:p [:em "foo bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 357"
 "_ foo bar_\n"
 [:p "_ foo bar_"])
(kiranshila.generate-test/test-hiccup
 "Example 358"
 "a_\"foo\"_\n"
 [:p "a_&quot;foo&quot;_"])
(kiranshila.generate-test/test-hiccup
 "Example 359"
 "foo_bar_\n"
 [:p "foo_bar_"])
(kiranshila.generate-test/test-hiccup
 "Example 360"
 "5_6_78\n"
 [:p "5_6_78"])
(kiranshila.generate-test/test-hiccup
 "Example 361"
 "пристаням_стремятся_\n"
 [:p "пристаням_стремятся_"])
(kiranshila.generate-test/test-hiccup
 "Example 362"
 "aa_\"bb\"_cc\n"
 [:p "aa_&quot;bb&quot;_cc"])
(kiranshila.generate-test/test-hiccup
 "Example 363"
 "foo-_(bar)_\n"
 [:p "foo-" [:em "(bar)"]])
(kiranshila.generate-test/test-hiccup
 "Example 364"
 "_foo*\n"
 [:p "_foo*"])
(kiranshila.generate-test/test-hiccup
 "Example 365"
 "*foo bar *\n"
 [:p "*foo bar *"])
(kiranshila.generate-test/test-hiccup
 "Example 366"
 "*foo bar\n*\n"
 [:p "*foo bar\n*"])
(kiranshila.generate-test/test-hiccup
 "Example 367"
 "*(*foo)\n"
 [:p "*(*foo)"])
(kiranshila.generate-test/test-hiccup
 "Example 368"
 "*(*foo*)*\n"
 [:p [:em "(" [:em "foo"] ")"]])
(kiranshila.generate-test/test-hiccup
 "Example 369"
 "*foo*bar\n"
 [:p [:em "foo"] "bar"])
(kiranshila.generate-test/test-hiccup
 "Example 370"
 "_foo bar _\n"
 [:p "_foo bar _"])
(kiranshila.generate-test/test-hiccup
 "Example 371"
 "_(_foo)\n"
 [:p "_(_foo)"])
(kiranshila.generate-test/test-hiccup
 "Example 372"
 "_(_foo_)_\n"
 [:p [:em "(" [:em "foo"] ")"]])
(kiranshila.generate-test/test-hiccup
 "Example 373"
 "_foo_bar\n"
 [:p "_foo_bar"])
(kiranshila.generate-test/test-hiccup
 "Example 374"
 "_пристаням_стремятся\n"
 [:p "_пристаням_стремятся"])
(kiranshila.generate-test/test-hiccup
 "Example 375"
 "_foo_bar_baz_\n"
 [:p [:em "foo_bar_baz"]])
(kiranshila.generate-test/test-hiccup
 "Example 376"
 "_(bar)_.\n"
 [:p [:em "(bar)"] "."])
(kiranshila.generate-test/test-hiccup
 "Example 377"
 "**foo bar**\n"
 [:p [:strong "foo bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 378"
 "** foo bar**\n"
 [:p "** foo bar**"])
(kiranshila.generate-test/test-hiccup
 "Example 379"
 "a**\"foo\"**\n"
 [:p "a**&quot;foo&quot;**"])
(kiranshila.generate-test/test-hiccup
 "Example 380"
 "foo**bar**\n"
 [:p "foo" [:strong "bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 381"
 "__foo bar__\n"
 [:p [:strong "foo bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 382"
 "__ foo bar__\n"
 [:p "__ foo bar__"])
(kiranshila.generate-test/test-hiccup
 "Example 383"
 "__\nfoo bar__\n"
 [:p "__\nfoo bar__"])
(kiranshila.generate-test/test-hiccup
 "Example 384"
 "a__\"foo\"__\n"
 [:p "a__&quot;foo&quot;__"])
(kiranshila.generate-test/test-hiccup
 "Example 385"
 "foo__bar__\n"
 [:p "foo__bar__"])
(kiranshila.generate-test/test-hiccup
 "Example 386"
 "5__6__78\n"
 [:p "5__6__78"])
(kiranshila.generate-test/test-hiccup
 "Example 387"
 "пристаням__стремятся__\n"
 [:p "пристаням__стремятся__"])
(kiranshila.generate-test/test-hiccup
 "Example 388"
 "__foo, __bar__, baz__\n"
 [:p [:strong "foo," [:strong "bar"] ", baz"]])
(kiranshila.generate-test/test-hiccup
 "Example 389"
 "foo-__(bar)__\n"
 [:p "foo-" [:strong "(bar)"]])
(kiranshila.generate-test/test-hiccup
 "Example 390"
 "**foo bar **\n"
 [:p "**foo bar **"])
(kiranshila.generate-test/test-hiccup
 "Example 391"
 "**(**foo)\n"
 [:p "**(**foo)"])
(kiranshila.generate-test/test-hiccup
 "Example 392"
 "*(**foo**)*\n"
 [:p [:em "(" [:strong "foo"] ")"]])
(kiranshila.generate-test/test-hiccup
 "Example 393"
 "**Gomphocarpus (*Gomphocarpus physocarpus*, syn.\n*Asclepias physocarpa*)**\n"
 [:p
  [:strong
   "Gomphocarpus ("
   [:em "Gomphocarpus physocarpus"]
   ", syn."
   [:em "Asclepias physocarpa"]
   ")"]])
(kiranshila.generate-test/test-hiccup
 "Example 394"
 "**foo \"*bar*\" foo**\n"
 [:p [:strong "foo &quot;" [:em "bar"] "&quot; foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 395"
 "**foo**bar\n"
 [:p [:strong "foo"] "bar"])
(kiranshila.generate-test/test-hiccup
 "Example 396"
 "__foo bar __\n"
 [:p "__foo bar __"])
(kiranshila.generate-test/test-hiccup
 "Example 397"
 "__(__foo)\n"
 [:p "__(__foo)"])
(kiranshila.generate-test/test-hiccup
 "Example 398"
 "_(__foo__)_\n"
 [:p [:em "(" [:strong "foo"] ")"]])
(kiranshila.generate-test/test-hiccup
 "Example 399"
 "__foo__bar\n"
 [:p "__foo__bar"])
(kiranshila.generate-test/test-hiccup
 "Example 400"
 "__пристаням__стремятся\n"
 [:p "__пристаням__стремятся"])
(kiranshila.generate-test/test-hiccup
 "Example 401"
 "__foo__bar__baz__\n"
 [:p [:strong "foo__bar__baz"]])
(kiranshila.generate-test/test-hiccup
 "Example 402"
 "__(bar)__.\n"
 [:p [:strong "(bar)"] "."])
(kiranshila.generate-test/test-hiccup
 "Example 403"
 "*foo [bar](/url)*\n"
 [:p [:em "foo" [:a {:href "/url"} "bar"]]])
(kiranshila.generate-test/test-hiccup
 "Example 404"
 "*foo\nbar*\n"
 [:p [:em "foo\nbar"]])
(kiranshila.generate-test/test-hiccup
 "Example 405"
 "_foo __bar__ baz_\n"
 [:p [:em "foo" [:strong "bar"] "baz"]])
(kiranshila.generate-test/test-hiccup
 "Example 406"
 "_foo _bar_ baz_\n"
 [:p [:em "foo" [:em "bar"] "baz"]])
(kiranshila.generate-test/test-hiccup
 "Example 407"
 "__foo_ bar_\n"
 [:p [:em [:em "foo"] "bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 408"
 "*foo *bar**\n"
 [:p [:em "foo" [:em "bar"]]])
(kiranshila.generate-test/test-hiccup
 "Example 409"
 "*foo **bar** baz*\n"
 [:p [:em "foo" [:strong "bar"] "baz"]])
(kiranshila.generate-test/test-hiccup
 "Example 410"
 "*foo**bar**baz*\n"
 [:p [:em "foo" [:strong "bar"] "baz"]])
(kiranshila.generate-test/test-hiccup
 "Example 411"
 "*foo**bar*\n"
 [:p [:em "foo**bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 412"
 "***foo** bar*\n"
 [:p [:em [:strong "foo"] "bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 413"
 "*foo **bar***\n"
 [:p [:em "foo" [:strong "bar"]]])
(kiranshila.generate-test/test-hiccup
 "Example 414"
 "*foo**bar***\n"
 [:p [:em "foo" [:strong "bar"]]])
(kiranshila.generate-test/test-hiccup
 "Example 415"
 "foo***bar***baz\n"
 [:p "foo" [:em [:strong "bar"]] "baz"])
(kiranshila.generate-test/test-hiccup
 "Example 416"
 "foo******bar*********baz\n"
 [:p "foo" [:strong [:strong [:strong "bar"]]] "***baz"])
(kiranshila.generate-test/test-hiccup
 "Example 417"
 "*foo **bar *baz* bim** bop*\n"
 [:p [:em "foo" [:strong "bar" [:em "baz"] "bim"] "bop"]])
(kiranshila.generate-test/test-hiccup
 "Example 418"
 "*foo [*bar*](/url)*\n"
 [:p [:em "foo" [:a {:href "/url"} [:em "bar"]]]])
(kiranshila.generate-test/test-hiccup
 "Example 419"
 "** is not an empty emphasis\n"
 [:p "** is not an empty emphasis"])
(kiranshila.generate-test/test-hiccup
 "Example 420"
 "**** is not an empty strong emphasis\n"
 [:p "**** is not an empty strong emphasis"])
(kiranshila.generate-test/test-hiccup
 "Example 421"
 "**foo [bar](/url)**\n"
 [:p [:strong "foo" [:a {:href "/url"} "bar"]]])
(kiranshila.generate-test/test-hiccup
 "Example 422"
 "**foo\nbar**\n"
 [:p [:strong "foo\nbar"]])
(kiranshila.generate-test/test-hiccup
 "Example 423"
 "__foo _bar_ baz__\n"
 [:p [:strong "foo" [:em "bar"] "baz"]])
(kiranshila.generate-test/test-hiccup
 "Example 424"
 "__foo __bar__ baz__\n"
 [:p [:strong "foo" [:strong "bar"] "baz"]])
(kiranshila.generate-test/test-hiccup
 "Example 425"
 "____foo__ bar__\n"
 [:p [:strong [:strong "foo"] "bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 426"
 "**foo **bar****\n"
 [:p [:strong "foo" [:strong "bar"]]])
(kiranshila.generate-test/test-hiccup
 "Example 427"
 "**foo *bar* baz**\n"
 [:p [:strong "foo" [:em "bar"] "baz"]])
(kiranshila.generate-test/test-hiccup
 "Example 428"
 "**foo*bar*baz**\n"
 [:p [:strong "foo" [:em "bar"] "baz"]])
(kiranshila.generate-test/test-hiccup
 "Example 429"
 "***foo* bar**\n"
 [:p [:strong [:em "foo"] "bar"]])
(kiranshila.generate-test/test-hiccup
 "Example 430"
 "**foo *bar***\n"
 [:p [:strong "foo" [:em "bar"]]])
(kiranshila.generate-test/test-hiccup
 "Example 431"
 "**foo *bar **baz**\nbim* bop**\n"
 [:p [:strong "foo" [:em "bar" [:strong "baz"] "bim"] "bop"]])
(kiranshila.generate-test/test-hiccup
 "Example 432"
 "**foo [*bar*](/url)**\n"
 [:p [:strong "foo" [:a {:href "/url"} [:em "bar"]]]])
(kiranshila.generate-test/test-hiccup
 "Example 433"
 "__ is not an empty emphasis\n"
 [:p "__ is not an empty emphasis"])
(kiranshila.generate-test/test-hiccup
 "Example 434"
 "____ is not an empty strong emphasis\n"
 [:p "____ is not an empty strong emphasis"])
(kiranshila.generate-test/test-hiccup
 "Example 435"
 "foo ***\n"
 [:p "foo ***"])
(kiranshila.generate-test/test-hiccup
 "Example 436"
 "foo *\\**\n"
 [:p "foo" [:em "*"]])
(kiranshila.generate-test/test-hiccup
 "Example 437"
 "foo *_*\n"
 [:p "foo" [:em "_"]])
(kiranshila.generate-test/test-hiccup
 "Example 438"
 "foo *****\n"
 [:p "foo *****"])
(kiranshila.generate-test/test-hiccup
 "Example 439"
 "foo **\\***\n"
 [:p "foo" [:strong "*"]])
(kiranshila.generate-test/test-hiccup
 "Example 440"
 "foo **_**\n"
 [:p "foo" [:strong "_"]])
(kiranshila.generate-test/test-hiccup
 "Example 441"
 "**foo*\n"
 [:p "*" [:em "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 442"
 "*foo**\n"
 [:p [:em "foo"] "*"])
(kiranshila.generate-test/test-hiccup
 "Example 443"
 "***foo**\n"
 [:p "*" [:strong "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 444"
 "****foo*\n"
 [:p "***" [:em "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 445"
 "**foo***\n"
 [:p [:strong "foo"] "*"])
(kiranshila.generate-test/test-hiccup
 "Example 446"
 "*foo****\n"
 [:p [:em "foo"] "***"])
(kiranshila.generate-test/test-hiccup
 "Example 447"
 "foo ___\n"
 [:p "foo ___"])
(kiranshila.generate-test/test-hiccup
 "Example 448"
 "foo _\\__\n"
 [:p "foo" [:em "_"]])
(kiranshila.generate-test/test-hiccup
 "Example 449"
 "foo _*_\n"
 [:p "foo" [:em "*"]])
(kiranshila.generate-test/test-hiccup
 "Example 450"
 "foo _____\n"
 [:p "foo _____"])
(kiranshila.generate-test/test-hiccup
 "Example 451"
 "foo __\\___\n"
 [:p "foo" [:strong "_"]])
(kiranshila.generate-test/test-hiccup
 "Example 452"
 "foo __*__\n"
 [:p "foo" [:strong "*"]])
(kiranshila.generate-test/test-hiccup
 "Example 453"
 "__foo_\n"
 [:p "_" [:em "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 454"
 "_foo__\n"
 [:p [:em "foo"] "_"])
(kiranshila.generate-test/test-hiccup
 "Example 455"
 "___foo__\n"
 [:p "_" [:strong "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 456"
 "____foo_\n"
 [:p "___" [:em "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 457"
 "__foo___\n"
 [:p [:strong "foo"] "_"])
(kiranshila.generate-test/test-hiccup
 "Example 458"
 "_foo____\n"
 [:p [:em "foo"] "___"])
(kiranshila.generate-test/test-hiccup
 "Example 459"
 "**foo**\n"
 [:p [:strong "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 460"
 "*_foo_*\n"
 [:p [:em [:em "foo"]]])
(kiranshila.generate-test/test-hiccup
 "Example 461"
 "__foo__\n"
 [:p [:strong "foo"]])
(kiranshila.generate-test/test-hiccup
 "Example 462"
 "_*foo*_\n"
 [:p [:em [:em "foo"]]])
(kiranshila.generate-test/test-hiccup
 "Example 463"
 "****foo****\n"
 [:p [:strong [:strong "foo"]]])
(kiranshila.generate-test/test-hiccup
 "Example 464"
 "____foo____\n"
 [:p [:strong [:strong "foo"]]])
(kiranshila.generate-test/test-hiccup
 "Example 465"
 "******foo******\n"
 [:p [:strong [:strong [:strong "foo"]]]])
(kiranshila.generate-test/test-hiccup
 "Example 466"
 "***foo***\n"
 [:p [:em [:strong "foo"]]])
(kiranshila.generate-test/test-hiccup
 "Example 467"
 "_____foo_____\n"
 [:p [:em [:strong [:strong "foo"]]]])
(kiranshila.generate-test/test-hiccup
 "Example 468"
 "*foo _bar* baz_\n"
 [:p [:em "foo _bar"] "baz_"])
(kiranshila.generate-test/test-hiccup
 "Example 469"
 "*foo __bar *baz bim__ bam*\n"
 [:p [:em "foo" [:strong "bar *baz bim"] "bam"]])
(kiranshila.generate-test/test-hiccup
 "Example 470"
 "**foo **bar baz**\n"
 [:p "**foo" [:strong "bar baz"]])
(kiranshila.generate-test/test-hiccup
 "Example 471"
 "*foo *bar baz*\n"
 [:p "*foo" [:em "bar baz"]])
(kiranshila.generate-test/test-hiccup
 "Example 472"
 "*[bar*](/url)\n"
 [:p "*" [:a {:href "/url"} "bar*"]])
(kiranshila.generate-test/test-hiccup
 "Example 473"
 "_foo [bar_](/url)\n"
 [:p "_foo" [:a {:href "/url"} "bar_"]])
(kiranshila.generate-test/test-hiccup
 "Example 474"
 "*<img src=\"foo\" title=\"*\"/>\n"
 [:p "*" [:img {:src "foo", :title "*"}]])
(kiranshila.generate-test/test-hiccup
 "Example 475"
 "**<a href=\"**\">\n"
 [:p "**" [:a {:href "**"}]]
 [:a {:href "**"}])
(kiranshila.generate-test/test-hiccup
 "Example 476"
 "__<a href=\"__\">\n"
 [:p "__" [:a {:href "__"}]]
 [:a {:href "__"}])
(kiranshila.generate-test/test-hiccup
 "Example 477"
 "*a `*`*\n"
 [:p [:em "a" [:code "*"]]])
(kiranshila.generate-test/test-hiccup
 "Example 478"
 "_a `_`_\n"
 [:p [:em "a" [:code "_"]]])
(kiranshila.generate-test/test-hiccup
 "Example 479"
 "**a<http://foo.bar/?q=**>\n"
 [:p "**a" [:a {:href "http://foo.bar/?q=**"} "http://foo.bar/?q=**"]])
(kiranshila.generate-test/test-hiccup
 "Example 480"
 "__a<http://foo.bar/?q=__>\n"
 [:p "__a" [:a {:href "http://foo.bar/?q=__"} "http://foo.bar/?q=__"]])
(kiranshila.generate-test/test-hiccup
 "Example 481"
 "[link](/uri \"title\")\n"
 [:p [:a {:href "/uri", :title "title"} "link"]])
(kiranshila.generate-test/test-hiccup
 "Example 482"
 "[link](/uri)\n"
 [:p [:a {:href "/uri"} "link"]])
(kiranshila.generate-test/test-hiccup
 "Example 483"
 "[link]()\n"
 [:p [:a {:href ""} "link"]])
(kiranshila.generate-test/test-hiccup
 "Example 484"
 "[link](<>)\n"
 [:p [:a {:href ""} "link"]])
(kiranshila.generate-test/test-hiccup
 "Example 485"
 "[link](/my uri)\n"
 [:p "[link](/my uri)"])
(kiranshila.generate-test/test-hiccup
 "Example 486"
 "[link](</my uri>)\n"
 [:p [:a {:href "/my%20uri"} "link"]])
(kiranshila.generate-test/test-hiccup
 "Example 487"
 "[link](foo\nbar)\n"
 [:p "[link](foo\nbar)"])
