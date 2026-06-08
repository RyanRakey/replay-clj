(ns play-clj.repl-test
  (:require [clojure.test :refer :all]
            [play-clj.repl :as r]))

(deftest s-tests
  (testing "s returns derefed screen map"
    (let [screen-atom (atom {:camera :test})]
      (is (= {:camera :test} (r/s {:screen screen-atom}))))))

(deftest s!-tests
  (testing "s! assoc values into screen atom"
    (let [screen-atom (atom {})]
      (r/s! {:screen screen-atom} :x 1)
      (is (= {:x 1} @screen-atom)))))

(deftest e-tests
  (testing "e returns derefed entities"
    (let [entities-atom (atom [{:id 1} {:id 2}])]
      (is (= [{:id 1} {:id 2}] (r/e {:entities entities-atom})))))

  (testing "e with filter returns filtered entities"
    (let [entities-atom (atom [{:id 1} {:id 2 :active? true}])]
      (is (= [{:id 2 :active? true}] (r/e :active? {:entities entities-atom}))))))

(deftest e!-tests
  (testing "e! assoc values into matching entities"
    (let [entities-atom (atom [{:id 1} {:id 2 :active? true}])]
      (r/e! :active? {:entities entities-atom} :health 100)
      (is (= [{:id 1} {:id 2 :active? true :health 100}] @entities-atom)))))
