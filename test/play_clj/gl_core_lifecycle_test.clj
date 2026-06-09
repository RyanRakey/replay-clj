(ns play-clj.gl-core-lifecycle-test
  (:require [clojure.test :refer :all]
            [play-clj.gl-fixture]
            [play-clj.core :as c]
            [play-clj.entities :as e])
  (:import [com.badlogic.gdx Game Gdx]
           [com.badlogic.gdx.graphics.g2d SpriteBatch TextureRegion]
           [com.badlogic.gdx.scenes.scene2d Actor Stage]))

(use-fixtures :once play-clj.gl-fixture/gl-setup)

(deftest normalize-tests
  (testing "normalize is a private fn accessible via var"
    (let [normalize (resolve 'play-clj.core/normalize)]
      (is (some? normalize)))))

(deftest defscreen-star-lifecycle-tests
  (testing "defscreen* creates screen with all lifecycle functions"
    (let [show-called (atom false)
          render-called (atom false)
          hide-called (atom false)
          resize-called (atom false)
          resume-called (atom false)
          pause-called (atom false)
          screen-atom (atom {})
          entities-atom (atom [])
          options {:on-show (fn [screen entities]
                              (reset! show-called true)
                              entities)
                   :on-render (fn [screen entities]
                                (reset! render-called true)
                                entities)
                   :on-hide (fn [screen entities]
                              (reset! hide-called true)
                              entities)
                   :on-resize (fn [screen entities]
                                (reset! resize-called true)
                                entities)
                   :on-resume (fn [screen entities]
                                (reset! resume-called true)
                                entities)
                   :on-pause (fn [screen entities]
                               (reset! pause-called true)
                               entities)}
          screen-obj (c/defscreen* screen-atom entities-atom options)]
      ;; Call show
      ((:show screen-obj))
      (is @show-called)
      ;; Call render
      ((:render screen-obj) 0.016)
      (is @render-called)
      ;; Call resize
      ((:resize screen-obj) 800 600)
      (is @resize-called)
      ;; Call pause/resume/hide
      ((:pause screen-obj))
      (is @pause-called)
      ((:resume screen-obj))
      (is @resume-called)
      ((:hide screen-obj))
      (is @hide-called)))

  (testing "defscreen* with nil handlers does not throw"
    (let [screen-atom (atom {})
          entities-atom (atom [])
          options {}
          screen-obj (c/defscreen* screen-atom entities-atom options)]
      (is (fn? (:show screen-obj)))
      (is (fn? (:render screen-obj)))
      ;; Calling nil handlers should not throw
      ((:show screen-obj))
      ((:render screen-obj) 0.016)
      ((:hide screen-obj))
      ((:pause screen-obj))
      ((:resume screen-obj))
      ((:resize screen-obj) 800 600)))

  (testing "defscreen* screen atom gets populated"
    (let [screen-atom (atom {})
          entities-atom (atom [])
          options {:on-show (fn [screen entities]
                              (c/update! screen :test-key 42)
                              entities)}
          screen-obj (c/defscreen* screen-atom entities-atom options)]
      ((:show screen-obj))
      (is (= 42 (:test-key @screen-atom))))))

(deftest defgame-star-tests
  (testing "defgame* creates a Game proxy"
    (let [created (atom false)
          game (c/defgame* {:on-create (fn [this] (reset! created true))})]
      (is (instance? Game game))
      ;; Call create to trigger on-create
      (.create game)
      (is @created)))

  (testing "defgame* with nil on-create does not throw"
    (let [game (c/defgame* {})]
      (is (instance? Game game))
      (.create game))))

(deftest update-with-real-screen-tests
  (testing "update! modifies screen atom through update-fn!"
    (let [screen-atom (atom {:x 0 :y 0})
          screen {:update-fn! (fn [f & args] (apply swap! screen-atom f args))
                  :renderer nil
                  :world nil}]
      (c/update! screen :x 10 :y 20)
      (is (= 10 (:x @screen-atom)))
      (is (= 20 (:y @screen-atom))))))

(deftest set-screen-wrapper-behavioral-tests
  (testing "set-screen-wrapper! sets the wrapper function"
    (let [wrapper-atom (atom nil)]
      (c/set-screen-wrapper! (fn [screen-atom screen-fn]
                               (reset! wrapper-atom :called)
                               (screen-fn)))
      (is (some? (resolve 'play-clj.core/wrapper)))
      ;; Restore default
      (c/set-screen-wrapper! (fn [screen-atom screen-fn] (screen-fn))))))

(deftest set-screen-with-mock-game-tests
  (testing "set-screen! creates a Screen and calls .setScreen on Game"
    (let [screen-set (atom nil)
          game (proxy [Game] []
                 (setScreen [s] (reset! screen-set s))
                 (getScreen [] @screen-set))
          show-called (atom false)
          render-called (atom false)
          screen-obj {:show (fn [] (reset! show-called true))
                      :render (fn [d] (reset! render-called true))
                      :hide (fn [])
                      :pause (fn [])
                      :resize (fn [w h])
                      :resume (fn [])
                      :screen (atom {})}]
      (c/set-screen! game screen-obj)
      (is (some? @screen-set))
      ;; Call show on the Screen
      (.show @screen-set)
      (is @show-called)
      ;; Call render
      (.render @screen-set 0.016)
      (is @render-called)))

  (testing "set-screen! with multiple screen objects"
    (let [screen-set (atom nil)
          game (proxy [Game] []
                 (setScreen [s] (reset! screen-set s))
                 (getScreen [] @screen-set))
          calls (atom [])
          screen1 {:show (fn [] (swap! calls conj :s1-show))
                   :render (fn [d] (swap! calls conj :s1-render))
                   :hide (fn [] (swap! calls conj :s1-hide))
                   :pause (fn [] (swap! calls conj :s1-pause))
                   :resize (fn [w h] (swap! calls conj :s1-resize))
                   :resume (fn [] (swap! calls conj :s1-resume))
                   :screen (atom {})}
          screen2 {:show (fn [] (swap! calls conj :s2-show))
                   :render (fn [d] (swap! calls conj :s2-render))
                   :hide (fn [] (swap! calls conj :s2-hide))
                   :pause (fn [] (swap! calls conj :s2-pause))
                   :resize (fn [w h] (swap! calls conj :s2-resize))
                   :resume (fn [] (swap! calls conj :s2-resume))
                   :screen (atom {})}]
      (c/set-screen! game screen1 screen2)
      (.show @screen-set)
      (is (some #(= :s1-show %) @calls))
      (is (some #(= :s2-show %) @calls)))))

(deftest screen-behavioral-tests
  (testing "screen! calls execute-fn! with screen function"
    (let [called (atom false)
          options {:on-my-event (fn [screen entities]
                                  (reset! called true)
                                  entities)}
          screen-obj {:execute-fn! (fn [func & args]
                                     (when func
                                       (func {} [])))
                      :options options}]
      (c/screen! screen-obj :on-my-event)
      (is @called)))

  (testing "screen! with extra options"
    (let [received-color (atom nil)
          options {:on-change-color (fn [screen entities]
                                      (reset! received-color (:color screen))
                                      entities)}
          screen-obj {:execute-fn! (fn [func & args]
                                     (when func
                                       (func (apply hash-map args) [])))
                      :options options}]
      (c/screen! screen-obj :on-change-color :color :blue)
      (is (= :blue @received-color)))))

(deftest render-stage-behavioral-tests
  (testing "render-stage! calls act and draw on Stage"
    (let [stage (Stage.)
          actor (Actor.)]
      (.addActor stage actor)
      (c/render-stage! {:renderer stage})
      (.dispose stage))))
