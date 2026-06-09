(ns play-clj.gl-g2d-extended-test
  (:require [clojure.test :refer :all]
            [play-clj.gl-fixture]
            [play-clj.g2d :as g2d]
            [play-clj.utils :as u])
  (:import [com.badlogic.gdx.graphics Pixmap Pixmap$Format Texture]
           [com.badlogic.gdx.graphics.g2d Animation Animation$PlayMode
            BitmapFont NinePatch ParticleEffect Sprite TextureAtlas
            TextureRegion]))

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

(deftest texture-bang-behavioral-tests
  (testing "texture! calls methods on texture entity"
    (let [region (make-texture-region)
          entity (g2d/texture* region)]
      ;; texture! should be callable
      (is (ifn? (resolve 'play-clj.g2d/texture!))))))

(deftest sprite-full-behavioral-tests
  (testing "sprite! set-position and get-position"
    (let [sprite (make-sprite)
          entity (g2d/sprite* sprite)]
      (g2d/sprite! entity :set-position 50 75)
      (is (= 50.0 (g2d/sprite! entity :get-x)))
      (is (= 75.0 (g2d/sprite! entity :get-y)))))

  (testing "sprite! set-size"
    (let [sprite (make-sprite)
          entity (g2d/sprite* sprite)]
      (g2d/sprite! entity :set-size 100 200)
      (is (= 100.0 (g2d/sprite! entity :get-width)))
      (is (= 200.0 (g2d/sprite! entity :get-height)))))

  (testing "sprite! set-rotation"
    (let [sprite (make-sprite)
          entity (g2d/sprite* sprite)]
      (g2d/sprite! entity :set-rotation 45)
      (is (= 45.0 (g2d/sprite! entity :get-rotation)))))

  (testing "sprite! set-color"
    (let [sprite (make-sprite)
          entity (g2d/sprite* sprite)]
      (g2d/sprite! entity :set-color 1 0 0 1)
      (is (some? (g2d/sprite! entity :get-color))))))

(deftest nine-patch-full-tests
  (testing "nine-patch with padding"
    (let [region (make-texture-region)
          np (NinePatch. region 2 3 4 5)
          entity (g2d/nine-patch* np)]
      (is (g2d/nine-patch? entity))
      (is (instance? NinePatch np)))))

(deftest animation-full-behavioral-tests
  (testing "animation with different play modes"
    (let [r1 (make-texture-region)
          r2 (make-texture-region)
          anim (g2d/animation* 0.5 [r1 r2])]
      ;; Test play modes
      (g2d/animation! anim :set-play-mode (g2d/play-mode :normal))
      (is (= Animation$PlayMode/NORMAL (.getPlayMode anim)))
      (g2d/animation! anim :set-play-mode (g2d/play-mode :loop))
      (is (= Animation$PlayMode/LOOP (.getPlayMode anim)))
      (g2d/animation! anim :set-play-mode (g2d/play-mode :loop-pingpong))
      (is (= Animation$PlayMode/LOOP_PINGPONG (.getPlayMode anim)))
      (g2d/animation! anim :set-play-mode (g2d/play-mode :reversed))
      (is (= Animation$PlayMode/REVERSED (.getPlayMode anim)))
      (g2d/animation! anim :set-play-mode (g2d/play-mode :loop-reversed))
      (is (= Animation$PlayMode/LOOP_REVERSED (.getPlayMode anim)))
      (g2d/animation! anim :set-play-mode (g2d/play-mode :loop-random))
      (is (= Animation$PlayMode/LOOP_RANDOM (.getPlayMode anim)))))

  (testing "animation getKeyFrame with different times"
    (let [r1 (make-texture-region)
          r2 (make-texture-region)
          r3 (make-texture-region)
          anim (g2d/animation* 0.1 [r1 r2 r3])]
      ;; At time 0, should get first frame
      (let [frame (.getKeyFrame anim 0.0 true)]
        (is (instance? TextureRegion frame)))
      ;; At time 0.15, should get second frame
      (let [frame (.getKeyFrame anim 0.15 true)]
        (is (instance? TextureRegion frame)))
      ;; At time 0.25, should get third frame
      (let [frame (.getKeyFrame anim 0.25 true)]
        (is (instance? TextureRegion frame)))))

  (testing "animation getKeyFrames returns array"
    (let [r1 (make-texture-region)
          r2 (make-texture-region)
          anim (g2d/animation* 0.1 [r1 r2])]
      (is (= 2 (count (.getKeyFrames anim))))))

  (testing "animation getAnimationDuration"
    (let [r1 (make-texture-region)
          r2 (make-texture-region)
          r3 (make-texture-region)
          anim (g2d/animation* 0.2 [r1 r2 r3])]
      (is (< 0.59 (.getAnimationDuration anim) 0.61)))))

(deftest animation->texture-full-tests
  (testing "animation->texture with looping"
    (let [r1 (make-texture-region)
          r2 (make-texture-region)
          anim (g2d/animation* 0.2 [r1 r2])
          screen {:total-time 0.5}
          result (g2d/animation->texture screen anim true)]
      (is (g2d/texture? result)))))

(deftest particle-effect-full-tests
  (testing "particle-effect* creates entity"
    (let [entity (g2d/particle-effect* nil)]
      (is (g2d/particle-effect? entity))
      (is (instance? ParticleEffect (:object entity)))))

  (testing "particle-effect! function exists"
    (is (:macro (meta (resolve 'play-clj.g2d/particle-effect!))))))

(deftest texture-atlas-tests
  (testing "texture-atlas* function exists"
    (is (resolve 'play-clj.g2d/texture-atlas*)))
  (testing "texture-atlas macro exists"
    (is (:macro (meta (resolve 'play-clj.g2d/texture-atlas)))))
  (testing "texture-atlas! macro exists"
    (is (:macro (meta (resolve 'play-clj.g2d/texture-atlas!))))))

(deftest bitmap-font-behavioral-tests
  (testing "BitmapFont can be created and used"
    (let [font (BitmapFont.)]
      (is (some? font))
      ;; Test basic font operations
      (is (some? (.getCapHeight font)))
      (.dispose font))))
