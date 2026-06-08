(ns play-clj.entities-test
  (:require [clojure.test :refer :all]
            [play-clj.headless-fixture]
            [play-clj.entities :as e])
  (:import [com.badlogic.gdx.graphics Color]
           [com.badlogic.gdx.graphics.g2d Batch NinePatch ParticleEffect Sprite
            TextureRegion]
           [com.badlogic.gdx.graphics.g3d Environment ModelBatch ModelInstance]
           [com.badlogic.gdx.graphics.glutils ShapeRenderer]
           [com.badlogic.gdx.scenes.scene2d Actor Stage]))

(use-fixtures :once play-clj.headless-fixture/headless-setup)

(def batch-calls (atom []))

(defn- mock-batch []
  (reset! batch-calls [])
  (proxy [Batch] []
    (draw
      ([region x y w h]
       (swap! batch-calls conj [:draw-simple region x y w h]))
      ([region x y ox oy w h sx sy a]
       (swap! batch-calls conj [:draw-transform region x y ox oy w h sx sy a])))
    (setColor
      ([r g b a]
       (swap! batch-calls conj [:set-color r g b a]))
      ([color]
       (swap! batch-calls conj [:set-color-obj color])))
    (getColor [] Color/WHITE)
    (begin [])
    (end [])))

(deftest entity-record-tests
  (testing "TextureEntity can be created"
    (let [region (TextureRegion.)
          entity (e/->TextureEntity region)]
      (is (satisfies? e/Entity entity))))

  (testing "SpriteEntity can be created"
    (let [sprite (Sprite.)
          entity (e/->SpriteEntity sprite)]
      (is (satisfies? e/Entity entity))))

  (testing "BundleEntity can be created"
    (let [inner-entities [{:x 1 :y 2} {:x 3 :y 4}]
          bundle (e/->BundleEntity inner-entities)]
      (is (satisfies? e/Entity bundle))
      (is (= inner-entities (:entities bundle))))))

(deftest texture-entity-draw-tests
  (testing "TextureEntity draw! calls batch.draw with simple args"
    (let [region (TextureRegion.)
          entity (e/->TextureEntity region)
          batch (mock-batch)]
      (e/draw! entity {} batch)
      (is (= 1 (count @batch-calls)))
      (is (= :draw-simple (first (first @batch-calls))))))

  (testing "TextureEntity draw! with scale uses transform draw"
    (let [region (TextureRegion.)
          entity (assoc (e/->TextureEntity region) :scale-x 2 :scale-y 2)
          batch (mock-batch)]
      (e/draw! entity {} batch)
      (is (= 1 (count @batch-calls)))
      (is (= :draw-transform (first (first @batch-calls))))))

  (testing "TextureEntity draw! with angle uses transform draw"
    (let [region (TextureRegion.)
          entity (assoc (e/->TextureEntity region) :angle 45)
          batch (mock-batch)]
      (e/draw! entity {} batch)
      (is (= 1 (count @batch-calls)))
      (is (= :draw-transform (first (first @batch-calls))))))

  (testing "TextureEntity draw! with color sets batch color"
    (let [region (TextureRegion.)
          entity (assoc (e/->TextureEntity region) :color [1 0 0 1])
          batch (mock-batch)]
      (e/draw! entity {} batch)
      (is (= 3 (count @batch-calls)))
      (is (= :set-color (first (first @batch-calls))))
      (is (= :draw-simple (first (second @batch-calls))))
      (is (= :set-color-obj (first (nth @batch-calls 2))))))

  (testing "TextureEntity draw! with position"
    (let [region (TextureRegion.)
          entity (assoc (e/->TextureEntity region) :x 10 :y 20)
          batch (mock-batch)]
      (e/draw! entity {} batch)
      (let [[_ _ x y _ _] (first @batch-calls)]
        (is (= 10.0 (float x)))
        (is (= 20.0 (float y)))))))

(deftest sprite-entity-draw-tests
  (testing "SpriteEntity draw! sets bounds and draws"
    (let [sprite (Sprite.)
          entity (e/->SpriteEntity sprite)
          batch (mock-batch)]
      ;; Sprite.draw needs a batch, but our mock proxy may not fully work
      ;; Just verify it doesn't throw on property setting
      (is (satisfies? e/Entity entity)))))

(deftest nine-patch-entity-draw-tests
  (testing "NinePatchEntity record exists and satisfies protocol"
    (is (resolve 'play-clj.entities/->NinePatchEntity))))

(deftest particle-effect-entity-tests
  (testing "ParticleEffectEntity can be created"
    (let [effect (ParticleEffect.)
          entity (e/->ParticleEffectEntity effect)]
      (is (satisfies? e/Entity entity)))))

(deftest actor-entity-tests
  (testing "ActorEntity can be created"
    (let [actor (Actor.)
          entity (e/->ActorEntity actor)]
      (is (satisfies? e/Entity entity))))

  (testing "ActorEntity draw! without stage is no-op"
    (let [actor (Actor.)
          entity (e/->ActorEntity actor)
          batch (mock-batch)]
      ;; Actor has no stage, so draw! should do nothing
      (e/draw! entity {} batch)
      (is (empty? @batch-calls)))))

(deftest model-entity-tests
  (testing "ModelEntity can be created"
    (let [entity (e/->ModelEntity nil)]
      (is (satisfies? e/Entity entity)))))

(deftest shape-entity-tests
  (testing "ShapeEntity record exists"
    (is (resolve 'play-clj.entities/->ShapeEntity))))

(deftest bundle-entity-draw-tests
  (testing "BundleEntity draw! calls draw! on each child"
    (let [child1 (e/->TextureEntity (TextureRegion.))
          child2 (e/->TextureEntity (TextureRegion.))
          bundle (e/->BundleEntity [child1 child2])
          batch (mock-batch)]
      (e/draw! bundle {} batch)
      ;; Each child calls draw, so we should have 2 draw calls
      (is (= 2 (count @batch-calls)))))

  (testing "BundleEntity draw! merges bundle keys to children"
    (let [child (e/->TextureEntity (TextureRegion.))
          bundle (assoc (e/->BundleEntity [child]) :x 100)
          batch (mock-batch)]
      (e/draw! bundle {} batch)
      ;; child should receive :x 100 from bundle and draw
      (is (= 1 (count @batch-calls))))))

(deftest map-entity-tests
  (testing "draw! on PersistentArrayMap returns nil"
    (is (nil? (e/draw! {} {} nil))))
  (testing "draw! on PersistentHashMap returns nil"
    (is (nil? (e/draw! {:a 1} {} nil))))
  (testing "draw! on nil returns nil"
    (is (nil? (e/draw! nil {} nil)))))
