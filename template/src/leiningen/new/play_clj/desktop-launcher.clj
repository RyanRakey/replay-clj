(ns {{desktop-namespace}}
  (:require [{{namespace}} :refer :all])
  (:import [com.badlogic.gdx.backends.lwjgl3 Lwjgl3Application
            Lwjgl3ApplicationConfiguration])
  (:gen-class))

(defn -main
  []
  (let [config (doto (Lwjgl3ApplicationConfiguration.)
                 (.setTitle "{{app-name}}")
                 (.setWindowedMode 800 600))]
    (Lwjgl3Application. {{game-name}} config)))
