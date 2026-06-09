(ns build
  (:require [clojure.tools.build.api :as b]))

(def lib 'play-clj/replay-clj)
(def version "1.2.0-SNAPSHOT")
(def class-dir "classes")
(def jar-file (format "target/%s-%s.jar" (name lib) version))
(def java-src-dirs ["src-java"])
(def basis (b/create-basis {:project "deps.edn"}))

(defn clean [_]
  (b/delete {:path "target"})
  (b/delete {:path class-dir}))

(defn javac [_]
  (b/javac {:src-dirs java-src-dirs
            :class-dir class-dir
            :basis basis
            :javac-opts ["-target" "1.8" "-source" "1.8" "-Xlint:-options"]}))

(defn jar [_]
  (javac nil)
  (b/write-pom {:class-dir class-dir
                :lib lib
                :version version
                :basis basis
                :src-dirs ["src"]})
  (b/copy-dir {:src-dirs ["src" "classes"]
               :target-dir class-dir})
  (b/jar {:class-dir class-dir
          :jar-file jar-file}))

(defn install [_]
  (jar nil)
  (b/install {:basis basis
              :lib lib
              :version version
              :class-dir class-dir
              :jar-file jar-file}))
