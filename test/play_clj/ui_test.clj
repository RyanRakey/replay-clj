(ns play-clj.ui-test
  (:require [clojure.test :refer :all]
            [play-clj.headless-fixture]
            [play-clj.core :as c]
            [play-clj.g2d :as g2d]
            [play-clj.ui :as ui])
  (:import [com.badlogic.gdx.graphics.g2d TextureRegion]
           [com.badlogic.gdx.scenes.scene2d Actor]))

(use-fixtures :once play-clj.headless-fixture/headless-setup)

(deftest actor-predicate-tests
  (testing "actor? returns true for actor entity"
    (let [actor (Actor.)
          entity (ui/actor? {:object actor})]
      (is (true? entity))))
  (testing "actor? returns false for non-actor"
    (is (not (ui/actor? {:object "not an actor"})))))

(deftest label-tests
  (testing "label? returns false for non-label"
    (is (not (ui/label? {:object "not a label"}))))
  (testing "label macro exists"
    (is (resolve 'play-clj.ui/label))))

(deftest image-tests
  (testing "image from texture region creates entity"
    (let [region (TextureRegion.)
          entity (ui/image region)]
      (is (ui/image? entity))))
  (testing "image? returns false for non-image"
    (is (not (ui/image? {:object "not an image"})))))

(deftest container-tests
  (testing "container wraps entity"
    (let [inner {:object (Actor.)}
          entity (ui/container inner)]
      (is (ui/container? entity))))
  (testing "container? returns false for non-container"
    (is (not (ui/container? {:object "not a container"})))))

(deftest table-tests
  (testing "table with children creates entity"
    (let [entity (ui/table [{:object (Actor.)}])]
      (is (ui/table? entity))))
  (testing "table? returns false for non-table"
    (is (not (ui/table? {:object "not a table"})))))

(deftest stack-tests
  (testing "stack with children creates entity"
    (let [entity (ui/stack [{:object (Actor.)}])]
      (is (ui/stack? entity))))
  (testing "stack? returns false for non-stack"
    (is (not (ui/stack? {:object "not a stack"})))))

(deftest horizontal-group-tests
  (testing "horizontal with children creates entity"
    (let [entity (ui/horizontal [{:object (Actor.)}])]
      (is (ui/horizontal? entity))))
  (testing "horizontal? returns false for non-horizontal"
    (is (not (ui/horizontal? {:object "not a horizontal"})))))

(deftest vertical-group-tests
  (testing "vertical with children creates entity"
    (let [entity (ui/vertical [{:object (Actor.)}])]
      (is (ui/vertical? entity))))
  (testing "vertical? returns false for non-vertical"
    (is (not (ui/vertical? {:object "not a vertical"})))))

(deftest check-box-tests
  (testing "check-box? returns false for non-check-box"
    (is (not (ui/check-box? {:object "not a check-box"})))))

(deftest dialog-tests
  (testing "dialog? returns false for non-dialog"
    (is (not (ui/dialog? {:object "not a dialog"})))))

(deftest window-tests
  (testing "window? returns false for non-window"
    (is (not (ui/window? {:object "not a window"})))))

(deftest text-button-tests
  (testing "text-button? returns false for non-text-button"
    (is (not (ui/text-button? {:object "not a text-button"})))))

(deftest text-field-tests
  (testing "text-field? returns false for non-text-field"
    (is (not (ui/text-field? {:object "not a text-field"})))))

(deftest scroll-pane-tests
  (testing "scroll-pane? returns false for non-scroll-pane"
    (is (not (ui/scroll-pane? {:object "not a scroll-pane"})))))

(deftest slider-tests
  (testing "slider? returns false for non-slider"
    (is (not (ui/slider? {:object "not a slider"})))))

(deftest tree-tests
  (testing "tree? returns false for non-tree"
    (is (not (ui/tree? {:object "not a tree"})))))

(deftest image-button-tests
  (testing "image-button? returns false for non-image-button"
    (is (not (ui/image-button? {:object "not an image-button"})))))

(deftest image-text-button-tests
  (testing "image-text-button? returns false for non-image-text-button"
    (is (not (ui/image-text-button? {:object "not an image-text-button"})))))

(deftest add-tests
  (testing "add! adds children to group"
    (let [parent {:object (com.badlogic.gdx.scenes.scene2d.ui.Table.)}
          child {:object (Actor.)}
          result (ui/add! parent child)]
      (is (= parent result)))))

(deftest align-tests
  (testing "align returns static field"
    (is (number? (ui/align :center)))
    (is (number? (ui/align :left)))
    (is (number? (ui/align :top)))))

(deftest drawable-tests
  (testing "drawable macro creates drawable"
    (let [d (ui/drawable :texture-region)]
      (is (instance? com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable d)))))

(deftest widget-macro-existence-tests
  (testing "widget creation macros exist"
    (is (:macro (meta (resolve 'play-clj.ui/check-box))))
    (is (:macro (meta (resolve 'play-clj.ui/dialog))))
    (is (:macro (meta (resolve 'play-clj.ui/image-button))))
    (is (:macro (meta (resolve 'play-clj.ui/image-text-button))))
    (is (:macro (meta (resolve 'play-clj.ui/scroll-pane))))
    (is (:macro (meta (resolve 'play-clj.ui/select-box))))
    (is (:macro (meta (resolve 'play-clj.ui/slider))))
    (is (:macro (meta (resolve 'play-clj.ui/text-button))))
    (is (:macro (meta (resolve 'play-clj.ui/text-field))))
    (is (:macro (meta (resolve 'play-clj.ui/tree))))
    (is (:macro (meta (resolve 'play-clj.ui/window))))))

(deftest widget-bang-macro-tests
  (testing "widget mutation macros exist"
    (is (:macro (meta (resolve 'play-clj.ui/check-box!))))
    (is (:macro (meta (resolve 'play-clj.ui/dialog!))))
    (is (:macro (meta (resolve 'play-clj.ui/image-button!))))
    (is (:macro (meta (resolve 'play-clj.ui/image-text-button!))))
    (is (:macro (meta (resolve 'play-clj.ui/scroll-pane!))))
    (is (:macro (meta (resolve 'play-clj.ui/select-box!))))
    (is (:macro (meta (resolve 'play-clj.ui/slider!))))
    (is (:macro (meta (resolve 'play-clj.ui/text-button!))))
    (is (:macro (meta (resolve 'play-clj.ui/text-field!))))
    (is (:macro (meta (resolve 'play-clj.ui/tree!))))
    (is (:macro (meta (resolve 'play-clj.ui/window!))))
    (is (:macro (meta (resolve 'play-clj.ui/actor!))))
    (is (:macro (meta (resolve 'play-clj.ui/container!))))
    (is (:macro (meta (resolve 'play-clj.ui/image!))))
    (is (:macro (meta (resolve 'play-clj.ui/label!))))
    (is (:macro (meta (resolve 'play-clj.ui/table!))))
    (is (:macro (meta (resolve 'play-clj.ui/stack!))))
    (is (:macro (meta (resolve 'play-clj.ui/horizontal!))))
    (is (:macro (meta (resolve 'play-clj.ui/vertical!))))))

(deftest skin-macro-tests
  (testing "skin and skin! macros exist"
    (is (:macro (meta (resolve 'play-clj.ui/skin))))
    (is (:macro (meta (resolve 'play-clj.ui/skin!))))))

(deftest style-macro-tests
  (testing "style macro exists"
    (is (:macro (meta (resolve 'play-clj.ui/style))))))

(deftest cell-tests
  (testing "cell! function exists"
    (is (resolve 'play-clj.ui/cell!))))

(deftest listener-macro-tests
  (testing "listener macros exist"
    (is (:macro (meta (resolve 'play-clj.ui/actor-gesture-listener!))))
    (is (:macro (meta (resolve 'play-clj.ui/change-listener!))))
    (is (:macro (meta (resolve 'play-clj.ui/click-listener!))))
    (is (:macro (meta (resolve 'play-clj.ui/drag-listener!))))
    (is (:macro (meta (resolve 'play-clj.ui/focus-listener!))))))
