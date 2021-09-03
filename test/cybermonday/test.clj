(ns cybermonday.test
  (:require  [clojure.test :refer [deftest testing is]]
             [cybermonday.ir :as ir]
             [cybermonday.core :as cm]
             [clojure.edn :as edn]
             [clojure.java.io :as io]))

(def test-document (slurp (io/resource "test-document.md")))
(def test-frontmatter-document (slurp (io/resource "test-frontmatter.md")))

(def expected-ir (edn/read-string (slurp (io/resource "expected-ir.edn"))))
(def expected-html (edn/read-string (slurp (io/resource "expected-html.edn"))))

(deftest ir
  (testing "Parsing to IR"
    (is (= expected-ir (ir/md-to-ir test-document)))))

(deftest frontmatter
  (testing "Parsing frontmatter"
    (let [{:keys [frontmatter body]} (cm/parse-md test-frontmatter-document)]
      (is (= {:title "Test Frontmatter"
              :author (list "Kiran Shila")
              :code "data"
              :tags (list "cybermonday" "unit-tests")}
             frontmatter))
      (is (= [:div {}
              [:h1 {:id "test"} "Test"]
              [:p {} "This file contains " [:em {} "markdown"]]]
             body)))))

(deftest html
  (testing "Parsing to HTML"
    (is (= expected-html (cm/parse-body test-document)))))

(cm/parse-body test-document)
