(ns nbb-runner
  (:require [clojure.string :as str]
            [clojure.test :refer [run-tests]]
            [nbb.classpath :as cp]))

(cp/add-classpath (str/join ":" ["src" "test"]))

(require '[cybermonday.core-test])

(run-tests 'cybermonday.core-test)
