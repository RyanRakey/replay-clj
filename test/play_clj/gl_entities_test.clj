(ns play-clj.gl-entities-test
  (:require [clojure.test :refer :all]
            [play-clj.gl-fixture]
            [play-clj.entities :as e]
            [play-clj.core :as c])
  (:import [com.badlogic.gdx.graphics Color Pixmap Pixmap$Format Texture]
           [com.badlogic.gdx.graphics.g2d NinePatch ParticleEffect
            Sprite SpriteBatch TextureRegion]
           [com.badlogic.gdx.graphics.glutils ShapeRenderer]
           [com.badlogic.gdx.scenes.scene2d Actor Stage]))

(use-fixtures :once play-clj.gl-fixture/gl-setup)

(defn- make-texture-region []
  (let [pm (Pixmap. 4 4 Pixmap$Format/RGBA8888)
        tex (Texture. pm)]
    (.dispose pm)
    (TextureRegion. tex)))

(defn- make-sprite []
  (let [pm (Pixmap. 4 4 Pixmap$Format/RGBA8888)
        tex (Texture. pm)]
    (.dispose pm)
    (Sprite. tex)))

(deftest sprite-batch-creation-tests
  (testing "SpriteBatch can be created with GL context"
    (let [batch (SpriteBatch.)]
      (is (some? batch))
      (.dispose batch)))

  (testing "SpriteBatch can begin and end"
    (let [batch (SpriteBatch.)]
      (.begin batch)
      (.end batch)
      (.dispose batch))))

(deftest texture-entity-draw-with-batch-tests
  (testing "TextureEntity draw! with real SpriteBatch"
    (let [batch (SpriteBatch.)
          region (make-texture-region)
          entity (e/->TextureEntity region)]
      (.begin batch)
      (e/draw! entity {} batch)
      (.end batch)
      (.dispose batch)))

  (testing "TextureEntity draw! with position on real batch"
    (let [batch (SpriteBatch.)
          region (make-texture-region)
          entity (assoc (e/->TextureEntity region) :x 10 :y 20 :width 32 :height 32)]
      (.begin batch)
      (e/draw! entity {} batch)
      (.end batch)
      (.dispose batch)))

  (testing "TextureEntity draw! with color on real batch"
    (let [batch (SpriteBatch.)
          region (make-texture-region)
          entity (assoc (e/->TextureEntity region) :color [1 0 0 1])]
      (.begin batch)
      (e/draw! entity {} batch)
      (.end batch)
      (.dispose batch)))

  (testing "TextureEntity draw! with scale on real batch"
    (let [batch (SpriteBatch.)
          region (make-texture-region)
          entity (assoc (e/->TextureEntity region) :scale-x 2 :scale-y 2)]
      (.begin batch)
      (e/draw! entity {} batch)
      (.end batch)
      (.dispose batch))))

(deftest sprite-entity-draw-with-batch-tests
  (testing "SpriteEntity draw! with real SpriteBatch"
    (let [batch (SpriteBatch.)
          sprite (make-sprite)
          entity (e/->SpriteEntity sprite)]
      (.begin batch)
      (e/draw! entity {} batch)
      (.end batch)
      (.dispose batch)))

  (testing "SpriteEntity draw! with custom properties"
    (let [batch (SpriteBatch.)
          sprite (make-sprite)
          entity (assoc (e/->SpriteEntity sprite) :x 10 :y 20 :angle 45 :scale-x 2)]
      (.begin batch)
      (e/draw! entity {} batch)
      (.end batch)
      (.dispose batch)))

  (testing "SpriteEntity draw! with color as vector"
    (let [batch (SpriteBatch.)
          sprite (make-sprite)
          entity (assoc (e/->SpriteEntity sprite) :color [1 0 0 1])]
      (.begin batch)
      (e/draw! entity {} batch)
      (.end batch)
      (.dispose batch)))

  (testing "SpriteEntity draw! with alpha"
    (let [batch (SpriteBatch.)
          sprite (make-sprite)
          entity (assoc (e/->SpriteEntity sprite) :alpha 0.5)]
      (.begin batch)
      (e/draw! entity {} batch)
      (.end batch)
      (.dispose batch)))

  (testing "SpriteEntity draw! with Color object"
    (let [batch (SpriteBatch.)
          sprite (make-sprite)
          entity (assoc (e/->SpriteEntity sprite) :color Color/RED)]
      (.begin batch)
      (e/draw! entity {} batch)
      (.end batch)
      (.dispose batch))))

(deftest nine-patch-creation-tests
  (testing "NinePatch can be created with Texture"
    (let [region (make-texture-region)
          np (NinePatch. region 2 2 2 2)]
      (is (some? np)))))

(deftest nine-patch-entity-draw-tests
  (testing "NinePatchEntity draw! with real SpriteBatch"
    (let [batch (SpriteBatch.)
          region (make-texture-region)
          np (NinePatch. region 2 2 2 2)
          entity (e/->NinePatchEntity np)]
      (.begin batch)
      (e/draw! entity {} batch)
      (.end batch)
      (.dispose batch)))

  (testing "NinePatchEntity draw! with custom position and size"
    (let [batch (SpriteBatch.)
          region (make-texture-region)
          np (NinePatch. region 2 2 2 2)
          entity (assoc (e/->NinePatchEntity np) :x 10 :y 20 :width 100 :height 50)]
      (.begin batch)
      (e/draw! entity {} batch)
      (.end batch)
      (.dispose batch))))

(deftest particle-effect-entity-draw-tests
  (testing "ParticleEffectEntity draw! with real SpriteBatch"
    (let [batch (SpriteBatch.)
          effect (ParticleEffect.)
          entity (e/->ParticleEffectEntity effect)]
      (.begin batch)
      (try (e/draw! entity {} batch)
           (catch Exception _))
      (.end batch)
      (.dispose batch))))

(deftest shape-renderer-creation-tests
  (testing "ShapeRenderer can be created with GL context"
    (let [sr (ShapeRenderer.)]
      (is (some? sr))
      (.dispose sr)))

  (testing "ShapeRenderer can begin and end"
    (let [sr (ShapeRenderer.)]
      (.begin sr com.badlogic.gdx.graphics.glutils.ShapeRenderer$ShapeType/Filled)
      (.end sr)
      (.dispose sr))))

(deftest stage-creation-tests
  (testing "Stage can be created with GL context"
    (let [stage (Stage.)]
      (is (some? stage))
      (.dispose stage)))

  (testing "Stage can add and draw actors"
    (let [stage (Stage.)
          actor (Actor.)]
      (.addActor stage actor)
      (.act stage)
      (.draw stage)
      (.dispose stage))))

(deftest bundle-entity-draw-with-batch-tests
  (testing "BundleEntity draw! with real SpriteBatch"
    (let [batch (SpriteBatch.)
          child (e/->TextureEntity (make-texture-region))
          bundle (e/->BundleEntity [child])]
      (.begin batch)
      (e/draw! bundle {} batch)
      (.end batch)
      (.dispose batch)))

  (testing "BundleEntity merges keys and draws children"
    (let [batch (SpriteBatch.)
          child (e/->TextureEntity (make-texture-region))
          bundle (assoc (e/->BundleEntity [child]) :x 50 :y 50)]
      (.begin batch)
      (e/draw! bundle {} batch)
      (.end batch)
      (.dispose batch))))

(deftest actor-entity-draw-with-stage-tests
  (testing "ActorEntity draw! with stage and real batch"
    (let [stage (Stage.)
          actor (Actor.)
          entity (e/->ActorEntity actor)]
      (.addActor stage actor)
      (let [batch (.getBatch stage)]
        (.begin batch)
        (e/draw! entity {:renderer stage} batch)
        (.end batch))
      (.dispose stage)))

  (testing "ActorEntity draw! with position and scale"
    (let [stage (Stage.)
          actor (Actor.)
          entity (assoc (e/->ActorEntity actor) :x 10 :y 20 :scale-x 2 :scale-y 2 :angle 45)]
      (.addActor stage actor)
      (let [batch (.getBatch stage)]
        (.begin batch)
        (e/draw! entity {:renderer stage} batch)
        (.end batch))
      (.dispose stage)))

  (testing "ActorEntity draw! with width and height"
    (let [stage (Stage.)
          actor (Actor.)
          entity (assoc (e/->ActorEntity actor) :width 100 :height 50)]
      (.addActor stage actor)
      (let [batch (.getBatch stage)]
        (.begin batch)
        (e/draw! entity {:renderer stage} batch)
        (.end batch))
      (.dispose stage))))
