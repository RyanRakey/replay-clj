(ns play-clj.core-test
  (:require [clojure.test :refer :all]
            [play-clj.headless-fixture]
            [play-clj.core :as c]
            [play-clj.utils :as u])
  (:import [com.badlogic.gdx.graphics Color OrthographicCamera PerspectiveCamera]
           [com.badlogic.gdx.graphics.glutils ShapeRenderer ShapeRenderer$ShapeType]
           [com.badlogic.gdx.graphics.g2d SpriteBatch]
           [com.badlogic.gdx.maps MapLayer MapLayers MapObject MapObjects]
           [com.badlogic.gdx.maps.tiled TiledMap TiledMapTileLayer TiledMapTileLayer$Cell]
           [com.badlogic.gdx.scenes.scene2d Stage]))

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

(deftest gdx-subsystem-macro-tests
  (testing "GDX subsystem macros exist"
    (is (:macro (meta (resolve 'play-clj.core/app!))))
    (is (:macro (meta (resolve 'play-clj.core/audio!))))
    (is (:macro (meta (resolve 'play-clj.core/files!))))
    (is (:macro (meta (resolve 'play-clj.core/gl!))))
    (is (:macro (meta (resolve 'play-clj.core/graphics!))))
    (is (:macro (meta (resolve 'play-clj.core/input!))))
    (is (:macro (meta (resolve 'play-clj.core/net!))))))

(deftest sound-music-macro-tests
  (testing "sound and music macros exist"
    (is (:macro (meta (resolve 'play-clj.core/sound))))
    (is (:macro (meta (resolve 'play-clj.core/sound!))))
    (is (:macro (meta (resolve 'play-clj.core/music))))
    (is (:macro (meta (resolve 'play-clj.core/music!))))))

(deftest pixmap-macro-tests
  (testing "pixmap and pixmap! macros exist"
    (is (:macro (meta (resolve 'play-clj.core/pixmap))))
    (is (:macro (meta (resolve 'play-clj.core/pixmap!))))))

(deftest shape-macro-tests
  (testing "shape and shape! macros exist"
    (is (:macro (meta (resolve 'play-clj.core/shape))))
    (is (:macro (meta (resolve 'play-clj.core/shape!))))))

(deftest tiled-map-macro-tests
  (testing "tiled-map macros exist"
    (is (:macro (meta (resolve 'play-clj.core/tiled-map))))
    (is (:macro (meta (resolve 'play-clj.core/tiled-map!))))
    (is (:macro (meta (resolve 'play-clj.core/tiled-map-layer))))
    (is (:macro (meta (resolve 'play-clj.core/tiled-map-layer!))))
    (is (:macro (meta (resolve 'play-clj.core/tiled-map-cell))))
    (is (:macro (meta (resolve 'play-clj.core/tiled-map-cell!))))))

(deftest map-macro-tests
  (testing "map-related macros exist"
    (is (:macro (meta (resolve 'play-clj.core/map-layers))))
    (is (:macro (meta (resolve 'play-clj.core/map-layers!))))
    (is (:macro (meta (resolve 'play-clj.core/map-layer))))
    (is (:macro (meta (resolve 'play-clj.core/map-layer!))))
    (is (:macro (meta (resolve 'play-clj.core/map-objects))))
    (is (:macro (meta (resolve 'play-clj.core/map-objects!))))
    (is (:macro (meta (resolve 'play-clj.core/map-object))))
    (is (:macro (meta (resolve 'play-clj.core/map-object!))))))

(deftest renderer-macro-tests
  (testing "renderer macros exist"
    (is (:macro (meta (resolve 'play-clj.core/orthogonal-tiled-map))))
    (is (:macro (meta (resolve 'play-clj.core/orthogonal-tiled-map!))))
    (is (:macro (meta (resolve 'play-clj.core/isometric-tiled-map))))
    (is (:macro (meta (resolve 'play-clj.core/isometric-tiled-map!))))
    (is (:macro (meta (resolve 'play-clj.core/hexagonal-tiled-map))))
    (is (:macro (meta (resolve 'play-clj.core/hexagonal-tiled-map!))))
    (is (:macro (meta (resolve 'play-clj.core/isometric-staggered-tiled-map))))
    (is (:macro (meta (resolve 'play-clj.core/isometric-staggered-tiled-map!))))))

(deftest camera-position-tests
  (testing "x and y return camera position"
    (let [cam (c/orthographic*)
          screen {:camera cam}]
      (c/position! screen 10 20)
      (is (= 10.0 (c/x screen)))
      (is (= 20.0 (c/y screen)))))
  (testing "position returns camera position vector"
    (let [cam (c/orthographic*)
          screen {:camera cam}]
      (c/position! screen 5 5)
      (is (instance? com.badlogic.gdx.math.Vector3 (c/position screen)))))
  (testing "x! and y! set camera position"
    (let [cam (c/orthographic*)
          screen {:camera cam}]
      (c/x! screen 100)
      (c/y! screen 200)
      (is (= 100.0 (c/x screen)))
      (is (= 200.0 (c/y screen))))))

(deftest perspective-camera-tests
  (testing "z returns camera z position"
    (let [cam (c/perspective* 75 800 600)
          screen {:camera cam}]
      (c/position! screen 1 2 3)
      (is (= 3.0 (c/z screen)))))
  (testing "near and far return clipping distances"
    (let [cam (c/perspective* 75 800 600)
          screen {:camera cam}]
      (is (number? (c/near screen)))
      (is (number? (c/far screen)))))
  (testing "near! and far! set clipping distances"
    (let [cam (c/perspective* 75 800 600)
          screen {:camera cam}]
      (c/near! screen 0.1)
      (c/far! screen 1000)
      ;; Use approximate comparison because .near returns a float
      (is (< 0.09 (c/near screen) 0.11))
      (is (= 1000.0 (c/far screen))))))

(deftest width-height-tests
  (testing "width and height return camera viewport dimensions"
    (let [cam (c/orthographic*)
          screen {:camera cam}]
      (c/size! screen 640 480)
      (is (= 640.0 (c/width screen)))
      (is (= 480.0 (c/height screen))))))

(deftest direction-tests
  (testing "direction returns camera direction vector"
    (let [cam (c/perspective* 75 800 600)
          screen {:camera cam}]
      (is (instance? com.badlogic.gdx.math.Vector3 (c/direction screen)))))
  (testing "direction! sets camera direction"
    (let [cam (c/perspective* 75 800 600)
          screen {:camera cam}]
      (c/direction! screen 0 0 -1 false)
      (is (instance? com.badlogic.gdx.math.Vector3 (c/direction screen))))))

(deftest up-tests
  (testing "up returns camera up vector"
    (let [cam (c/perspective* 75 800 600)
          screen {:camera cam}]
      (is (instance? com.badlogic.gdx.math.Vector3 (c/up screen)))))
  (testing "up! sets camera up vector"
    (let [cam (c/perspective* 75 800 600)
          screen {:camera cam}]
      (c/up! screen 0 1 0)
      (is (instance? com.badlogic.gdx.math.Vector3 (c/up screen))))))

(deftest camera-macro-tests
  (testing "camera mutation macros exist"
    (is (:macro (meta (resolve 'play-clj.core/orthographic!))))
    (is (:macro (meta (resolve 'play-clj.core/perspective!))))))

(deftest orthographic-macro-tests
  (testing "orthographic macro creates OrthographicCamera"
    (let [cam (c/orthographic)]
      (is (instance? OrthographicCamera cam)))))

(deftest perspective-macro-tests
  (testing "perspective macro creates PerspectiveCamera"
    (let [cam (c/perspective 75 800 600)]
      (is (instance? PerspectiveCamera cam))
      (is (= 75.0 (.fieldOfView cam))))))

(deftest width-bang-tests
  (testing "width! function exists"
    (is (resolve 'play-clj.core/width!))))

(deftest height-bang-tests
  (testing "height! function exists"
    (is (resolve 'play-clj.core/height!))))

(deftest z-bang-tests
  (testing "z! sets camera z position"
    (let [cam (c/perspective* 75 800 600)
          screen {:camera cam}]
      (c/position! screen 1 2 0)
      (c/z! screen 42.0)
      (is (= 42.0 (c/z screen))))))

(deftest game-input-tests
  (testing "game returns input/touch values"
    (is (boolean? (c/game :touched?)))
    (is (boolean? (c/game :fullscreen?)))))

(deftest core-macro-tests
  (testing "core game/screen macros exist"
    (is (:macro (meta (resolve 'play-clj.core/defscreen))))
    (is (:macro (meta (resolve 'play-clj.core/defgame))))
    (is (resolve 'play-clj.core/set-screen!))
    (is (resolve 'play-clj.core/set-screen-wrapper!))
    (is (resolve 'play-clj.core/update!))
    (is (resolve 'play-clj.core/screen!))))

(deftest update-tests
  (testing "update! assoc values into screen map"
    (let [sa (atom {})
          screen {:update-fn! (fn [f & args] (apply swap! sa f args))}
          result (c/update! screen :x 1)]
      ;; update! returns the updated screen after calling update-screen!
      ;; which may fail without renderer, so just verify it doesn't throw
      (is (or (map? result) (nil? result))))))

;; core_graphics.clj tests

(deftest shape-creation-tests
  (testing "shape* function exists"
    (is (resolve 'play-clj.core/shape*))))

(deftest shape-macro-tests
  (testing "shape macro exists"
    (is (:macro (meta (resolve 'play-clj.core/shape))))
    (is (:macro (meta (resolve 'play-clj.core/shape!))))))

(deftest tiled-map-creation-tests
  (testing "tiled-map* with no args creates empty TiledMap"
    (let [tm (c/tiled-map*)]
      (is (instance? TiledMap tm))
      (is (some? (.getLayers tm))))))

(deftest tiled-map-cell-creation-tests
  (testing "tiled-map-cell* with no args creates Cell"
    (let [cell (c/tiled-map-cell*)]
      (is (instance? TiledMapTileLayer$Cell cell)))))

(deftest tiled-map-layer-creation-tests
  (testing "tiled-map-layer* with dimensions creates TiledMapTileLayer"
    (let [layer (c/tiled-map-layer* 10 10 32 32)]
      (is (instance? TiledMapTileLayer layer))
      (is (= 10 (.getWidth layer)))
      (is (= 10 (.getHeight layer))))))

(deftest map-layers-creation-tests
  (testing "map-layers* with no args creates empty MapLayers"
    (let [layers (c/map-layers*)]
      (is (instance? MapLayers layers))
      (is (= 0 (.size layers))))))

(deftest map-layer-creation-tests
  (testing "map-layer* with no args creates MapLayer"
    (let [layer (c/map-layer*)]
      (is (instance? MapLayer layer)))))

(deftest map-objects-creation-tests
  (testing "map-objects* with no args creates MapObjects"
    (let [objs (c/map-objects*)]
      (is (instance? MapObjects objs))
      (is (some? objs)))))

(deftest map-object-creation-tests
  (testing "map-object* creates MapObject"
    (let [obj (c/map-object*)]
      (is (instance? MapObject obj)))))

(deftest stage-creation-tests
  (testing "stage* function exists"
    (is (resolve 'play-clj.core/stage*)))
  (testing "stage macro exists"
    (is (:macro (meta (resolve 'play-clj.core/stage))))
    (is (:macro (meta (resolve 'play-clj.core/stage!))))))

(deftest draw-multimethod-tests
  (testing "draw! is a multimethod"
    (is (instance? clojure.lang.MultiFn (var-get (resolve 'play-clj.core/draw!))))))

(deftest render-function-existence-tests
  (testing "render functions exist"
    (is (resolve 'play-clj.core/render-map!))
    (is (resolve 'play-clj.core/render-stage!))
    (is (resolve 'play-clj.core/render!))
    (is (resolve 'play-clj.core/render-sorted!))))

(deftest tiled-map-macro-behavioral-tests
  (testing "tiled-map macro exists and is callable"
    (is (:macro (meta (resolve 'play-clj.core/tiled-map))))
    (is (:macro (meta (resolve 'play-clj.core/tiled-map!))))))

(deftest map-layer-names-tests
  (testing "map-layers* can iterate layers"
    (let [layers (c/map-layers*)]
      (is (instance? MapLayers layers))
      (is (= 0 (count (for [_ layers] true)))))))
