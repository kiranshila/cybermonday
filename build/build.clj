(ns build
  (:require [clojure.tools.build.api :as b]
            [org.corfield.build :as bb]
            [clojure.string :as str]))

(def scm-url "git@github.com:kiranshila/cybermonday.git")

(def lib 'com.kiranshila/cybermonday)
(def version (format "0.5.%s" (b/git-count-revs nil)))

(defn sha
  [{:keys [dir path] :or {dir "."}}]
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
             :scm {:tag (sha nil)
                   :connection (str "scm:git:" scm-url)
                   :developerConnection (str "scm:git:" scm-url)
                   :url scm-url})
      (bb/clean)
      (bb/jar)))

(defn deploy
  "Send to Clojars"
  [opts]
  (-> opts
      (assoc :lib lib :version version)
      (bb/deploy)))
