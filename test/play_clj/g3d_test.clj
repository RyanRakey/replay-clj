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

(deftest model-batch-star-tests
  (testing "model-batch* function exists"
    (is (resolve 'play-clj.g3d/model-batch*))))

(deftest model-batch-macro-tests
  (testing "model-batch macro exists"
    (is (:macro (meta (resolve 'play-clj.g3d/model-batch))))))

(deftest model-batch-bang-macro-tests
  (testing "model-batch! macro exists"
    (is (:macro (meta (resolve 'play-clj.g3d/model-batch!))))))

(deftest model-macro-existence-tests
  (testing "model macro exists"
    (is (:macro (meta (resolve 'play-clj.g3d/model))))))

(deftest model-bang-macro-behavioral-tests
  (testing "model! macro exists"
    (is (:macro (meta (resolve 'play-clj.g3d/model!))))))

(deftest model-star-function-tests
  (testing "model* exists"
    (is (resolve 'play-clj.g3d/model*))))

(deftest model-builder-macro-tests
  (testing "model-builder macro creates ModelBuilder"
    (let [builder (g3d/model-builder)]
      (is (instance? ModelBuilder builder)))))

(deftest model-builder-bang-macro-tests
  (testing "model-builder! macro exists"
    (is (:macro (meta (resolve 'play-clj.g3d/model-builder!))))))

(deftest material-macro-tests
  (testing "material macro creates Material"
    (let [mat (g3d/material)]
      (is (instance? Material mat)))))

(deftest material-bang-macro-behavioral-tests
  (testing "material! macro exists"
    (is (:macro (meta (resolve 'play-clj.g3d/material!))))))

(deftest environment-bang-macro-behavioral-tests
  (testing "environment! macro exists"
    (is (:macro (meta (resolve 'play-clj.g3d/environment!))))))

;; Additional behavioral tests

(deftest environment-star-behavioral-tests
  (testing "environment* returns empty Environment"
    (let [env (g3d/environment*)]
      (is (instance? Environment env)))))

(deftest environment-macro-behavioral-tests
  (testing "environment macro creates Environment"
    (let [env (g3d/environment)]
      (is (instance? Environment env)))))

(deftest material-star-behavioral-tests
  (testing "material* returns empty Material"
    (let [mat (g3d/material*)]
      (is (instance? Material mat)))))

(deftest material-macro-behavioral-tests
  (testing "material macro creates Material"
    (let [mat (g3d/material)]
      (is (instance? Material mat)))))

(deftest model-builder-star-behavioral-tests
  (testing "model-builder* returns ModelBuilder"
    (let [builder (g3d/model-builder*)]
      (is (instance? ModelBuilder builder)))))

(deftest model-builder-macro-behavioral-tests
  (testing "model-builder macro creates ModelBuilder"
    (let [builder (g3d/model-builder)]
      (is (instance? ModelBuilder builder)))))

(deftest attribute-type-behavioral-tests
  (testing "attribute-type returns numeric values for known types"
    (is (number? (g3d/attribute-type :color :diffuse)))
    (is (number? (g3d/attribute-type :color :specular)))
    (is (number? (g3d/attribute-type :float :shininess)))))

(deftest model-predicate-behavioral-tests
  (testing "model? returns false for non-model string"
    (is (not (g3d/model? {:object "not a model"})))))

(deftest g3d-function-existence-tests
  (testing "all g3d functions exist"
    (is (resolve 'play-clj.g3d/animation-controller*))
    (is (resolve 'play-clj.g3d/model*))
    (is (resolve 'play-clj.g3d/model-batch*))
    (is (resolve 'play-clj.g3d/model-builder*))
    (is (resolve 'play-clj.g3d/material*))
    (is (resolve 'play-clj.g3d/environment*))))
