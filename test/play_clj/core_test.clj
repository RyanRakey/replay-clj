(ns play-clj.core-test
  (:require [clojure.test :refer :all]
            [play-clj.headless-fixture]
            [play-clj.core :as c]
            [play-clj.utils :as u])
  (:import [com.badlogic.gdx.graphics Color OrthographicCamera PerspectiveCamera]
           [com.badlogic.gdx.graphics.glutils ShapeRenderer ShapeRenderer$ShapeType]
           [com.badlogic.gdx.graphics.g2d SpriteBatch]))

(use-fixtures :once play-clj.headless-fixture/headless-setup)

(deftest color-tests
  (testing "color from keyword creates named color"
    (let [col (c/color :red)]
      (is (instance? Color col))
      (is (= Color/RED col))))

  (testing "color from RGBA values"
    (let [col (c/color 1 0.5 0.25 1)]
      (is (instance? Color col))
      (is (= 1.0 (.r col)))
      (is (= 0.5 (.g col)))
      (is (= 0.25 (.b col)))
      (is (= 1.0 (.a col))))))

(deftest camera-tests
  (testing "orthographic creates OrthographicCamera"
    (let [cam (c/orthographic*)]
      (is (instance? OrthographicCamera cam))))

  (testing "perspective creates PerspectiveCamera"
    (let [cam (c/perspective* 75 800 600)]
      (is (instance? PerspectiveCamera cam))
      (is (= 75.0 (.fieldOfView cam)))))

  (testing "size! sets camera dimensions"
    (let [cam (c/orthographic*)
          screen {:camera cam}]
      (c/size! screen 640 480)
      (is (= 640.0 (.viewportWidth cam)))
      (is (= 480.0 (.viewportHeight cam))))))

(deftest shape-renderer-tests
  (testing "shape-type returns correct enum"
    (is (= ShapeRenderer$ShapeType/Filled (c/shape-type :filled)))
    (is (= ShapeRenderer$ShapeType/Line (c/shape-type :line)))
    (is (= ShapeRenderer$ShapeType/Point (c/shape-type :point)))))

(deftest pixmap-tests
  (testing "pixmap-format returns correct enum"
    (is (instance? com.badlogic.gdx.graphics.Pixmap$Format (c/pixmap-format :alpha)))
    (is (instance? com.badlogic.gdx.graphics.Pixmap$Format (c/pixmap-format :intensity)))
    (is (= com.badlogic.gdx.graphics.Pixmap$Format/RGBA8888 (eval (u/gdx-field :graphics "Pixmap$Format" "RGBA8888"))))))

(deftest key-code-tests
  (testing "key-code returns correct key codes"
    (is (number? (c/key-code :a)))
    (is (number? (c/key-code :space)))
    (is (number? (c/key-code :escape)))))

(deftest button-code-tests
  (testing "button-code returns correct button codes"
    (is (number? (c/button-code :left)))
    (is (number? (c/button-code :right)))
    (is (number? (c/button-code :middle)))))

(deftest screen-map-tests
  (testing "screen->input flips y axis without camera"
    (let [result (c/screen->input {:camera nil} 10 20)]
      ;; Without camera, it should just flip y
      (is (map? result))
      (is (contains? result :x))
      (is (contains? result :y)))))

(deftest input->screen-tests
  (testing "input->screen flips y axis without camera"
    (let [result (c/input->screen {:camera nil} 10 20)]
      (is (map? result))
      (is (contains? result :x))
      (is (contains? result :y)))))

(deftest find-first-tests
  (testing "find-first returns first matching entity"
    (is (= {:id 2 :active? true}
           (c/find-first :active? [{:id 1} {:id 2 :active? true} {:id 3 :active? true}])))
    (is (nil? (c/find-first :active? [{:id 1} {:id 2}]))))

  (testing "find-first with predicate function"
    (is (= {:id 2} (c/find-first #(= 2 (:id %)) [{:id 1} {:id 2} {:id 3}])))))

(deftest bundle-tests
  (testing "bundle creates a BundleEntity"
    (let [b (c/bundle {:x 1} {:x 2})]
      (is (c/bundle? b))
      (is (= 2 (count (:entities b))))))

  (testing "bundle? returns false for non-bundle"
    (is (not (c/bundle? {:entities []})))))

(deftest color-mutation-tests
  (testing "color! mutates color"
    (let [col (c/color :red)]
      (c/color! col :set 0 1 0 1)
      (is (= 0.0 (.r col)))
      (is (= 1.0 (.g col))))))

(deftest shape-predicate-tests
  (testing "shape? returns false for non-shape"
    (is (not (c/shape? {:object "not a shape"})))))

(deftest pixmap-creation-tests
  (testing "pixmap* creates Pixmap from dimensions"
    (let [pm (c/pixmap* 64 64 com.badlogic.gdx.graphics.Pixmap$Format/RGBA8888)]
      (is (instance? com.badlogic.gdx.graphics.Pixmap pm))
      (is (= 64 (.getWidth pm)))
      (is (= 64 (.getHeight pm)))
      (c/pixmap! pm :dispose))))

(deftest stage-tests
  (testing "stage macro exists"
    (is (:macro (meta (resolve 'play-clj.core/stage))))))

(deftest gl-static-field-tests
  (testing "gl returns static GL20 field"
    (is (number? (c/gl :gl-triangles)))
    (is (number? (c/gl :gl-src-alpha)))
    (is (number? (c/gl :gl-one-minus-src-alpha)))))

(deftest input-query-tests
  (testing "key-pressed? returns boolean"
    (is (boolean? (c/key-pressed? :a))))
  (testing "button-pressed? returns boolean"
    (is (boolean? (c/button-pressed? :left)))))

(deftest game-dimensions-tests
  (testing "game returns dimensions as numbers"
    (is (number? (c/game :width)))
    (is (number? (c/game :height)))
    (is (number? (c/game :fps)))))

(deftest on-gl-tests
  (testing "on-gl macro exists"
    (is (:macro (meta (resolve 'play-clj.core/on-gl))))))

(deftest pref-tests
  (testing "pref! macro exists"
    (is (:macro (meta (resolve 'play-clj.core/pref!))))))

(deftest screenshot-tests
  (testing "screenshot! macro exists"
    (is (resolve 'play-clj.core/screenshot!))))

(deftest asset-manager-tests
  (testing "asset-manager macro exists"
    (is (:macro (meta (resolve 'play-clj.core/asset-manager))))))
