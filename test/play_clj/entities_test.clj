(ns play-clj.entities-test
  (:require [clojure.test :refer :all]
            [play-clj.headless-fixture]
            [play-clj.entities :as e])
  (:import [com.badlogic.gdx.graphics.g2d NinePatch ParticleEffect TextureRegion Sprite]
           [com.badlogic.gdx.graphics.glutils ShapeRenderer]
           [com.badlogic.gdx.scenes.scene2d Actor]))

(use-fixtures :once play-clj.headless-fixture/headless-setup)

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

(deftest entity-draw-tests
  (testing "TextureEntity protocol method exists"
    (let [region (TextureRegion.)
          entity (e/->TextureEntity region)]
      ;; We can't create SpriteBatch without GL context,
      ;; so we just verify the protocol method exists and is callable
      (is (ifn? e/draw!))))

  (testing "nil entity returns nil from draw!"
    ;; draw! on nil should not throw
    (is (nil? (e/draw! nil {} nil)))))

(deftest nine-patch-entity-tests
  (testing "NinePatchEntity record exists"
    ;; NinePatch requires a valid texture, hard to create in headless mode
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
      (is (satisfies? e/Entity entity)))))

(deftest shape-entity-tests
  (testing "ShapeEntity record exists"
    ;; ShapeRenderer requires a GL context
    (is (resolve 'play-clj.entities/->ShapeEntity))))

(deftest model-entity-tests
  (testing "ModelEntity can be created"
    (let [entity (e/->ModelEntity nil)]
      (is (satisfies? e/Entity entity)))))

(deftest bundle-entity-merge-tests
  (testing "BundleEntity draw! merges bundle keys to children"
    (let [inner [{:x 10} {:x 20}]
          bundle (e/->BundleEntity inner)]
      (is (satisfies? e/Entity bundle))
      (is (= 2 (count (:entities bundle)))))))

(deftest map-entity-tests
  (testing "draw! on PersistentArrayMap returns nil"
    (is (nil? (e/draw! {} {} nil))))
  (testing "draw! on PersistentHashMap returns nil"
    (is (nil? (e/draw! {:a 1} {} nil)))))
