(ns build
  (:require [clojure.tools.build.api :as b]
            [clojure.string :as str]
            [deps-deploy.deps-deploy :as d]))

(def scm-url "git@github.com:kiranshila/cybermonday.git")
(def lib 'com.kiranshila/cybermonday)
(def version (format "0.6.%s" (b/git-count-revs nil)))
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))
(def jar-file (format "target/%s-%s.jar" (name lib) version))

(defn clean
  "Clean the build dir"
  [_]
  (b/delete {:path "target"}))

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
  "Build the thinjar"
  [_]
  (b/write-pom {:class-dir class-dir
                :lib lib
                :version version
                :basis basis
                :scm {:tag (sha nil)
                      :connection (str "scm:git:" scm-url)
                      :developerConnection (str "scm:git:" scm-url)
                      :url scm-url}
                :src-dirs ["src"]})
  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir class-dir})
  (b/jar {:class-dir class-dir
          :jar-file jar-file}))

(defn deploy
  "Send to Clojars"
  [_]
  (d/deploy {:lib lib
             :version version
             :installer :remote
             :artifact (b/resolve-path jar-file)
             :pom-file (b/pom-path {:lib lib :class-dir class-dir})}))
