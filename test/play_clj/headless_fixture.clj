(ns play-clj.headless-fixture
  (:require [clojure.test :refer :all])
  (:import [com.badlogic.gdx.backends.headless HeadlessApplication
            HeadlessApplicationConfiguration]))

(def ^:private headless-app (atom nil))

(defn- dummy-listener []
  (proxy [com.badlogic.gdx.ApplicationListener] []
    (create []) (render []) (resize [w h]) (pause []) (resume []) (dispose [])))

(defn headless-setup [f]
  (let [config (HeadlessApplicationConfiguration.)]
    (set! (. config updatesPerSecond) 1)
    (reset! headless-app (HeadlessApplication. (dummy-listener) config))
    ;; Give libGDX a moment to initialize Gdx statics
    (Thread/sleep 100)
    (try
      (f)
      (finally
        (when-let [app @headless-app]
          (.exit app))
        (reset! headless-app nil)))))

(use-fixtures :once headless-setup)
