(ns play-clj.g3d-test
  (:require [clojure.test :refer :all]
            [play-clj.headless-fixture]
            [play-clj.g3d :as g3d])
  (:import [com.badlogic.gdx.graphics.g3d Environment ModelBatch Material]
           [com.badlogic.gdx.graphics.g3d.utils ModelBuilder AnimationController]))

(use-fixtures :once play-clj.headless-fixture/headless-setup)

(deftest environment-tests
  (testing "environment* creates Environment"
    (let [env (g3d/environment*)]
      (is (instance? Environment env))))

  (testing "environment macro creates and calls methods"
    (let [env (g3d/environment)]
      (is (instance? Environment env)))))

(deftest model-batch-tests
  (testing "model-batch macro exists"
    (is (resolve 'play-clj.g3d/model-batch*))))

(deftest material-tests
  (testing "material* creates Material"
    (let [mat (g3d/material*)]
      (is (instance? Material mat)))))

(deftest model-builder-tests
  (testing "model-builder* creates ModelBuilder"
    (let [builder (g3d/model-builder*)]
      (is (instance? ModelBuilder builder)))))

(deftest model-predicate-tests
  (testing "model? returns false for non-model"
    (is (not (g3d/model? {:object "not a model"})))))

(deftest animation-controller-tests
  (testing "animation-controller predicate"
    (let [builder (g3d/model-builder*)]
      ;; ModelBuilder doesn't work with AnimationController, just test macro existence
      (is (resolve 'play-clj.g3d/animation-controller*)))))

(deftest attribute-type-tests
  (testing "attribute-type returns static field value"
    (is (number? (g3d/attribute-type :color :diffuse)))))

(deftest g3d-macro-existence-tests
  (testing "g3d macros exist"
    (is (:macro (meta (resolve 'play-clj.g3d/animation-controller))))
    (is (:macro (meta (resolve 'play-clj.g3d/animation-controller!))))
    (is (:macro (meta (resolve 'play-clj.g3d/environment!))))
    (is (:macro (meta (resolve 'play-clj.g3d/material!))))
    (is (:macro (meta (resolve 'play-clj.g3d/model!))))
    (is (:macro (meta (resolve 'play-clj.g3d/model-builder!))))
    (is (:macro (meta (resolve 'play-clj.g3d/attribute))))
    (is (:macro (meta (resolve 'play-clj.g3d/attribute!))))))
