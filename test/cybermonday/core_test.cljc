(ns cybermonday.core-test
  (:require
   [cybermonday.core :as cm]
   [cybermonday.ir :as ir]
   #?(:clj [clojure.test :as t]
      :cljs [cljs.test :as t :include-macros true])))

(t/deftest ir
  (t/testing "Parsing to IR"
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
      (t/is (= [:div {} [:markdown/heading {:level 1}
                         [:p {:class "foo" :id "heading"} "Heading"]]] (ir/md-to-ir "# <p class=\"foo\" id=heading>Heading</p>"))))
    (t/testing "Code Blocks"
      (t/is (= [:div {} [:markdown/fenced-code-block {:language "clojure"} "(+ 1 1)"]] (ir/md-to-ir "```clojure\n(+ 1 1)\n```")))
      (t/is (= [:div {} [:markdown/fenced-code-block {:language nil} "foo + bar"]] (ir/md-to-ir "```\nfoo + bar\n```")))
      (t/is (= [:div {} [:markdown/indented-code-block {} "x = x+1"]] (ir/md-to-ir "\tx = x+1\n")))
      (t/is (= [:div {} [:markdown/indented-code-block {} "x = x+1"]] (ir/md-to-ir "    x = x+1\n"))))))
