(ns cybermonday.core-test
  (:require
   [clojure.test :as t]
   [cybermonday.core :as cm]
   [cybermonday.ir :as ir]))

(t/deftest ir
  (t/testing "Parsing to IR"
    (t/testing "Paragraphs"
      (t/is (= [:div {} [:p {} "aaa"] [:p {} "bbb"]] (ir/md-to-ir "aaa\n\nbbb")))
      (t/is (= [:div {} [:p {} "aaabbb"]] (ir/md-to-ir "  aaabbb")))
      (t/is (= [:div {} [:p {} "aaa" [:br {}] "bbb"]] (ir/md-to-ir "aaa     \nbbb     "))))
    (t/testing "Block Quotes"
      (t/is (= [:div {} [:blockquote {}
                         [:markdown/heading {:level 1} "Foo"]
                         [:p {} "bar"]]]
               (ir/md-to-ir "> # Foo\n> bar")))
      (t/is (= [:div {} [:blockquote {}
                         [:p {} "foo"]
                         [:p {} "bar"]]]
               (ir/md-to-ir "> foo\n>\n> bar")))
      #_(t/is (= [:div {} [:blockquote {} ;; Softline breaks are not working in JS
                           [:blockquote {}
                            [:blockquote {}
                             [:p {} "foo\nbar\nbaz"]]]]]
                 (ir/md-to-ir ">>> foo\n> bar\n>>baz"))))
    (t/testing "List Items"
      (t/is (= [:div {} [:ol {}
                         [:markdown/ordered-list-item {} [:p {} "One"]]
                         [:markdown/ordered-list-item {} [:p {} "Two"]]
                         [:markdown/ordered-list-item {} [:p {} "Three"]]]]
               (ir/md-to-ir "1. One\n2. Two\n3. Three")))
      (t/is (= [:div {} [:ul {}
                         [:markdown/bullet-list-item {} [:p {} "One"]]
                         [:markdown/bullet-list-item {} [:p {} "Two"]]
                         [:markdown/bullet-list-item {} [:p {} "Three"]]]]
               (ir/md-to-ir "- One\n- Two\n- Three")))
      (t/is (= [:div {} [:ul {}
                         [:markdown/bullet-list-item {}
                          [:p {} "foo"]
                          [:ul {}
                           [:markdown/bullet-list-item {}
                            [:p {} "bar"]
                            [:ul {}
                             [:markdown/bullet-list-item {}
                              [:p {} "baz"]
                              [:ul {}
                               [:markdown/bullet-list-item {}
                                [:p {} "boo"]]]]]]]]]]
               (ir/md-to-ir "- foo\n  - bar\n    - baz\n      - boo")))
      (t/is (= [:div {}
                [:ul {}
                 [:markdown/bullet-list-item {}
                  [:p {} "foo"]]
                 [:markdown/bullet-list-item {}
                  [:p {} "bar"]]]
                [:ul {}
                 [:markdown/bullet-list-item {}
                  [:p {} "baz"]]]]
               (ir/md-to-ir "- foo\n- bar\n+ baz"))))
    (t/testing "Inlines"
      (t/is (= [:div {} [:p {} [:code {} "hi"] "lo`"]] (ir/md-to-ir "`hi`lo`"))))
    (t/testing "Thematic Breaks"
      (t/is (= [:div {} [:hr {}]] (ir/md-to-ir "***")))
      (t/is (= [:div {} [:hr {}]] (ir/md-to-ir "---")))
      (t/is (= [:div {} [:hr {}]] (ir/md-to-ir "___")))
      (t/is (= [:div {} [:hr {}]] (ir/md-to-ir "___________"))))
    (t/testing "Headings"
      (t/is (= [:div {} [:markdown/heading {:level 1} "Heading"]] (ir/md-to-ir "# Heading")))
      (t/is (= [:div {} [:markdown/heading {:level 2} "Heading"]] (ir/md-to-ir "## Heading")))
      (t/is (= [:div {} [:markdown/heading {:level 3} "Heading"]] (ir/md-to-ir "### Heading")))
      (t/is (= [:div {} [:markdown/heading {:level 4} "Heading"]] (ir/md-to-ir "#### Heading")))
      (t/is (= [:div {} [:markdown/heading {:level 5} "Heading"]] (ir/md-to-ir "##### Heading")))
      (t/is (= [:div {} [:markdown/heading {:level 6} "Heading"]] (ir/md-to-ir "###### Heading")))
      (t/is (= [:div {} [:markdown/heading {:level 1} [:em {} "Heading"]]] (ir/md-to-ir "# *Heading*")))
      (t/is (= [:div {} [:markdown/heading {:level 1} "Heading"]] (ir/md-to-ir "Heading\n===\n")))
      (t/is (= [:div {} [:markdown/heading {:level 2} "Heading"]] (ir/md-to-ir "Heading\n-\n")))
      (t/is (= [:div {}
                [:markdown/heading {:level 1}
                 [:p {:class "foo" :id "heading"} "Heading"]]] (ir/md-to-ir "# <p class=\"foo\" id=heading>Heading</p>"))))
    (t/testing "Code Blocks"
      (t/is (= [:div {} [:markdown/fenced-code-block {:language "clojure"} "(+ 1 1)"]] (ir/md-to-ir "```clojure\n(+ 1 1)\n```")))
      (t/is (= [:div {} [:markdown/fenced-code-block {:language nil} "foo + bar"]] (ir/md-to-ir "```\nfoo + bar\n```")))
      (t/is (= [:div {} [:markdown/indented-code-block {} "x = x+1"]] (ir/md-to-ir "\tx = x+1\n")))
      (t/is (= [:div {} [:markdown/indented-code-block {} "x = x+1"]] (ir/md-to-ir "    x = x+1\n"))))
    (t/testing "Links"
      (t/is (= [:div {}
                [:markdown/reference {:title "title" :label "foo" :url "/url"}]
                [:p {}
                 [:markdown/link-ref
                  {:reference [:markdown/reference {:title "title" :label "foo" :url "/url"}]}
                  "foo"]]]
               (ir/md-to-ir "[foo]: /url \"title\"\n\n[foo]")))
      (t/is (= [:div {} [:p {} [:a {:href "/uri" :title nil} "link"]]] (ir/md-to-ir "[link](/uri)")))
      (t/is (= [:div {} [:p {} [:a {:href "#fragment" :title "title"} "link"]]] (ir/md-to-ir "[link](#fragment \"title\")")))
      (t/is (= [:div {} [:p {} [:markdown/autolink {:href "https://kiranshila.com"}]]] (ir/md-to-ir "<https://kiranshila.com>")))
      (t/is (= [:div {} [:p {} [:markdown/mail-link {:address "me@kiranshila.com"}]]] (ir/md-to-ir "<me@kiranshila.com>"))))
    (t/testing "Images"
      (t/is (= [:div {} [:p {} [:img {:alt "foo" :src "/url" :title "title"}]]] (ir/md-to-ir "![foo](/url \"title\")")))
      #_(t/is (= [:div {} [:p {} ;; Complex image ref example broken in js
                           [:markdown/image-ref
                            {:reference
                             [:markdown/reference {:label "foo *bar*"
                                                   :title "train & tracks"
                                                   :url "train.jpg"}]}
                            "foo "
                            [:em {} "bar"]]]
                  [:markdown/reference {:label "foo *bar*"
                                        :title "train & tracks"
                                        :url "train.jpg"}]]
                 (ir/md-to-ir "![foo *bar*][]\n\n[foo *bar*]: train.jpg \"train & tracks\""))))
    #?(:clj (t/testing "Table of Contents"
              (t/is (= [:div {} [:markdown/table-of-contents {:style ""}]] (ir/md-to-ir "[TOC]")))
              (t/is (= [:div {} [:markdown/table-of-contents {:style "levels=1-3"}]] (ir/md-to-ir "[TOC levels=1-3]")))))))

(t/deftest html-lowering
  (t/testing "Parsing to HTML"
    ; TODO - Run all the commonmark tests
    (t/testing "List Items"
      (t/is (= [:div {}
                [:ul {}
                 [:li {} "one"]]
                [:p {} "two"]]
               (cm/parse-body "- one\n\n two")))
      (t/is (= [:div {}
                [:ul {}
                 [:li {}
                  [:p {} "one"]
                  [:p {} "two"]]]]
               (cm/parse-body "- one\n\n  two")))
      (t/is (= [:div {}
                [:ul {}
                 [:li {}
                  [:code {} "foo"]]]]
               (cm/parse-body "- `foo`"))))))
