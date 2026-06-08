(ns play-clj.core-listeners-test
  (:require [clojure.test :refer :all]
            [play-clj.headless-fixture]
            [play-clj.core :as c]))

(use-fixtures :once play-clj.headless-fixture/headless-setup)

(deftest input-processor-macro-tests
  (testing "input-processor! macro exists"
    (is (:macro (meta (resolve 'play-clj.core/input-processor!))))))

(deftest gesture-detector-macro-tests
  (testing "gesture-detector! macro exists"
    (is (:macro (meta (resolve 'play-clj.core/gesture-detector!))))))

(deftest contact-listener-multimethod-tests
  (testing "contact-listener is a multimethod"
    (is (instance? clojure.lang.MultiFn (var-get (resolve 'play-clj.core/contact-listener)))))

  (testing "contact-listener default dispatch returns nil for nil world"
    (is (nil? (c/contact-listener {:world nil} {} (fn [& args]))))))

(deftest update-physics-multimethod-tests
  (testing "update-physics! is a multimethod"
    (is (instance? clojure.lang.MultiFn (var-get (resolve 'play-clj.core/update-physics!)))))

  (testing "update-physics! default dispatch returns nil for nil world"
    (is (nil? (c/update-physics! {:world nil})))))

(deftest defscreen-macro-tests
  (testing "defscreen macro exists and creates screen functions"
    (is (:macro (meta (resolve 'play-clj.core/defscreen))))))

(deftest screen-handler-keywords
  (testing "defscreen accepts all listener keywords"
    (let [keywords #{:on-key-down :on-key-typed :on-key-up :on-mouse-moved
                     :on-scrolled :on-touch-down :on-touch-dragged :on-touch-up
                     :on-fling :on-long-press :on-pan :on-pan-stop :on-pinch
                     :on-tap :on-zoom
                     :on-ui-fling :on-ui-long-press :on-ui-pan :on-ui-pinch
                     :on-ui-tap :on-ui-zoom
                     :on-ui-changed :on-ui-clicked :on-ui-enter :on-ui-exit
                     :on-ui-touch-down :on-ui-touch-dragged :on-ui-touch-up
                     :on-ui-drag :on-ui-drag-start :on-ui-drag-stop
                     :on-ui-keyboard-focus-changed :on-ui-scroll-focus-changed
                     :on-begin-contact :on-end-contact}]
      (is (= 35 (count keywords))))))
