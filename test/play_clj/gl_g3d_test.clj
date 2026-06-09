(ns play-clj.gl-g3d-test
  (:require [clojure.test :refer :all]
            [play-clj.gl-fixture]
            [play-clj.g3d :as g3d])
  (:import [com.badlogic.gdx.graphics.g3d Environment Material ModelBatch]
           [com.badlogic.gdx.graphics.g3d.utils ModelBuilder]))

(use-fixtures :once play-clj.gl-fixture/gl-setup)

(deftest environment-full-behavioral-tests
  (testing "environment* creates Environment"
    (let [env (g3d/environment*)]
      (is (instance? Environment env))))

  (testing "environment macro creates Environment"
    (let [env (g3d/environment)]
      (is (instance? Environment env)))))

(deftest material-full-behavioral-tests
  (testing "material* creates Material"
    (let [mat (g3d/material*)]
      (is (instance? Material mat))))

  (testing "material macro creates Material"
    (let [mat (g3d/material)]
      (is (instance? Material mat))))

  (testing "material! macro exists"
    (is (:macro (meta (resolve 'play-clj.g3d/material!))))))

(deftest model-builder-full-behavioral-tests
  (testing "model-builder* creates ModelBuilder"
    (let [builder (g3d/model-builder*)]
      (is (instance? ModelBuilder builder))))

  (testing "model-builder macro creates ModelBuilder"
    (let [builder (g3d/model-builder)]
      (is (instance? ModelBuilder builder))))

  (testing "model-builder! macro exists"
    (is (:macro (meta (resolve 'play-clj.g3d/model-builder!))))))

(deftest model-batch-behavioral-tests
  (testing "model-batch* function exists"
    (is (resolve 'play-clj.g3d/model-batch*)))
  (testing "model-batch macro exists"
    (is (:macro (meta (resolve 'play-clj.g3d/model-batch)))))
  (testing "model-batch! macro exists"
    (is (:macro (meta (resolve 'play-clj.g3d/model-batch!))))))

(deftest model-behavioral-tests
  (testing "model* function exists"
    (is (resolve 'play-clj.g3d/model*)))
  (testing "model macro exists"
    (is (:macro (meta (resolve 'play-clj.g3d/model)))))
  (testing "model! macro exists"
    (is (:macro (meta (resolve 'play-clj.g3d/model!)))))
  (testing "model? returns false for non-model"
    (is (not (g3d/model? {:object "not a model"})))))

(deftest animation-controller-behavioral-tests
  (testing "animation-controller* function exists"
    (is (resolve 'play-clj.g3d/animation-controller*)))
  (testing "animation-controller macro exists"
    (is (:macro (meta (resolve 'play-clj.g3d/animation-controller)))))
  (testing "animation-controller! macro exists"
    (is (:macro (meta (resolve 'play-clj.g3d/animation-controller!))))))

(deftest attribute-type-full-behavioral-tests
  (testing "attribute-type returns values for various attribute types"
    (is (number? (g3d/attribute-type :color :diffuse)))
    (is (number? (g3d/attribute-type :color :specular)))
    (is (number? (g3d/attribute-type :color :emissive)))
    (is (number? (g3d/attribute-type :color :ambient)))
    (is (number? (g3d/attribute-type :float :shininess)))
    (is (number? (g3d/attribute-type :float :alpha-test)))))

(deftest attribute-behavioral-tests
  (testing "attribute macro exists"
    (is (:macro (meta (resolve 'play-clj.g3d/attribute)))))
  (testing "attribute! macro exists"
    (is (:macro (meta (resolve 'play-clj.g3d/attribute!))))))

(deftest environment-bang-behavioral-tests
  (testing "environment! macro exists"
    (is (:macro (meta (resolve 'play-clj.g3d/environment!))))))
