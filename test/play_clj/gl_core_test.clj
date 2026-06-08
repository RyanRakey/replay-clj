(ns play-clj.gl-core-test
  (:require [clojure.test :refer :all]
            [play-clj.gl-fixture]
            [play-clj.core :as c])
  (:import [com.badlogic.gdx.graphics.g2d SpriteBatch]
           [com.badlogic.gdx.graphics.glutils ShapeRenderer ShapeRenderer$ShapeType]
           [com.badlogic.gdx.scenes.scene2d Stage]))

(use-fixtures :once play-clj.gl-fixture/gl-setup)

(deftest stage-creation-with-gl-tests
  (testing "stage* creates a Stage with GL context"
    (let [s (c/stage*)]
      (is (instance? Stage s))
      (.dispose s)))

  (testing "stage macro creates Stage"
    (let [s (c/stage)]
      (is (instance? Stage s))
      (.dispose s))))

(deftest shape-creation-with-gl-tests
  (testing "shape* creates a ShapeEntity with GL context"
    (let [entity (c/shape*)]
      (is (c/shape? entity))
      (is (instance? ShapeRenderer (:object entity)))))

  (testing "shape macro creates entity with draw function"
    (let [entity (c/shape :filled)]
      (is (c/shape? entity))
      (is (fn? (:draw! entity))))))

(deftest pixmap-creation-with-gl-tests
  (testing "pixmap* creates Pixmap"
    (let [pm (c/pixmap* 64 64 com.badlogic.gdx.graphics.Pixmap$Format/RGBA8888)]
      (is (instance? com.badlogic.gdx.graphics.Pixmap pm))
      (is (= 64 (.getWidth pm)))
      (c/pixmap! pm :dispose))))

(deftest clear-with-gl-tests
  (testing "clear! does not throw with GL context"
    (is (do (c/clear!) true))
    (is (do (c/clear! 0.5 0.5 1.0 1.0) true))))
