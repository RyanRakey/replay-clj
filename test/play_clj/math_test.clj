(ns play-clj.math-test
  (:require [clojure.test :refer :all]
            [play-clj.math :as m])
  (:import [com.badlogic.gdx.math Bezier BSpline CatmullRomSpline ConvexHull
            DelaunayTriangulator EarClippingTriangulator Ellipse FloatCounter Frustum
            GridPoint2 GridPoint3 Matrix3 Plane Polygon Polyline Quaternion Vector2
            Vector3 Matrix4 WindowedMean]))

(deftest vector-creation-tests
  (testing "vector-2 creates a 2D vector"
    (let [v (m/vector-2* 10 20)]
      (is (instance? Vector2 v))
      (is (= 10.0 (.x v)))
      (is (= 20.0 (.y v)))))

  (testing "vector-2 defaults to zero"
    (let [v (m/vector-2*)]
      (is (= 0.0 (.x v)))
      (is (= 0.0 (.y v)))))

  (testing "vector-3 creates a 3D vector"
    (let [v (m/vector-3* 1 2 3)]
      (is (instance? Vector3 v))
      (is (= 1.0 (.x v)))
      (is (= 2.0 (.y v)))
      (is (= 3.0 (.z v))))))

(deftest vector-macro-tests
  (testing "vector-2 macro creates and calls methods"
    (let [v (m/vector-2 5 10 :add 3 3)]
      (is (instance? Vector2 v))
      (is (= 8.0 (.x v)))
      (is (= 13.0 (.y v)))))

  (testing "vector-3 macro creates and calls methods"
    (let [v (m/vector-3 1 1 1 :add 2 3 4)]
      (is (= 3.0 (.x v)))
      (is (= 4.0 (.y v)))
      (is (= 5.0 (.z v))))))

(deftest matrix-tests
  (testing "matrix-4 creates identity matrix by default"
    (let [mat (m/matrix-4*)]
      (is (instance? Matrix4 mat))
      ;; Verify identity matrix has 1s on diagonal
      (is (= 1.0 (aget (.val mat) 0)))
      (is (= 1.0 (aget (.val mat) 5)))
      (is (= 1.0 (aget (.val mat) 10)))
      (is (= 1.0 (aget (.val mat) 15)))))

  (testing "matrix-4 with float array"
    (let [mat (m/matrix-4* (float-array [1 0 0 0
                                         0 1 0 0
                                         0 0 1 0
                                         0 0 0 1]))]
      (is (= 1.0 (aget (.val mat) 0))))))

(deftest geometry-tests
  (testing "rectangle creation"
    (let [r (m/rectangle* 0 0 100 50)]
      (is (= 0.0 (.x r)))
      (is (= 0.0 (.y r)))
      (is (= 100.0 (.width r)))
      (is (= 50.0 (.height r)))))

  (testing "circle creation"
    (let [c (m/circle* 10 20 5)]
      (is (= 10.0 (.x c)))
      (is (= 20.0 (.y c)))
      (is (= 5.0 (.radius c)))))

  (testing "ellipse creation"
    (let [e (m/ellipse* 5 5 30 20)]
      (is (= 5.0 (.x e)))
      (is (= 5.0 (.y e)))
      (is (= 30.0 (.width e)))
      (is (= 20.0 (.height e))))))

(deftest grid-point-tests
  (testing "grid-point-2 creation"
    (let [gp (m/grid-point-2* 3 4)]
      (is (= 3 (.x gp)))
      (is (= 4 (.y gp)))))

  (testing "grid-point-3 creation"
    (let [gp (m/grid-point-3* 1 2 3)]
      (is (= 1 (.x gp)))
      (is (= 2 (.y gp)))
      (is (= 3 (.z gp))))))

(deftest math-utils-tests
  (testing "math! static methods"
    (is (= 2 (m/math! :ceil 1.2)))
    (is (= 1 (m/math! :floor 1.9)))
    (is (< 0.99 (m/math! :sin (/ Math/PI 2))))
    (is (< -0.01 (m/math! :cos (/ Math/PI 2)) 0.01))))

(deftest quaternion-tests
  (testing "quaternion creation"
    (let [q (m/quaternion* 0 0 0 1)]
      (is (instance? Quaternion q))
      (is (= 0.0 (.x q)))
      (is (= 0.0 (.y q)))
      (is (= 0.0 (.z q)))
      (is (= 1.0 (.w q))))))

(deftest polygon-tests
  (testing "polygon creation"
    (let [poly (m/polygon* (float-array [0 0 10 0 10 10]))]
      (is (instance? Polygon poly))
      (is (= 6 (count (.getVertices poly)))))))

(deftest polyline-tests
  (testing "polyline creation"
    (let [line (m/polyline* (float-array [0 0 10 10]))]
      (is (instance? Polyline line))
      (is (= 4 (count (.getVertices line)))))))

(deftest bezier-tests
  (testing "bezier creation"
    (let [b (m/bezier* [(Vector2. 0 0) (Vector2. 1 1)])]
      (is (instance? Bezier b)))))

(deftest b-spline-tests
  (testing "b-spline creation"
    (let [s (m/b-spline* [(Vector2. 0 0) (Vector2. 1 1) (Vector2. 2 0) (Vector2. 3 1)] 3 false)]
      (is (instance? BSpline s)))))

(deftest catmull-rom-spline-tests
  (testing "catmull-rom-spline creation"
    (let [s (m/catmull-rom-spline* [(Vector2. 0 0) (Vector2. 1 1)] false)]
      (is (instance? CatmullRomSpline s)))))

(deftest windowed-mean-tests
  (testing "windowed-mean creation"
    (let [wm (m/windowed-mean* 5)]
      (is (instance? WindowedMean wm))
      (is (= 5 (.getWindowSize wm))))))

(deftest vector-2-chaining-tests
  (testing "vector-2 macro chains method calls"
    (let [v (m/vector-2 1 2 :set 10 20)]
      (is (= 10.0 (.x v)))
      (is (= 20.0 (.y v))))))

(deftest vector-3-chaining-tests
  (testing "vector-3 macro chains method calls"
    (let [v (m/vector-3 1 2 3 :set 10 20 30)]
      (is (= 10.0 (.x v)))
      (is (= 20.0 (.y v)))
      (is (= 30.0 (.z v))))))

(deftest rectangle-chaining-tests
  (testing "rectangle macro chains method calls"
    (let [r (m/rectangle 0 0 100 50 :set-position 10 20)]
      (is (= 10.0 (.x r)))
      (is (= 20.0 (.y r)))
      (is (= 100.0 (.width r)))
      (is (= 50.0 (.height r))))))

(deftest circle-chaining-tests
  (testing "circle macro chains method calls"
    (let [c (m/circle 0 0 10 :set-position 5 5)]
      (is (= 5.0 (.x c)))
      (is (= 5.0 (.y c)))
      (is (= 10.0 (.radius c))))))

(deftest matrix-3-chaining-tests
  (testing "matrix-3 macro chains method calls"
    (let [mat (m/matrix-3 (float-array [1 0 0 0 1 0 0 0 1]) :scale 2 2)]
      (is (instance? Matrix3 mat)))))

(deftest matrix-4-chaining-tests
  (testing "matrix-4 macro chains method calls"
    (let [mat (m/matrix-4 (float-array [1 0 0 0 0 1 0 0 0 0 1 0 0 0 0 1]) :scale 2 2 2)]
      (is (instance? Matrix4 mat)))))

(deftest polygon-chaining-tests
  (testing "polygon macro chains method calls"
    (let [poly (m/polygon (float-array [0 0 10 0 10 10]) :set-position 5 5)]
      (is (instance? Polygon poly))
      (is (= 5.0 (.getX poly))))))

(deftest polyline-chaining-tests
  (testing "polyline macro chains method calls"
    (let [line (m/polyline (float-array [0 0 10 10]) :set-origin 5 5)]
      (is (instance? Polyline line))
      (is (= 5.0 (.getOriginX line))))))

(deftest bezier-chaining-tests
  (testing "bezier macro chains method calls"
    (let [b (m/bezier [(Vector2. 0 0) (Vector2. 1 1)] :value-at (Vector2.) 0.5)]
      (is (instance? Bezier b)))))

(deftest catmull-rom-spline-chaining-tests
  (testing "catmull-rom-spline macro chains method calls"
    (let [s (m/catmull-rom-spline [(Vector2. 0 0) (Vector2. 1 1)] false :value-at (Vector2.) 0.5)]
      (is (instance? CatmullRomSpline s)))))

(deftest quaternion-chaining-tests
  (testing "quaternion macro chains method calls"
    ;; Quaternion.set takes (x y z w) not (w x y z)
    (let [q (m/quaternion 0 0 0 1 :set 0 0 0 1)]
      (is (instance? Quaternion q))
      (is (= 1.0 (.w q))))))

(deftest intersector-tests
  (testing "intersector! static methods"
    (is (boolean? (m/intersector! :is-point-in-triangle 0 0 0 0 1 0 0.5 0.5)))))

(deftest frustum-tests
  (testing "frustum creation"
    (let [f (m/frustum*)]
      (is (instance? Frustum f)))))

(deftest plane-tests
  (testing "plane creation from vectors"
    (let [p (m/plane* (Vector3. 0 1 0) (Vector3. 0 0 0) (Vector3. 1 0 0))]
      (is (instance? Plane p)))))

(deftest convex-hull-tests
  (testing "convex-hull creation"
    (let [ch (m/convex-hull*)]
      (is (instance? ConvexHull ch)))))

(deftest delaunay-triangulator-tests
  (testing "delaunay-triangulator creation"
    (let [dt (m/delaunay-triangulator*)]
      (is (instance? DelaunayTriangulator dt)))))

(deftest ear-clipping-triangulator-tests
  (testing "ear-clipping-triangulator creation"
    (let [ect (m/ear-clipping-triangulator*)]
      (is (instance? EarClippingTriangulator ect)))))

(deftest float-counter-tests
  (testing "float-counter creation"
    (let [fc (m/float-counter* 10)]
      (is (instance? FloatCounter fc)))))

(deftest grid-point-mutation-tests
  (testing "grid-point-2! calls method"
    (let [gp (m/grid-point-2* 1 2)]
      (m/grid-point-2! gp :set 10 20)
      (is (= 10 (.x gp)))
      (is (= 20 (.y gp)))))
  (testing "grid-point-3! calls method"
    (let [gp (m/grid-point-3* 1 2 3)]
      (m/grid-point-3! gp :set 10 20 30)
      (is (= 10 (.x gp)))
      (is (= 20 (.y gp)))
      (is (= 30 (.z gp))))))

(deftest vector-mutation-tests
  (testing "vector-2! calls method"
    (let [v (m/vector-2* 1 2)]
      (m/vector-2! v :set 10 20)
      (is (= 10.0 (.x v)))
      (is (= 20.0 (.y v)))))
  (testing "vector-3! calls method"
    (let [v (m/vector-3* 1 2 3)]
      (m/vector-3! v :set 10 20 30)
      (is (= 10.0 (.x v)))
      (is (= 20.0 (.y v)))
      (is (= 30.0 (.z v))))))

(deftest rectangle-mutation-tests
  (testing "rectangle! calls method"
    (let [r (m/rectangle* 0 0 100 50)]
      (m/rectangle! r :set-position 10 20)
      (is (= 10.0 (.x r)))
      (is (= 20.0 (.y r))))))

(deftest circle-mutation-tests
  (testing "circle! calls method"
    (let [c (m/circle* 0 0 10)]
      (m/circle! c :set-position 5 5)
      (is (= 5.0 (.x c)))
      (is (= 5.0 (.y c))))))

(deftest ellipse-mutation-tests
  (testing "ellipse! calls method"
    (let [e (m/ellipse* 0 0 30 20)]
      (m/ellipse! e :set-position 10 10)
      (is (= 10.0 (.x e)))
      (is (= 10.0 (.y e))))))

(deftest matrix-mutation-tests
  (testing "matrix-3! calls method"
    (let [mat (m/matrix-3*)]
      (m/matrix-3! mat :scale 2 2)
      (is (instance? Matrix3 mat))))
  (testing "matrix-4! calls method"
    (let [mat (m/matrix-4*)]
      (m/matrix-4! mat :scale 2 2 2)
      (is (instance? Matrix4 mat)))))

(deftest geometry!-tests
  (testing "geometry! macro exists"
    (is (:macro (meta (resolve 'play-clj.math/geometry!))))))

(deftest plane-side-tests
  (testing "plane-side macro exists"
    (is (:macro (meta (resolve 'play-clj.math/plane-side))))))

(deftest interpolation-macro-tests
  (testing "interpolation macro exists"
    (is (:macro (meta (resolve 'play-clj.math/interpolation))))))
