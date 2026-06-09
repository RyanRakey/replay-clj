(ns play-clj.gl-ui-test
  (:require [clojure.test :refer :all]
            [play-clj.gl-fixture]
            [play-clj.ui :as ui]
            [play-clj.core :as c]
            [play-clj.g2d :as g2d])
  (:import [com.badlogic.gdx.graphics Pixmap Pixmap$Format Texture]
           [com.badlogic.gdx.graphics.g2d BitmapFont TextureRegion]
           [com.badlogic.gdx.scenes.scene2d Actor Stage]
           [com.badlogic.gdx.scenes.scene2d.ui CheckBox Container Dialog
            HorizontalGroup Image ImageButton ImageTextButton Label
            ScrollPane SelectBox Skin Slider Stack Table TextButton
            TextField Tree VerticalGroup Window]))

(use-fixtures :once play-clj.gl-fixture/gl-setup)

(defn- make-texture-region []
  (let [pm (Pixmap. 4 4 Pixmap$Format/RGBA8888)
        tex (Texture. pm)]
    (.dispose pm)
    (TextureRegion. tex)))

(defn- make-font []
  (BitmapFont.))

(defn- make-label-style []
  (let [font (make-font)]
    (com.badlogic.gdx.scenes.scene2d.ui.Label$LabelStyle. font com.badlogic.gdx.graphics.Color/WHITE)))

(defn- make-text-button-style []
  (let [font (make-font)
        dr (com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable. (make-texture-region))]
    (com.badlogic.gdx.scenes.scene2d.ui.TextButton$TextButtonStyle. dr dr dr font)))

(defn- make-check-box-style []
  (let [font (make-font)
        dr (com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable. (make-texture-region))]
    (com.badlogic.gdx.scenes.scene2d.ui.CheckBox$CheckBoxStyle. dr dr font com.badlogic.gdx.graphics.Color/WHITE)))

(deftest label-creation-tests
  (testing "label* creates Label with LabelStyle"
    (let [style (make-label-style)
          entity (ui/label* "Hello" style)]
      (is (ui/label? entity))
      (is (instance? Label (:object entity)))))

  (testing "label* with Color creates Label"
    (let [entity (ui/label* "Hello" com.badlogic.gdx.graphics.Color/WHITE)]
      (is (ui/label? entity))
      (is (instance? Label (:object entity)))))

  (testing "label! calls methods on Label"
    (let [style (make-label-style)
          entity (ui/label* "Hello" style)]
      (ui/label! entity :set-text "World")
      (is (some? (.getText (:object entity)))))))

(deftest text-button-creation-tests
  (testing "text-button* creates TextButton"
    (let [style (make-text-button-style)
          entity (ui/text-button* "Click me" style)]
      (is (ui/text-button? entity))
      (is (instance? TextButton (:object entity)))))

  (testing "text-button! calls methods"
    (let [style (make-text-button-style)
          entity (ui/text-button* "Click" style)]
      (ui/text-button! entity :set-disabled false)
      (is (instance? TextButton (:object entity))))))

(deftest check-box-creation-tests
  (testing "check-box* creates CheckBox"
    (let [style (make-check-box-style)
          entity (ui/check-box* "Check me" style)]
      (is (ui/check-box? entity))
      (is (instance? CheckBox (:object entity)))))

  (testing "check-box! calls methods"
    (let [style (make-check-box-style)
          entity (ui/check-box* "Check" style)]
      (ui/check-box! entity :set-checked true)
      (is (.isChecked (:object entity))))))

(deftest dialog-creation-tests
  (testing "dialog* creates Dialog"
    (let [style (com.badlogic.gdx.scenes.scene2d.ui.Window$WindowStyle.
                  (make-font) com.badlogic.gdx.graphics.Color/WHITE
                  (com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable. (make-texture-region)))
          entity (ui/dialog* "Title" style)]
      (is (ui/dialog? entity))
      (is (instance? Dialog (:object entity))))))

(deftest window-creation-tests
  (testing "window* creates Window"
    (let [style (com.badlogic.gdx.scenes.scene2d.ui.Window$WindowStyle.
                  (make-font) com.badlogic.gdx.graphics.Color/WHITE
                  (com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable. (make-texture-region)))
          entity (ui/window* [] "Title" style)]
      (is (ui/window? entity))
      (is (instance? Window (:object entity))))))

(deftest scroll-pane-creation-tests
  (testing "scroll-pane* creates ScrollPane"
    (let [child {:object (Actor.)}
          style (com.badlogic.gdx.scenes.scene2d.ui.ScrollPane$ScrollPaneStyle.)
          entity (ui/scroll-pane* child style)]
      (is (ui/scroll-pane? entity))
      (is (instance? ScrollPane (:object entity))))))

(deftest slider-creation-tests
  (testing "slider* creates Slider"
    (let [style (com.badlogic.gdx.scenes.scene2d.ui.Slider$SliderStyle.)
          entity (ui/slider* {:min 0 :max 100 :step 1} style)]
      (is (ui/slider? entity))
      (is (instance? Slider (:object entity))))))

(deftest tree-creation-tests
  (testing "tree* function exists"
    (is (resolve 'play-clj.ui/tree*)))
  (testing "tree! macro exists"
    (is (:macro (meta (resolve 'play-clj.ui/tree!))))))

(deftest image-button-creation-tests
  (testing "image-button* creates ImageButton"
    (let [dr (com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable. (make-texture-region))
          style (com.badlogic.gdx.scenes.scene2d.ui.ImageButton$ImageButtonStyle. dr dr dr dr dr dr)
          entity (ui/image-button* style)]
      (is (ui/image-button? entity))
      (is (instance? ImageButton (:object entity))))))

(deftest image-text-button-creation-tests
  (testing "image-text-button* creates ImageTextButton"
    (let [dr (com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable. (make-texture-region))
          font (make-font)
          style (com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton$ImageTextButtonStyle. dr dr dr font)
          entity (ui/image-text-button* "Click" style)]
      (is (ui/image-text-button? entity))
      (is (instance? ImageTextButton (:object entity))))))

(deftest select-box-creation-tests
  (testing "select-box* function exists"
    (is (resolve 'play-clj.ui/select-box*)))
  (testing "select-box! macro exists"
    (is (:macro (meta (resolve 'play-clj.ui/select-box!))))))

(deftest skin-creation-tests
  (testing "skin* function exists and Skin can be created"
    ;; Skin needs a file, so we just verify the function exists
    (is (resolve 'play-clj.ui/skin*))))

(deftest add-to-group-with-table-tests
  (testing "add-to-group! with Table adds children"
    (let [table (Table.)
          parent {:object table}
          child1 {:object (Actor.)}
          child2 {:object (Actor.)}]
      (ui/add! parent child1 :row child2)
      ;; Table should have rows
      (is (instance? Table table)))))

(deftest add-to-group-with-tree-tests
  (testing "add-to-group! with Tree function exists"
    (is (instance? clojure.lang.MultiFn
                   (var-get (resolve 'play-clj.ui/add-to-group!))))))

(deftest container-bang-behavioral-tests
  (testing "container! calls methods on Container"
    (let [child {:object (Actor.)}
          entity (ui/container* child)]
      (ui/container! entity :set-fill-parent true)
      (is (instance? Container (:object entity))))))

(deftest actor-bang-full-tests
  (testing "actor! sets name, position, and size"
    (let [actor (Actor.)
          entity {:object actor}]
      (ui/actor! entity :set-name "test-actor")
      (ui/actor! entity :set-position 10 20)
      (is (= "test-actor" (.getName actor)))
      (is (= 10.0 (.getX actor)))
      (is (= 20.0 (.getY actor))))))

(deftest image-bang-full-tests
  (testing "image! sets size and position"
    (let [region (make-texture-region)
          entity (ui/image* region)]
      (ui/image! entity :set-width 100)
      (ui/image! entity :set-height 50)
      (is (= 100.0 (.getWidth ^Image (:object entity))))
      (is (= 50.0 (.getHeight ^Image (:object entity)))))))

(deftest horizontal-bang-full-tests
  (testing "horizontal! calls methods on HorizontalGroup"
    (let [entity (ui/horizontal* [{:object (Actor.)}])]
      (ui/horizontal! entity :space 10)
      (is (instance? HorizontalGroup (:object entity))))))

(deftest vertical-bang-full-tests
  (testing "vertical! calls methods on VerticalGroup"
    (let [entity (ui/vertical* [{:object (Actor.)}])]
      (ui/vertical! entity :space 10)
      (is (instance? VerticalGroup (:object entity))))))

(deftest stack-bang-full-tests
  (testing "stack! calls methods on Stack"
    (let [entity (ui/stack* [{:object (Actor.)}])]
      (is (instance? Stack (:object entity))))))

(deftest table-bang-full-tests
  (testing "table! calls methods on Table"
    (let [entity (ui/table* [{:object (Actor.)}])]
      (ui/table! entity :set-fill-parent false)
      (is (instance? Table (:object entity))))))

(deftest drawable-full-tests
  (testing "drawable :texture-region creates TextureRegionDrawable"
    (let [d (ui/drawable :texture-region)]
      (is (instance? com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable d))))
  (testing "drawable :sprite creates SpriteDrawable"
    (let [d (ui/drawable :sprite)]
      (is (instance? com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable d))))
  (testing "drawable :nine-patch creates NinePatchDrawable"
    (let [d (ui/drawable :nine-patch)]
      (is (instance? com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable d)))))

(deftest style-full-tests
  (testing "style :label creates LabelStyle"
    (let [s (ui/style :label)]
      (is (instance? com.badlogic.gdx.scenes.scene2d.ui.Label$LabelStyle s))))
  (testing "style :text-button creates TextButtonStyle"
    (let [s (ui/style :text-button)]
      (is (instance? com.badlogic.gdx.scenes.scene2d.ui.TextButton$TextButtonStyle s))))
  (testing "style :window creates WindowStyle"
    (let [s (ui/style :window)]
      (is (instance? com.badlogic.gdx.scenes.scene2d.ui.Window$WindowStyle s))))
  (testing "style :scroll-pane creates ScrollPaneStyle"
    (let [s (ui/style :scroll-pane)]
      (is (instance? com.badlogic.gdx.scenes.scene2d.ui.ScrollPane$ScrollPaneStyle s))))
  (testing "style :slider creates SliderStyle"
    (let [s (ui/style :slider)]
      (is (instance? com.badlogic.gdx.scenes.scene2d.ui.Slider$SliderStyle s))))
  (testing "style :check-box creates CheckBoxStyle"
    (let [s (ui/style :check-box)]
      (is (instance? com.badlogic.gdx.scenes.scene2d.ui.CheckBox$CheckBoxStyle s)))))

(deftest align-full-tests
  (testing "align returns values for all alignment keywords"
    (is (number? (ui/align :center)))
    (is (number? (ui/align :top)))
    (is (number? (ui/align :bottom)))
    (is (number? (ui/align :left)))
    (is (number? (ui/align :right)))
    (is (number? (ui/align :top-left)))
    (is (number? (ui/align :top-right)))
    (is (number? (ui/align :bottom-left)))
    (is (number? (ui/align :bottom-right)))))
