(ns play-clj.physics-shapes-test
  (:require [clojure.test :refer :all]
            [play-clj.headless-fixture]
            [play-clj.g2d-physics :as p]))

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
      (is (instance? com.badlogic.gdx.physics.box2d.FixtureDef fd))
      (is (= 1.0 (.density fd)))
      (is (= 0.5 (.friction fd))))))

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
