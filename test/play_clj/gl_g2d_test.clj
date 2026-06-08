(ns play-clj.gl-g2d-test
  (:require [clojure.test :refer :all]
            [play-clj.gl-fixture]
            [play-clj.g2d :as g2d])
  (:import [com.badlogic.gdx.graphics Pixmap Pixmap$Format Texture]
           [com.badlogic.gdx.graphics.g2d Animation BitmapFont NinePatch
            Sprite TextureRegion]))

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

(deftest texture-creation-with-gl-tests
  (testing "texture* from TextureRegion creates entity"
    (let [region (make-texture-region)
          entity (g2d/texture* region)]
      (is (g2d/texture? entity)))))

(deftest texture-from-pixmap-tests
  (testing "Texture can be created from Pixmap with GL context"
    (let [pm (Pixmap. 32 32 Pixmap$Format/RGBA8888)
          tex (Texture. pm)]
      (is (some? tex))
      (.dispose pm)
      (.dispose tex)))

  (testing "texture* from Texture creates entity"
    (let [pm (Pixmap. 32 32 Pixmap$Format/RGBA8888)
          tex (Texture. pm)
          region (TextureRegion. tex)
          entity (g2d/texture* region)]
      (is (g2d/texture? entity))
      (.dispose pm)
      (.dispose tex))))

(deftest sprite-creation-with-gl-tests
  (testing "sprite* from existing Sprite"
    (let [sprite (make-sprite)
          entity (g2d/sprite* sprite)]
      (is (g2d/sprite? entity)))))

(deftest sprite-bang-with-gl-tests
  (testing "sprite! calls method on sprite entity"
    (let [sprite (make-sprite)
          entity (g2d/sprite* sprite)]
      (is (= 1.0 (g2d/sprite! entity :get-scale-x)))
      (g2d/sprite! entity :set-scale 2 2)
      (is (= 2.0 (g2d/sprite! entity :get-scale-x))))))

(deftest nine-patch-creation-with-gl-tests
  (testing "NinePatch can be created from TextureRegion"
    (let [region (make-texture-region)
          np (NinePatch. region 2 2 2 2)]
      (is (some? np))
      (is (instance? NinePatch np)))))

(deftest bitmap-font-creation-with-gl-tests
  (testing "BitmapFont can be created with GL context"
    (let [font (BitmapFont.)]
      (is (some? font))
      (.dispose font))))

(deftest animation-creation-with-gl-tests
  (testing "animation* creates Animation from regions"
    (let [r1 (make-texture-region)
          r2 (make-texture-region)
          r3 (make-texture-region)
          anim (g2d/animation* 0.1 [r1 r2 r3])]
      (is (instance? Animation anim))
      (is (= 3 (count (.getKeyFrames anim))))))

  (testing "animation! sets play mode"
    (let [r1 (make-texture-region)
          r2 (make-texture-region)
          anim (g2d/animation* 0.1 [r1 r2])]
      (g2d/animation! anim :set-play-mode (g2d/play-mode :loop))
      (is (= com.badlogic.gdx.graphics.g2d.Animation$PlayMode/LOOP
             (.getPlayMode anim))))))

(deftest animation->texture-with-gl-tests
  (testing "animation->texture returns texture entity"
    (let [r1 (make-texture-region)
          r2 (make-texture-region)
          anim (g2d/animation* 0.2 [r1 r2])
          screen {:total-time 0.05}
          result (g2d/animation->texture screen anim)]
      (is (g2d/texture? result)))))
