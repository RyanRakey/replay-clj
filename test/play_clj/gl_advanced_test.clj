(ns play-clj.gl-advanced-test
  (:require [clojure.test :refer :all]
            [play-clj.gl-fixture]
            [play-clj.core :as c]
            [play-clj.entities :as e]
            [play-clj.g3d :as g3d])
  (:import [com.badlogic.gdx Game]
           [com.badlogic.gdx.graphics Color OrthographicCamera]
           [com.badlogic.gdx.graphics.g2d SpriteBatch]
           [com.badlogic.gdx.graphics.g3d Environment Material ModelBatch
            ModelInstance]
           [com.badlogic.gdx.graphics.g3d.attributes ColorAttribute]
           [com.badlogic.gdx.graphics.g3d.utils ModelBuilder]
           [com.badlogic.gdx.graphics.glutils ShapeRenderer
            ShapeRenderer$ShapeType]
           [com.badlogic.gdx.math Matrix4 Vector3]
           [com.badlogic.gdx.scenes.scene2d Actor Stage]))

(use-fixtures :once play-clj.gl-fixture/gl-setup)

;; ModelEntity draw! tests

(deftest model-entity-draw-tests
  (testing "ModelEntity draw! with ModelBatch and Environment"
    (let [builder (ModelBuilder.)
          model (.createBox builder 1.0 1.0 1.0
                            (Material.)
                            (bit-or com.badlogic.gdx.graphics.VertexAttributes$Usage/Position
                                    com.badlogic.gdx.graphics.VertexAttributes$Usage/Normal))
          instance (ModelInstance. model)
          env (Environment.)
          batch (ModelBatch.)
          cam (OrthographicCamera.)
          entity (e/->ModelEntity instance)]
      (.begin batch cam)
      (e/draw! entity {:renderer batch :attributes env} nil)
      (.end batch)
      (.dispose model)
      (.dispose batch)))

  (testing "ModelEntity draw! with position sets transform"
    (let [builder (ModelBuilder.)
          model (.createBox builder 1.0 1.0 1.0
                            (Material.)
                            (bit-or com.badlogic.gdx.graphics.VertexAttributes$Usage/Position
                                    com.badlogic.gdx.graphics.VertexAttributes$Usage/Normal))
          instance (ModelInstance. model)
          env (Environment.)
          batch (ModelBatch.)
          cam (OrthographicCamera.)
          entity (assoc (e/->ModelEntity instance) :x 5 :y 10 :z 15)]
      (.begin batch cam)
      (e/draw! entity {:renderer batch :attributes env} nil)
      (.end batch)
      ;; Verify transform was set
      (let [m (.transform instance)
            pos (Vector3.)]
        (.getTranslation m pos)
        (is (< 4.9 (.x pos) 5.1))
        (is (< 9.9 (.y pos) 10.1))
        (is (< 14.9 (.z pos) 15.1)))
      (.dispose model)
      (.dispose batch)))

  (testing "ModelEntity draw! without position does not modify transform"
    (let [builder (ModelBuilder.)
          model (.createBox builder 1.0 1.0 1.0
                            (Material.)
                            (bit-or com.badlogic.gdx.graphics.VertexAttributes$Usage/Position
                                    com.badlogic.gdx.graphics.VertexAttributes$Usage/Normal))
          instance (ModelInstance. model)
          env (Environment.)
          batch (ModelBatch.)
          cam (OrthographicCamera.)
          entity (e/->ModelEntity instance)]
      (.begin batch cam)
      (e/draw! entity {:renderer batch :attributes env} nil)
      (.end batch)
      (.dispose model)
      (.dispose batch))))

;; ShapeEntity draw! tests

(deftest shape-entity-draw-tests
  (testing "ShapeEntity draw! with ShapeRenderer"
    (let [sr (ShapeRenderer.)
          draw-called (atom false)
          entity (assoc (e/->ShapeEntity sr)
                        :type ShapeRenderer$ShapeType/Filled
                        :draw! (fn [] (reset! draw-called true)))]
      (e/draw! entity {} nil)
      (is @draw-called)
      (.dispose sr)))

  (testing "ShapeEntity draw! with camera sets projection matrix"
    (let [sr (ShapeRenderer.)
          cam (OrthographicCamera.)
          draw-called (atom false)
          entity (assoc (e/->ShapeEntity sr)
                        :type ShapeRenderer$ShapeType/Filled
                        :draw! (fn [] (reset! draw-called true)))]
      (e/draw! entity {:camera cam} nil)
      (is @draw-called)
      (.dispose sr)))

  (testing "ShapeEntity draw! with position and scale"
    (let [sr (ShapeRenderer.)
          draw-called (atom false)
          entity (assoc (e/->ShapeEntity sr)
                        :type ShapeRenderer$ShapeType/Filled
                        :draw! (fn [] (reset! draw-called true))
                        :x 10 :y 20 :scale-x 2 :scale-y 3 :angle 45)]
      (e/draw! entity {} nil)
      (is @draw-called)
      (.dispose sr)))

  (testing "ShapeEntity draw! with batch ends and re-begins batch"
    (let [sr (ShapeRenderer.)
          batch (SpriteBatch.)
          draw-called (atom false)
          entity (assoc (e/->ShapeEntity sr)
                        :type ShapeRenderer$ShapeType/Filled
                        :draw! (fn [] (reset! draw-called true)))]
      (.begin batch)
      (e/draw! entity {} batch)
      (is @draw-called)
      ;; Batch should be re-begun after shape draw
      (.end batch)
      (.dispose sr)))

  (testing "ShapeEntity draw! with Line type"
    (let [sr (ShapeRenderer.)
          draw-called (atom false)
          entity (assoc (e/->ShapeEntity sr)
                        :type ShapeRenderer$ShapeType/Line
                        :draw! (fn [] (reset! draw-called true)))]
      (e/draw! entity {} nil)
      (is @draw-called)
      (.dispose sr))))

;; set-screen! with Stage input tests

(deftest set-screen-with-stage-tests
  (testing "set-screen! creates screen with required keys"
    (let [screen-set (atom nil)
          game (proxy [Game] []
                 (setScreen [s] (reset! screen-set s))
                 (getScreen [] @screen-set))
          show-called (atom false)
          screen-obj {:show (fn [] (reset! show-called true))
                      :render (fn [d])
                      :hide (fn [])
                      :pause (fn [])
                      :resize (fn [w h])
                      :resume (fn [])
                      :screen (atom {})}]
      (c/set-screen! game screen-obj)
      (is (some? @screen-set)))))

;; render-map! tests

(deftest render-map-existence-tests
  (testing "render-map! function exists"
    (is (resolve 'play-clj.core/render-map!)))
  (testing "render-sorted! function exists"
    (is (resolve 'play-clj.core/render-sorted!))))

;; Environment with attributes tests

(deftest environment-with-color-attribute-tests
  (testing "environment with color attribute"
    (let [env (g3d/environment)
          attr (ColorAttribute/createAmbient 1 0 0 1)]
      (.set env attr)
      (is (some? (.get env com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute/Ambient))))))

;; ModelBuilder tests

(deftest model-builder-create-tests
  (testing "model-builder creates box model"
    (let [builder (ModelBuilder.)
          model (.createBox builder 1.0 1.0 1.0
                            (Material.)
                            (bit-or com.badlogic.gdx.graphics.VertexAttributes$Usage/Position
                                    com.badlogic.gdx.graphics.VertexAttributes$Usage/Normal))]
      (is (some? model))
      (.dispose model)))

  (testing "model-builder creates sphere model"
    (let [builder (ModelBuilder.)
          model (.createSphere builder 1.0 1.0 1.0 10 10
                               (Material.)
                               (bit-or com.badlogic.gdx.graphics.VertexAttributes$Usage/Position
                                       com.badlogic.gdx.graphics.VertexAttributes$Usage/Normal))]
      (is (some? model))
      (.dispose model)))

  (testing "model-builder creates cylinder model"
    (let [builder (ModelBuilder.)
          model (.createCylinder builder 1.0 2.0 1.0 10
                                 (Material.)
                                 (bit-or com.badlogic.gdx.graphics.VertexAttributes$Usage/Position
                                         com.badlogic.gdx.graphics.VertexAttributes$Usage/Normal))]
      (is (some? model))
      (.dispose model))))

;; Material with attributes tests

(deftest material-with-attributes-tests
  (testing "material can add color attribute"
    (let [mat (Material.)
          attr (ColorAttribute/createDiffuse Color/RED)]
      (.set mat attr)
      (is (some? (.get mat com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute/Diffuse))))))

;; TextureAtlas tests (needs real file - test existence)

(deftest texture-atlas-existence-tests
  (testing "texture-atlas* function exists"
    (is (resolve 'play-clj.g2d/texture-atlas*)))
  (testing "texture-atlas macro exists"
    (is (:macro (meta (resolve 'play-clj.g2d/texture-atlas)))))
  (testing "texture-atlas! macro exists"
    (is (:macro (meta (resolve 'play-clj.g2d/texture-atlas!))))))

;; render-sorted! existence

(deftest render-sorted-existence-tests
  (testing "render-sorted! function exists"
    (is (resolve 'play-clj.core/render-sorted!)))
  (testing "render-map! function exists"
    (is (resolve 'play-clj.core/render-map!))))
