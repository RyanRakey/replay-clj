(ns play-clj.utils-test
  (:require [clojure.test :refer :all]
            [play-clj.utils :as u]))

(deftest key-conversion-tests
  (testing "key->upper converts keywords to UPPER_CASE with underscores"
    (is (= "NORMAL" (u/key->upper :normal)))
    (is (= "LOOP_PINGPONG" (u/key->upper :loop-pingpong)))
    (is (= "ALPHA" (u/key->upper :alpha))))

  (testing "key->pascal converts keywords to PascalCase"
    (is (= "Normal" (u/key->pascal :normal)))
    (is (= "LoopPingpong" (u/key->pascal :loop-pingpong)))
    (is (= "TextureRegion" (u/key->pascal :texture-region))))

  (testing "key->camel converts keywords to camelCase"
    (is (= "normal" (u/key->camel :normal)))
    (is (= "loopPingpong" (u/key->camel :loop-pingpong)))
    (is (= "textureRegion" (u/key->camel :texture-region)))))

(deftest gdx-symbol-tests
  (testing "gdx returns fully-qualified libGDX symbols"
    (is (= 'com.badlogic.gdx.graphics.Color (u/gdx :graphics :Color)))
    (is (= 'com.badlogic.gdx.math.Vector2 (u/gdx :math :Vector2))))

  (testing "gdx-field returns static field references"
    (is (= 'com.badlogic.gdx.graphics.GL20/GL_COLOR_BUFFER_BIT
           (u/gdx-field :graphics :GL20 "GL_COLOR_BUFFER_BIT")))
    (is (= 'com.badlogic.gdx.graphics.Color/RED
           (u/gdx-field :graphics :Color "RED"))))

  (testing "gdx-class returns inner class references"
    (is (= 'com.badlogic.gdx.graphics.Pixmap$Format
           (u/gdx-class :graphics "Pixmap" "Format")))))

(deftest get-obj-tests
  (testing "get-obj returns value from map"
    (is (= 42 (u/get-obj {:x 42} :x))))

  (testing "get-obj throws on missing key"
    (is (thrown? Exception (u/get-obj {:x 42} :y))))

  (testing "get-obj returns obj when not a map"
    (is (= "hello" (u/get-obj "hello" :anything)))))

(deftest key-conversion-edge-cases
  (testing "single word keywords"
    (is (= "A" (u/key->upper :a)))
    (is (= "A" (u/key->pascal :a)))
    (is (= "a" (u/key->camel :a))))

  (testing "many-part keywords"
    (is (= "ONE_TWO_THREE_FOUR" (u/key->upper :one-two-three-four)))
    (is (= "OneTwoThreeFour" (u/key->pascal :one-two-three-four)))
    (is (= "oneTwoThreeFour" (u/key->camel :one-two-three-four)))))

(deftest call!-macro-tests
  (testing "call! invokes method on object"
    (is (= 6 (u/call! "hello world" :index-of "w")))
    (is (= "HELLO" (u/call! "hello" :to-upper-case)))))

(deftest fields!-macro-tests
  (testing "fields! sets fields on object"
    (let [point (java.awt.Point. 5 10)]
      (u/fields! point :x 100 :y 200)
      (is (= 100 (.x point)))
      (is (= 200 (.y point))))))

(deftest throw-key-not-found-tests
  (testing "get-obj throws on missing key"
    (is (thrown? Exception (u/get-obj {} :missing)))
    (is (thrown-with-msg? Exception #"not found" (u/get-obj {} :missing)))))

(deftest data-structure-tests
  (testing "gdx-array creates libGDX Array from Clojure vector"
    (let [arr (u/gdx-array* [1 2 3])]
      (is (= 3 (.size arr)))
      (is (= 1 (.get arr 0)))
      (is (= 2 (.get arr 1)))
      (is (= 3 (.get arr 2)))))

  (testing "gdx-array-map creates libGDX ArrayMap from Clojure map"
    (let [m (u/gdx-array-map* {:a 1 :b 2})]
      (is (= 2 (.size m)))
      (is (= 1 (.get m :a)))
      (is (= 2 (.get m :b))))))

(deftest timer-tests
  (testing "track-timers! and stop-timers!"
    (u/track-timers!)
    (is (set? @play-clj.utils/*timers*))
    (u/stop-timers!)))

(deftest gdx-array-macro-tests
  (testing "gdx-array macro chains method calls"
    (let [arr (u/gdx-array [1 2 3] :add 4)]
      (is (= 4 (.size arr))))))

(deftest gdx-array-map-macro-tests
  (testing "gdx-array-map macro chains method calls"
    ;; Use string keys so :put's args aren't parsed as method keywords
    (let [m (u/gdx-array-map {"a" 1} :put "b" 2)]
      (is (= 2 (.size m))))))
