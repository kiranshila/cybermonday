(ns build
  (:require [clojure.tools.build.api :as b]
            [org.corfield.build :as bb]
            [clojure.string :as str]))

(def lib 'com.kiranshila/cybermonday)
(def version (format "0.2.%s" (b/git-count-revs nil)))

(defn sha
  [{:keys [dir path] :or {dir "."} :as params}]
  (-> {:command-args (cond-> ["git" "rev-parse" "HEAD"]
                       path (conj "--" path))
       :dir (.getPath (b/resolve-path dir))
       :out :capture}
      b/process
      :out
      str/trim))

(defn thinjar
  "Create thinjar"
  [opts]
  (-> opts
      (assoc :lib lib
             :version version
             :tag (sha nil)
             :scm-url "https://github.com/kiranshila/cybermonday")
      (bb/clean)
      (bb/jar)))

(defn deploy
  "Send to Clojars"
  [opts]
  (-> opts
      (assoc :lib lib :version version)
      (bb/deploy)))
