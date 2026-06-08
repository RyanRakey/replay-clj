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
