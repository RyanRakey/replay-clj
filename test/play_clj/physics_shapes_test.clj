(ns play-clj.physics-shapes-test
  (:require [clojure.test :refer :all]
            [play-clj.headless-fixture]
            [play-clj.g2d-physics :as p])
  (:import [com.badlogic.gdx.physics.box2d FixtureDef]))

(use-fixtures :once play-clj.headless-fixture/headless-setup)

(deftest chain-shape-macro-tests
  (testing "chain-shape macros exist"
    (is (:macro (meta (resolve 'play-clj.g2d-physics/chain-shape))))
    (is (:macro (meta (resolve 'play-clj.g2d-physics/chain-shape!))))))

(deftest circle-shape-macro-tests
  (testing "circle-shape macros exist"
    (is (:macro (meta (resolve 'play-clj.g2d-physics/circle-shape))))
    (is (:macro (meta (resolve 'play-clj.g2d-physics/circle-shape!))))))

(deftest edge-shape-macro-tests
  (testing "edge-shape macros exist"
    (is (:macro (meta (resolve 'play-clj.g2d-physics/edge-shape))))
    (is (:macro (meta (resolve 'play-clj.g2d-physics/edge-shape!))))))

(deftest polygon-shape-macro-tests
  (testing "polygon-shape macros exist"
    (is (:macro (meta (resolve 'play-clj.g2d-physics/polygon-shape))))
    (is (:macro (meta (resolve 'play-clj.g2d-physics/polygon-shape!))))))

(deftest fixture-def-tests
  (testing "fixture-def macro creates FixtureDef"
    (let [fd (p/fixture-def :density 1 :friction 0.5)]
      (is (instance? FixtureDef fd))
      (is (== 1.0 (.density fd)))
      (is (== 0.5 (.friction fd))))))

(deftest fixture-def-with-more-options-tests
  (testing "fixture-def with restitution"
    (let [fd (p/fixture-def :density 2.0 :friction 0.3 :restitution 0.8)]
      (is (instance? FixtureDef fd))
      (is (== (float 2.0) (.density fd)))
      (is (== (float 0.3) (.friction fd)))
      (is (== (float 0.8) (.restitution fd))))))

(deftest body-def-tests
  (testing "body-def macro exists"
    (is (:macro (meta (resolve 'play-clj.g2d-physics/body-def))))))

(deftest box-2d-macro-tests
  (testing "box-2d macros exist"
    (is (:macro (meta (resolve 'play-clj.g2d-physics/box-2d))))
    (is (:macro (meta (resolve 'play-clj.g2d-physics/box-2d!))))))

(deftest body-macro-tests
  (testing "body! macro exists"
    (is (:macro (meta (resolve 'play-clj.g2d-physics/body!))))))

(deftest joint-def-tests
  (testing "joint-def macro exists"
    (is (:macro (meta (resolve 'play-clj.g2d-physics/joint-def))))))

(deftest joint-macro-tests
  (testing "joint! macro exists"
    (is (:macro (meta (resolve 'play-clj.g2d-physics/joint!))))))

(deftest fixture-macro-tests
  (testing "fixture! macro exists"
    (is (:macro (meta (resolve 'play-clj.g2d-physics/fixture!))))))

(deftest add-body-function-tests
  (testing "add-body! function exists"
    (is (resolve 'play-clj.g2d-physics/add-body!))))

(deftest add-joint-function-tests
  (testing "add-joint! function exists"
    (is (resolve 'play-clj.g2d-physics/add-joint!))))

(deftest first-entity-function-tests
  (testing "first-entity function exists"
    (is (resolve 'play-clj.g2d-physics/first-entity))))

(deftest second-entity-function-tests
  (testing "second-entity function exists"
    (is (resolve 'play-clj.g2d-physics/second-entity))))

(deftest step-function-tests
  (testing "step! function exists"
    (is (resolve 'play-clj.g2d-physics/step!))))

(deftest body-position-function-tests
  (testing "body-position! function exists"
    (is (resolve 'play-clj.g2d-physics/body-position!))))

(deftest body-x-function-tests
  (testing "body-x! function exists"
    (is (resolve 'play-clj.g2d-physics/body-x!))))

(deftest body-y-function-tests
  (testing "body-y! function exists"
    (is (resolve 'play-clj.g2d-physics/body-y!))))

(deftest body-angle-function-tests
  (testing "body-angle! function exists"
    (is (resolve 'play-clj.g2d-physics/body-angle!))))
