(ns play-clj.gl-fixture
  (:require [clojure.test :refer :all])
  (:import [com.badlogic.gdx Gdx]
           [com.badlogic.gdx.backends.headless HeadlessApplication
            HeadlessApplicationConfiguration]
           [play_clj.test MockGL20]))

(def ^:private gl-app (atom nil))

(defn- dummy-listener []
  (proxy [com.badlogic.gdx.ApplicationListener] []
    (create []) (render []) (resize [w h]) (pause []) (resume []) (dispose [])))

(defn gl-setup [f]
  (let [config (HeadlessApplicationConfiguration.)]
    (set! (. config updatesPerSecond) 1)
    (reset! gl-app (HeadlessApplication. (dummy-listener) config))
    (Thread/sleep 100)
    ;; Inject mock GL into Gdx statics
    (let [mock-gl (MockGL20.)]
      (set! Gdx/gl mock-gl)
      (set! Gdx/gl20 mock-gl))
    (try
      (f)
      (finally
        (when-let [app @gl-app]
          (.exit app))
        (reset! gl-app nil)))))

(use-fixtures :once gl-setup)
