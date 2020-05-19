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
      (testing "Example 1"
        (is (= [:pre [:code "foo\tbaz\t\tbim"]]
               (first (md-to-hiccup "\tfoo\tbaz\t\tbim")))))
      (testing "Example 2"
        (is (= [:pre [:code "foo\tbaz\t\tbim"]]
               (first (md-to-hiccup "  \tfoo\tbaz\t\tbim")))))
      (testing "Example 3"
        (is (= [:pre [:code "a\ta\nὐ\ta"]]
               (first (md-to-hiccup "    a\ta\n    ὐ\ta")))))
      (testing "Example 4"
        (is (= [:ul [:li ([:p "foo"] [:p "bar"])]]
               (first (md-to-hiccup "  - foo\n\n\tbar")))))
      (testing "Example 5"
        (is (= [:ul [:li ([:p "foo"] [:pre [:code "  bar"]])]]
               (first (md-to-hiccup "- foo\n\n\t\tbar")))))
      (testing "Example 6"
        (is (= [:blockquote [:pre [:code "  foo"]]]
               (first (md-to-hiccup ">\t\tfoo")))))
      (testing "Example 7"
        (is (= [:ul [:li [:pre [:code "  foo"]]]]
               (first (md-to-hiccup "-\t\tfoo")))))
      (testing "Example 8"
        (is (= [:pre [:code "foo\nbar"]]
               (first (md-to-hiccup "    foo\n\tbar")))))
      (testing "Example 9"
        (is (= [:ul [:li "foo" [:ul [:li "bar" [:ul [:li "baz"]]]]]]
               (first (md-to-hiccup " - foo\n   - bar\n\t - baz")))))
      (testing "Example 10"
        (is (= [:h1 "Foo"]
               (first (md-to-hiccup "#\tFoo")))))
      (testing "Example 11"
        (is (= [:hr]
               (first (md-to-hiccup "*\t*\t*\t"))))))))

;; Test for insecure characters? Spec#2.3

;; 3 - Blocks and Inlines
(deftest blocks-and-inlines
  (testing "Blocks and Inlines"
    (testing "Example 12"
      (is (= [:ul ([:li "`one"] [:li "two`"])]
             (first (md-to-hiccup "- `one\n- two`")))))))

;; 4 - Leaf Blocks
(deftest thematic-breaks
  (testing "Thematic Breaks"
    (testing "Example 13"
      (is (= (list [:hr] [:hr] [:hr])
             (md-to-hiccup "***\n---\n___"))))
    (testing "Example 14"
      (is (= [:p "+++"]
             (md-to-hiccup "+++"))))
    (testing "Example 15"
      (is (= (list [:p "==="])
             (md-to-hiccup "==="))))
    (testing "Example 16"
      (is (= (list [:p "--\n**\n__"])
             (md-to-hiccup "--\n**\n__"))))
    (testing "Example 17"
      (is (= (list [:hr] [:hr] [:hr])
             (md-to-hiccup " ***\n  ***\n   ***"))))
    (testing "Example 18"
      (is (= (list [:pre [:code "***"]])
             (md-to-hiccup "    ***"))))
    (testing "Example 19"
      (is (= (list [:p "Foo\n***"])
             (md-to-hiccup "Foo\n    ***"))))
    (testing "Example 20"
      (is (= (list [:hr])
             (md-to-hiccup "_____________________________________"))))
    (testing "Example 21"
      (is (= (list [:hr])
             (md-to-hiccup " - - -"))))
    (testing "Example 22"
      (is (= (list [:hr])
             (md-to-hiccup " **  * ** * ** * **"))))
    (testing "Example 23"
      (is (= (list [:hr])
             (md-to-hiccup "-     -      -      -"))))
    (testing "Example 24"
      (is (= (list [:hr])
             (md-to-hiccup "- - - -    "))))
    (testing "Example 25"
      (is (= (list [:p "_ _ _ _"] [:p "a------"] [:p "---a---"])
             (md-to-hiccup "_ _ _ _ a\n\na------\n\n---a---"))))
    (testing "Example 26"
      (is (= (list [:p [:em "-"]])
             (md-to-hiccup " *-*"))))
    (testing "Example 27"
      (is (= (list [:ul [:li "foo"]] [:hr] [:ul [:li "bar"]])
             (md-to-hiccup "- foo\n***\n- bar"))))
    (testing "Example 28"
      (is (= (list [:p "Foo"] [:hr] [:p "bar"])
             (md-to-hiccup "Foo\n***\nbar"))))
    (testing "Example 29"
      (is (= (list [:h2 "Foo"] [:p "bar"])
             (md-to-hiccup "Foo\n---\nbar"))))
    (testing "Example 30"
      (is (= (list [:ul [:li "Foo"]] [:hr] [:ul [:li "Bar"]])
             (md-to-hiccup "* Foo\n* * *\n* Bar"))))
    (testing "Example 31"
      (is (= (list [:ul ([:li "Foo"] [:li] [:hr])])
             (md-to-hiccup "- Foo\n- * * *"))))))

(deftest atx-headings
  (testing "ATX Headings"
    (testing "Example 32"
      (is (= (list [:h1 "foo"] [:h2 "foo"] [:h3 "foo"] [:h4 "foo"] [:h5 "foo"] [:h6 "foo"])
             (md-to-hiccup "# foo\n## foo\n### foo\n#### foo\n##### foo\n###### foo"))))
    (testing "Example 33"
      (is (= (list [:p "####### foo"])
             (md-to-hiccup "####### foo"))))
    (testing "Example 34"
      (is (= (list [:p "#5 bolt"] [:p "#hashtag"])
             (md-to-hiccup "#5 bolt\n#hashtag"))))
    (testing "Example 35"
      (is (= (list [:p "## foo"])
             (md-to-hiccup "\\## foo"))))
    (testing "Example 36"
      (is (= (list [:h1 "foo" [:em "bar"] "*baz*"])
             (md-to-hiccup "# foo *bar* \\*baz\\*"))))
    (testing "Example 37"
      (is (= (list [:h1 "foo"])
             (md-to-hiccup "#                  foo                     "))))
    (testing "Example 38"
      (is (= (list [:h3 "foo"] [:h2 "foo"] [:h1 "foo"])
             (md-to-hiccup " ### foo\n  ## foo\n   # foo"))))
    (testing "Example 39"
      (is (= (list [:pre [:code "# foo"]])
             (md-to-hiccup "    # foo"))))
    (testing "Example 40"
      (is (= (list [:p "foo\n# bar"])
             (md-to-hiccup "foo\n    # bar"))))
    (testing "Example 41"
      (is (= (list [:h2 "foo"] [:h3 "bar"])
             (md-to-hiccup "## foo ##\n  ###   bar    ###"))))
    (testing "Example 42"
      (is (= (list [:h1 "foo"] [:h5 "foo"])
             (md-to-hiccup "# foo ##################################\n##### foo ##"))))
    (testing "Example 43"
      (is (= (list [:h3 "foo"])
             (md-to-hiccup "### foo ###     "))))
    (testing "Example 44"
      (is (= (list [:h3 "foo ### b"])
             (md-to-hiccup "### foo ### b"))))
    (testing "Example 45"
      (is (= (list [:h1 "foo#"])
             (md-to-hiccup "# foo#"))))
    (testing "Example 46"
      (is (= (list [:h3 "foo ###"] [:h2 "foo ###"] [:h1 "foo #"])
             (md-to-hiccup "### foo \\###\n## foo #\\##\n# foo \\#"))))
    (testing "Example 47"
      (is (= (list [:hr] [:h2 "foo"] [:hr])
             (md-to-hiccup "****\n## foo\n****"))))
    (testing "Example 48"
      (is (= (list [:p "Foo bar"] [:h1 "baz"] [:p "Bar foo"])
             (md-to-hiccup "Foo bar\n# baz\nBar foo"))))
    (testing "Example 49"
      (is (= (list [:h2] [:h1] [:h3])
             (md-to-hiccup "## \n#\n### ###"))))))

(deftest setext-headings
  (tesing))
