(ns kiranshila.cybermonday-test
  (:require [clojure.test :refer :all]
            [kiranshila.cybermonday :refer :all]))

;; 2 - Preliminaries
(deftest prelims
  (testing "Preliminaries"
    (testing "Tabs"
      (is (= (first (md-to-hiccup "\tfoo\tbaz\t\tbim"))                     ;; Example 1
             [:pre [:code "foo\tbaz\t\tbim"]]))
      (is (= (first (md-to-hiccup "  \tfoo\tbaz\t\tbim"))                   ;; Example 2
             [:pre [:code "foo\tbaz\t\tbim"]]))
      (is (= (first (md-to-hiccup "    a\ta\n    ὐ\ta"))                    ;; Example 3
             [:pre [:code "a\ta\nὐ\ta"]]))
      (is (= (first (md-to-hiccup "  - foo\n\n\tbar"))                      ;; Example 4
             [:ul [:li ([:p foo] [:p bar])]]))
      (is (= (first (md-to-hiccup "- foo\n\n\t\tbar"))                      ;; Example 5
             [:ul [:li ([:p foo]
                        [:pre [:code "  bar"]])]]))
      (is (= (first (md-to-hiccup ">\t\tfoo"))                              ;; Example 6
             [:blockquote [:pre [:code "  foo"]]]))
      (is (= (first (md-to-hiccup "-\t\tfoo"))                              ;; Example 7
             [:ul [:li [:pre [:code "  foo"]]]]))
      (is (= (first (md-to-hiccup "    foo\n\tbar"))                        ;; Example 8
             [:pre [:code "foo\nbar"]]))
      (is (= (first (md-to-hiccup " - foo\n   - bar\n\t - baz"))            ;; Example 9
             [:ul [:li "foo" [:ul [:li "bar" [:ul [:li "baz"]]]]]]))
      (is (= (first (md-to-hiccup "#\tFoo"))                                ;; Example 10
             [:h1 "Foo"]))
      (is (= (first (md-to-hiccup "*\t*\t*\t"))                             ;; Example 11
             [:hr])))))

;; Test for insecure characters? Spec#2.3

;; 3 - Blocks and Inlines
(deftest blocks-and-inlines
  (testing "Blocks and Inlines"
    (is (= (first (md-to-hiccup "- `one\n- two`"))                          ;; Example 12
           [:ul ([:li "`one"] [:li "two`"])]))))

;; 4 - Leaf Blocks
(deftest leaf-blocks
  (testing "Leaf Blocks"
    (testing "Thematic Breaks"
      (is (= (md-to-hiccup "***\n---\n___") ;; Example 13
             (list [:hr] [:hr] [:hr])))
      (is (= (first (md-to-hiccup "+++")) ;; Example 14
             [:p "+++"]))
      (is (= (first (md-to-hiccup "===")) ;; Example 15
             [:p "==="]))
      (is (= (first (md-to-hiccup "--\n**\n__")) ;; Example 16
             [:p "--\n**\n__"]))
      (is (= (md-to-hiccup " ***\n  ***\n   ***") ;; Example 17
             (list [:hr] [:hr] [:hr])))
      (is (= (first (md-to-hiccup "    ***")) ;; Example 18
             [:pre [:code "***"]]))
      (is (= (first (md-to-hiccup "Foo\n    ***")) ;; Example 19
             [:p "Foo\n***"]))
      (is (= (first (md-to-hiccup "_____________________________________")) ;; Example 20
             [:hr]))
      (is (= (first (md-to-hiccup " - - -")) ;; Example 21
             [:hr]))
      (is (= (first (md-to-hiccup " **  * ** * ** * **")) ;; Example 22
             [:hr]))
      (is (= (first (md-to-hiccup "-     -      -      -")) ;; Example 23
             [:hr]))
      (is (= (first (md-to-hiccup "- - - -    ")) ;; Example 24
             [:hr]))
      (is (= (md-to-hiccup "_ _ _ _ a\n\na------\n\n---a---") ;; Example 25
             (list [:p "_ _ _ _"]
                   [:p "a------"]
                   [:p "---a---"])))
      (is (= (first (md-to-hiccup " *-*")) ;; Example 26
             [:p [:em "-"]]))
      (is (= (md-to-hiccup "- foo\n***\n- bar") ;; Example 27
             (list [:ul [:li "foo"]]
                   [:hr]
                   [:ul [:li "bar"]])))
      (is (= (md-to-hiccup "Foo\n***\nbar") ;; Example 28
             (list [:p "Foo"]
                   [:hr]
                   [:p "bar"])))
      (is (= (md-to-hiccup "Foo\n---\nbar") ;; Example 29
             (list [:h2 "Foo"]
                   [:p "bar"])))
      (is (= (md-to-hiccup "* Foo\n* * *\n* Bar") ;; Example 30
             (list [:ul [:li "Foo"]]
                   [:hr]
                   [:ul [:li "Bar"]])))
      (is (= (first (md-to-hiccup "- Foo\n- * * *")) ;; Example 31
             [:ul ([:li "Foo"]
                   [:li]
                   [:hr])])))))
