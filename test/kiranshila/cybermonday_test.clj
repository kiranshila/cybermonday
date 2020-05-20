(ns kiranshila.cybermonday-test
  (:require [clojure.test :refer :all]
            [kiranshila.cybermonday :refer :all]))

(defmacro test-hiccup [name md-str & hiccup]
  `(testing ~name (is (= '~hiccup
                         (md-to-hiccup ~md-str)))))

;; 2 - Preliminaries
(deftest prelims
  (testing "Preliminaries"
    (testing "Tabs"
      (test-hiccup "Example 1"
                   "\tfoo\tbaz\t\tbim"
                   [:pre [:code "foo\tbaz\t\tbim"]])
      (test-hiccup "Example 2"
                   "  \tfoo\tbaz\t\tbim"
                   [:pre [:code "foo\tbaz\t\tbim"]])
      (test-hiccup "Example 3"
                   "    a\ta\n    ὐ\ta"
                   [:pre [:code "a\ta\nὐ\ta"]])
      (test-hiccup "Example 4"
                   "  - foo\n\n\tbar"
                   [:ul [:li ([:p "foo"] [:p "bar"])]])
      (test-hiccup "Example 5"
                   "- foo\n\n\t\tbar"
                   [:ul [:li ([:p "foo"] [:pre [:code "  bar"]])]])
      (test-hiccup "Example 6"
                   ">\t\tfoo"
                   [:blockquote [:pre [:code "  foo"]]])
      (test-hiccup "Example 7"
                   "-\t\tfoo"
                   [:ul [:li [:pre [:code "  foo"]]]])
      (test-hiccup "Example 8"
                   "    foo\n\tbar"
                   [:pre [:code "foo\nbar"]])
      (test-hiccup "Example 9"
                   " - foo\n   - bar\n\t - baz"
                   [:ul [:li "foo" [:ul [:li "bar" [:ul [:li "baz"]]]]]])
      (test-hiccup "Example 10"
                   "#\tFoo"
                   [:h1 "Foo"])
      (test-hiccup "Example 11"
                   "*\t*\t*\t"
                   [:hr]))))

;; Test for insecure characters? Spec#2.3

;; 3 - Blocks and Inlines
(deftest blocks-and-inlines
  (testing "Blocks and Inlines"
    (test-hiccup "Example 12"
                 "- `one\n- two`"
                 [:ul ([:li "`one"] [:li "two`"])])))

;; 4 - Leaf Blocks
(deftest thematic-breaks
  (testing "Thematic Breaks"
    (test-hiccup "Example 13"
                 "***\n---\n___"
                 [:hr] [:hr] [:hr])
    (test-hiccup "Example 14"
                 "+++"
                 [:p "+++"])
    (test-hiccup "Example 15"
                 "==="
                 [:p "==="])
    (test-hiccup "Example 16"
                 "--\n**\n__"
                 [:p "--\n**\n__"])
    (test-hiccup "Example 17"
                 " ***\n  ***\n   ***"
                 [:hr] [:hr] [:hr])
    (test-hiccup "Example 18"
                 "    ***"
                 [:pre [:code "***"]])
    (test-hiccup "Example 19"
                 "Foo\n    ***"
                 [:p "Foo\n***"])
    (test-hiccup "Example 20"
                 "_____________________________________"
                 [:hr])
    (test-hiccup "Example 21"
                 " - - -"
                 [:hr])
    (test-hiccup "Example 22"
                 " **  * ** * ** * **"
                 [:hr])
    (test-hiccup "Example 23"
                 "-     -      -      -"
                 [:hr])
    (test-hiccup "Example 24"
                 "- - - -    "
                 [:hr])
    (test-hiccup "Example 25"
                 "_ _ _ _ a\n\na------\n\n---a---"
                 [:p "_ _ _ _"] [:p "a------"] [:p "---a---"])
    (test-hiccup "Example 26"
                 " *-*"
                 [:p [:em "-"]])
    (test-hiccup "Example 27"
                 "- foo\n***\n- bar"
                 [:ul [:li "foo"]] [:hr] [:ul [:li "bar"]])
    (test-hiccup "Example 28"
                 "Foo\n***\nbar"
                 [:p "Foo"] [:hr] [:p "bar"])
    (test-hiccup "Example 29"
                 "Foo\n---\nbar"
                 [:h2 "Foo"] [:p "bar"])
    (test-hiccup "Example 30"
                 "* Foo\n* * *\n* Bar"
                 [:ul [:li "Foo"]] [:hr] [:ul [:li "Bar"]])
    (test-hiccup "Example 31"
                 "- Foo\n- * * *"
                 [:ul ([:li "Foo"] [:li] [:hr])])))

(deftest atx-headings
  (testing "ATX Headings"
    (test-hiccup "Example 32"
                 "# foo\n## foo\n### foo\n#### foo\n##### foo\n###### foo"
                 [:h1 "foo"] [:h2 "foo"] [:h3 "foo"] [:h4 "foo"] [:h5 "foo"] [:h6 "foo"])
    (test-hiccup "Example 33"
                 "####### foo"
                 [:p "####### foo"])
    (test-hiccup "Example 34"
                 "#5 bolt\n#hashtag"
                 [:p "#5 bolt"] [:p "#hashtag"])
    (test-hiccup "Example 35"
                 "\\## foo"
                 [:p "## foo"])
    (test-hiccup "Example 36"
                 "# foo *bar* \\*baz\\*"
                 [:h1 "foo" [:em "bar"] "*baz*"])
    (test-hiccup "Example 37"
                 "#                  foo                     "
                 [:h1 "foo"])
    (test-hiccup "Example 38"
                 " ### foo\n  ## foo\n   # foo"
                 [:h3 "foo"] [:h2 "foo"] [:h1 "foo"])
    (test-hiccup "Example 39"
                 "    # foo"
                 [:pre [:code "# foo"]])
    (test-hiccup "Example 40"
                 "foo\n    # bar"
                 [:p "foo\n# bar"])
    (test-hiccup "Example 41"
                 "## foo ##\n  ###   bar    ###"
                 [:h2 "foo"] [:h3 "bar"])
    (test-hiccup "Example 42"
                 "# foo ##################################\n##### foo ##"
                 [:h1 "foo"] [:h5 "foo"])
    (test-hiccup "Example 43"
                 "### foo ###     "
                 [:h3 "foo"])
    (test-hiccup "Example 44"
                 "### foo ### b"
                 [:h3 "foo ### b"])
    (test-hiccup "Example 45"
                 "# foo#"
                 [:h1 "foo#"])
    (test-hiccup "Example 46"
                 "### foo \\###\n## foo #\\##\n# foo \\#"
                 [:h3 "foo ###"] [:h2 "foo ###"] [:h1 "foo #"])
    (test-hiccup "Example 47"
                 "****\n## foo\n****"
                 [:hr] [:h2 "foo"] [:hr])
    (test-hiccup "Example 48"
                 "Foo bar\n# baz\nBar foo"
                 [:p "Foo bar"] [:h1 "baz"] [:p "Bar foo"])
    (test-hiccup "Example 49"
                 "## \n#\n### ###"
                 [:h2] [:h1] [:h3])))

(deftest setext-headings
  (testing "Setext Headings"
    (test-hiccup "Example 50"
                 "Foo *bar*\n=========\n\nFoo *bar*\n---------"
                 [:h1 "Foo" [:em "bar"]] [:h2 "Foo" [:em "bar"]])
    (test-hiccup "Example 51"
                 "Foo *bar\r\nbaz*\r\n===="
                 [:h1 "Foo " [:em "bar\nbaz"]])
    (test-hiccup "Example 52"
                 "  Foo *bar\r\nbaz*\t\r\n====\r\n"
                 [:h1 "Foo " [:em "bar\nbaz"]])
    (test-hiccup "Example 53"
                 "Foo\r\n-------------------------\r\n\r\nFoo\r\n="
                 [:h2 "Foo"] [:h1 "Foo"])
    (test-hiccup "Example 54"
                 "   Foo\r\n---\r\n\r\n  Foo\r\n-----\r\n\r\n  Foo\r\n  ==="
                 [:h2 "Foo"] [:h2 "Foo"] [:h1 "Foo"])
    (test-hiccup "Example 55"
                 "    Foo\r\n    ---\r\n\r\n    Foo\r\n---"
                 [:pre [:code "Foo\n---\n\nFoo"]] [:hr])
    (test-hiccup "Example 56"
                 "Foo\r\n   ----      "
                 [:h2 "Foo"])
    (test-hiccup "Example 57"
                 "Foo\r\n    ---"
                 [:p "Foo\n---"])
    (test-hiccup "Example 58"
                 "Foo\r\n= =\r\n\r\nFoo\r\n--- -"
                 [:p "Foo\n= ="] [:p "Foo"] [:hr])
    (test-hiccup "Example 59"
                 [:h2 "Foo"])
    (test-hiccup "Example 60"
                 [:h2 "Foo\\"])
    (test-hiccup "Example 61"
                 [:h2 "`Foo"] [:p "`"] [:h2 "&lt;a title=&quot;a lot"] [:p "of dashes&quot;/&gt;"])
    (test-hiccup "Example 62"
                 [:blockquote [:p "Foo"]] [:hr])
    (test-hiccup "Example 63"
                 [:blockquote [:p "foo\nbar\n==="]])
    (test-hiccup "Example 64"
                 [:ul [:li "Foo"]] [:hr])
    (test-hiccup "Example 65"
                 [:h2 "Foo\nBar"])
    (test-hiccup "Example 66"
                 [:hr] [:h2 "Foo"]  [:h2 "Bar"] [:p "Baz"])
    (test-hiccup "Example 67"
                 [:p "===="])
    (test-hiccup "Example 68"
                 [:hr] [:hr])
    (test-hiccup "Example 69"
                 [:ul [:li "foo"]] [:hr])
    (test-hiccup "Example 70"
                 [:pre [:code "foo"]] [:hr])
    (test-hiccup "Example 71"
                 [:blockquote [:p "foo"]] [:hr])
    (test-hiccup "Example 72"
                 [:h2 "&gt; foo"])
    (test-hiccup "Example 73"
                 [:p "Foo"] [:h2 "bar"] [:p "baz"])
    (test-hiccup "Example 74"
                 [:p "Foo\nbar"] [:hr] [:p "baz"])
    (test-hiccup "Example 75"
                 [:p "Foo\nbar"] [:hr] [:p "baz"])
    (test-hiccup "Example 76"
                 [:p "Foo\nbar\n---\nbaz"])))
