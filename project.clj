(defproject play-clj "1.2.0-SNAPSHOT"
  :description "A libGDX wrapper for easy cross-platform game development"
  :url "https://github.com/oakes/play-clj"
  :license {:name "Public Domain"
            :url "http://unlicense.org/UNLICENSE"}
  :dependencies [[com.badlogicgames.gdx/gdx "1.14.2"]
                 [com.badlogicgames.gdx/gdx-box2d "1.14.2"]
                 [com.badlogicgames.gdx/gdx-bullet "1.14.2"]
                 [org.clojure/clojure "1.12.0"]]
  :source-paths ["src"]
  :java-source-paths ["src-java"]
  :javac-options ["-target" "1.8" "-source" "1.8" "-Xlint:-options"])
