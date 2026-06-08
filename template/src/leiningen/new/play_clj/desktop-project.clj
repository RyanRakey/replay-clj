(defproject {{app-name}} "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  
  :dependencies [[com.badlogicgames.gdx/gdx "1.14.2"]
                 [com.badlogicgames.gdx/gdx-backend-lwjgl3 "1.14.2"]
                 [com.badlogicgames.gdx/gdx-box2d "1.14.2"]
                 [com.badlogicgames.gdx/gdx-box2d-platform "1.14.2"
                  :classifier "natives-desktop"]
                 [com.badlogicgames.gdx/gdx-bullet "1.14.2"]
                 [com.badlogicgames.gdx/gdx-bullet-platform "1.14.2"
                  :classifier "natives-desktop"]
                 [com.badlogicgames.gdx/gdx-platform "1.14.2"
                  :classifier "natives-desktop"]
                 [org.clojure/clojure "1.12.0"]
                 [play-clj "1.2.0-SNAPSHOT"]]
   
  :source-paths ["src" "src-common"]
  :javac-options ["-target" "1.8" "-source" "1.8" "-Xlint:-options"]
  :aot [{{desktop-namespace}}]
  :main {{desktop-namespace}})
