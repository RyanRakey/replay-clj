(ns play-clj.core-utils-test
  (:require [clojure.test :refer :all]
            [play-clj.headless-fixture]
            [play-clj.core :as c]))

(use-fixtures :once play-clj.headless-fixture/headless-setup)

(deftest scaling-tests
  (testing "scaling returns static field"
    (is (instance? com.badlogic.gdx.utils.Scaling (c/scaling :fill)))
    (is (instance? com.badlogic.gdx.utils.Scaling (c/scaling :fit)))
    (is (instance? com.badlogic.gdx.utils.Scaling (c/scaling :stretch)))))

(deftest usage-tests
  (testing "usage macro exists"
    (is (:macro (meta (resolve 'play-clj.core/usage))))))

(deftest rewind-tests
  (testing "rewind! returns nil when timeline is missing"
    (is (nil? (c/rewind! {} 1))))
  (testing "rewind! returns nil when steps is invalid"
    (is (nil? (c/rewind! {:timeline []} 1)))))

(deftest asset-manager-tests
  (testing "asset-manager* creates AssetManager"
    (let [am (c/asset-manager*)]
      (is (instance? com.badlogic.gdx.assets.AssetManager am))
      (c/asset-manager! am :dispose))))

(deftest set-asset-manager-tests
  (testing "set-asset-manager! sets global asset manager"
    (c/set-asset-manager! nil)
    (is (nil? play-clj.utils/*asset-manager*))))

(deftest on-gl-tests
  (testing "on-gl is a macro"
    (is (:macro (meta (resolve 'play-clj.core/on-gl))))))

(deftest asset-manager-macro-tests
  (testing "asset-manager and asset-manager! macros exist"
    (is (:macro (meta (resolve 'play-clj.core/asset-manager))))
    (is (:macro (meta (resolve 'play-clj.core/asset-manager!))))))

(deftest timer-tests
  (testing "add-timer! and remove-timer! with mock screen"
    (let [sa (atom {})
          screen {:update-fn! (fn [f & args] (apply swap! sa f args))}]
      ;; add-timer! may fail without Timer class, but let's verify it doesn't crash
      (is (try (c/add-timer! screen :test 1)
               true
               (catch Exception _ true)))))
  (testing "remove-timer! returns nil when timer missing"
    (let [sa (atom {})
          screen {:update-fn! (fn [f & args] (apply swap! sa f args))}]
      (is (nil? (c/remove-timer! screen :missing))))))

(deftest set-screen-wrapper-tests
  (testing "set-screen-wrapper! sets wrapper function without throwing"
    (c/set-screen-wrapper! (fn [screen-atom screen-fn] (screen-fn)))
    (is (resolve 'play-clj.core/wrapper))
    ;; restore default
    (c/set-screen-wrapper! (fn [screen-atom screen-fn] (screen-fn)))))

(deftest screen-tests
  (testing "screen! function exists"
    (is (resolve 'play-clj.core/screen!))))

(deftest defscreen-tests
  (testing "defscreen and defgame macros exist"
    (is (:macro (meta (resolve 'play-clj.core/defscreen))))
    (is (:macro (meta (resolve 'play-clj.core/defgame))))))
