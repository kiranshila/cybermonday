(ns build
  (:require [clojure.tools.build.api :as b]
            [org.corfield.build :as bb]))

(def lib 'com.kiranshila/cybermonday)
(def version (format "0.2.%s" (b/git-count-revs nil)))

(defn thinjar
  "Create thinjar"
  [opts]
  (-> opts
      (assoc :lib lib
             :version version
             :scm-url "https://github.com/kiranshila/cybermonday")
      (bb/clean)
      (bb/jar)))

(defn deploy
  "Send to Clojars"
  [opts]
  (-> opts
      (assoc :lib lib :version version)
      (bb/deploy)))
