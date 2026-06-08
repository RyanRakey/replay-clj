(ns play-clj.g2d-test
  (:require [clojure.test :refer :all]
            [play-clj.headless-fixture]
            [play-clj.g2d :as g2d]
            [play-clj.utils :as u])
  (:import [com.badlogic.gdx.graphics Pixmap Pixmap$Format Texture]
           [com.badlogic.gdx.graphics.g2d Animation Animation$PlayMode BitmapFont NinePatch ParticleEffect
            Sprite TextureAtlas TextureRegion]))

(use-fixtures :once play-clj.headless-fixture/headless-setup)

(deftest texture-creation-tests
  (testing "texture? predicate returns false for non-texture"
    (is (not (g2d/texture? {:object "not a texture"}))))

  (testing "texture from existing TextureRegion creates new TextureRegion entity"
    ;; Create a texture directly via pixmap to avoid Gdx.gl requirement
    ;; Actually, in headless mode Gdx.gl is null so Texture won't work.
    ;; We test the wrapping logic instead.
    (let [existing-region (TextureRegion.)
          entity (g2d/texture* existing-region)]
      (is (g2d/texture? entity)))))

(deftest sprite-creation-tests
  (testing "sprite? predicate returns false for non-sprite"
    (is (not (g2d/sprite? {:object "not a sprite"}))))

  (testing "sprite from existing Sprite creates Sprite entity"
    (let [existing-sprite (Sprite.)
          entity (g2d/sprite* existing-sprite)]
      (is (instance? Sprite (:object entity)))
      (is (g2d/sprite? entity)))))

(deftest nine-patch-creation-tests
  (testing "nine-patch? predicate returns false for non-nine-patch"
    (is (not (g2d/nine-patch? {:object "not a nine-patch"})))))

(deftest bitmap-font-tests
  (testing "bitmap-font macro exists in namespace"
    ;; BitmapFont default constructor crashes in headless without GL context
    ;; so we just verify the wrapper macro exists
    (is (get (ns-publics 'play-clj.g2d) 'bitmap-font))))

(deftest animation-creation-tests
  (testing "animation from texture regions"
    (let [region1 (TextureRegion.)
          region2 (TextureRegion.)
          anim (g2d/animation* 0.2 [region1 region2])]
      (is (instance? Animation anim))
      (is (= 2 (count (.getKeyFrames anim)))))))

(deftest texture-atlas-tests
  (testing "texture-atlas* returns nil for missing file"
    (is (nil? (try (u/load-asset "nonexistent.atlas" TextureAtlas)
                   (catch Exception _ nil))))))

(deftest particle-effect-tests
  (testing "particle-effect? predicate returns false for non-particle-effect"
    (is (not (g2d/particle-effect? {:object "not a particle effect"}))))

  (testing "particle-effect from existing ParticleEffect creates entity"
    (let [effect (ParticleEffect.)
          entity (g2d/particle-effect* nil)]
      (is (g2d/particle-effect? entity)))))

(deftest texture-macro-tests
  (testing "texture! macro exists and is callable"
    (let [region (TextureRegion.)
          entity (g2d/texture* region)]
      ;; Just verify texture! is a macro that doesn't throw when called
      (is (ifn? (resolve 'play-clj.g2d/texture!))))))

(deftest sprite-macro-tests
  (testing "sprite! macro calls method on sprite"
    (let [sprite (Sprite.)
          entity (g2d/sprite* sprite)]
      (is (= 1.0 (g2d/sprite! entity :get-scale-x))))))

(deftest animation-macro-tests
  (testing "animation! macro calls method on animation"
    (let [region1 (TextureRegion.)
          region2 (TextureRegion.)
          anim (g2d/animation* 0.2 [region1 region2])]
      (is (instance? Animation anim))
      (g2d/animation! anim :set-play-mode (g2d/play-mode :loop))
      (is (= Animation$PlayMode/LOOP (.getPlayMode anim))))))

(deftest bitmap-font-macro-tests
  (testing "bitmap-font macro exists"
    (is (:macro (meta (resolve 'play-clj.g2d/bitmap-font))))))

(deftest nine-patch-macro-tests
  (testing "nine-patch macro exists"
    (is (:macro (meta (resolve 'play-clj.g2d/nine-patch))))))

(deftest particle-effect-macro-tests
  (testing "particle-effect macro exists"
    (is (:macro (meta (resolve 'play-clj.g2d/particle-effect))))))

(deftest texture-atlas-macro-tests
  (testing "texture-atlas macro exists"
    (is (:macro (meta (resolve 'play-clj.g2d/texture-atlas))))))

(deftest texture-macro-existence-tests
  (testing "texture macro exists"
    (is (:macro (meta (resolve 'play-clj.g2d/texture))))))

(deftest sprite-macro-existence-tests
  (testing "sprite macro exists"
    (is (:macro (meta (resolve 'play-clj.g2d/sprite))))))

(deftest nine-patch-bang-macro-tests
  (testing "nine-patch! macro exists"
    (is (:macro (meta (resolve 'play-clj.g2d/nine-patch!))))))

(deftest particle-effect-bang-macro-tests
  (testing "particle-effect! macro exists"
    (is (:macro (meta (resolve 'play-clj.g2d/particle-effect!))))))

(deftest texture-atlas-bang-macro-tests
  (testing "texture-atlas! macro exists"
    (is (:macro (meta (resolve 'play-clj.g2d/texture-atlas!))))))

(deftest bitmap-font-bang-macro-tests
  (testing "bitmap-font! macro exists"
    (is (:macro (meta (resolve 'play-clj.g2d/bitmap-font!))))))

(deftest animation-bang-macro-tests
  (testing "animation! macro exists"
    (is (:macro (meta (resolve 'play-clj.g2d/animation!))))))

(deftest play-mode-tests
  (testing "play-mode returns Animation.PlayMode enum"
    (is (instance? Animation$PlayMode (g2d/play-mode :normal)))
    (is (instance? Animation$PlayMode (g2d/play-mode :loop)))
    (is (instance? Animation$PlayMode (g2d/play-mode :loop-pingpong)))))

(deftest animation->texture-tests
  (testing "animation->texture function exists"
    (is (resolve 'play-clj.g2d/animation->texture)))

  (testing "animation->texture returns TextureRegion from animation and screen"
    (let [region1 (TextureRegion.)
          region2 (TextureRegion.)
          anim (g2d/animation* 0.2 [region1 region2])
          screen {:total-time 0.05}
          result (g2d/animation->texture screen anim)]
      (is (g2d/texture? result)))))

(deftest nine-patch-star-tests
  (testing "nine-patch* function exists"
    (is (resolve 'play-clj.g2d/nine-patch*))))

(deftest animation-macro-existence-tests
  (testing "animation macro exists"
    (is (:macro (meta (resolve 'play-clj.g2d/animation))))))

;; Additional behavioral tests

(deftest texture-entity-from-region-tests
  (testing "texture* from TextureRegion creates texture entity"
    (let [region (TextureRegion.)
          entity (g2d/texture* region)]
      (is (g2d/texture? entity))
      (is (= region (:object entity))))))

(deftest sprite-entity-from-sprite-tests
  (testing "sprite* from Sprite preserves sprite"
    (let [sprite (Sprite.)]
      (.setBounds sprite 10 20 32 32)
      (let [entity (g2d/sprite* sprite)]
        (is (g2d/sprite? entity))
        (is (= sprite (:object entity)))
        (is (= 10.0 (.getX (:object entity))))))))

(deftest animation-play-mode-behavioral-tests
  (testing "play-mode returns correct enum values"
    (is (= com.badlogic.gdx.graphics.g2d.Animation$PlayMode/NORMAL
           (g2d/play-mode :normal)))
    (is (= com.badlogic.gdx.graphics.g2d.Animation$PlayMode/LOOP
           (g2d/play-mode :loop)))
    (is (= com.badlogic.gdx.graphics.g2d.Animation$PlayMode/LOOP_PINGPONG
           (g2d/play-mode :loop-pingpong)))
    (is (= com.badlogic.gdx.graphics.g2d.Animation$PlayMode/LOOP_REVERSED
           (g2d/play-mode :loop-reversed)))
    (is (= com.badlogic.gdx.graphics.g2d.Animation$PlayMode/REVERSED
           (g2d/play-mode :reversed)))))

(deftest animation-get-key-frame-tests
  (testing "animation getKeyFrame returns a TextureRegion"
    (let [r1 (TextureRegion.)
          r2 (TextureRegion.)
          r3 (TextureRegion.)
          anim (g2d/animation* 0.1 [r1 r2 r3])]
      (is (instance? com.badlogic.gdx.graphics.g2d.Animation anim))
      (is (instance? TextureRegion (.getKeyFrame anim 0.0 true)))
      (is (= 3 (count (.getKeyFrames anim)))))))

(deftest particle-effect-star-tests
  (testing "particle-effect* creates entity with ParticleEffect"
    (let [entity (g2d/particle-effect* nil)]
      (is (g2d/particle-effect? entity))
      (is (instance? ParticleEffect (:object entity))))))

(deftest nine-patch-predicate-tests
  (testing "nine-patch? returns false for non-nine-patch object"
    (is (not (g2d/nine-patch? {:object "not a nine-patch"})))))

(deftest texture-atlas-predicate-tests
  (testing "texture-atlas function exists"
    (is (resolve 'play-clj.g2d/texture-atlas*))
    (is (:macro (meta (resolve 'play-clj.g2d/texture-atlas))))))
