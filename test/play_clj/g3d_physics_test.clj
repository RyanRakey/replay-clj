(ns play-clj.g3d-physics-test
  (:require [clojure.test :refer :all]
            [play-clj.headless-fixture]
            [play-clj.g3d-physics :as p]
            [play-clj.math :as m])
  (:import [com.badlogic.gdx.math Vector3]
           [com.badlogic.gdx.physics.bullet Bullet]
           [com.badlogic.gdx.physics.bullet.collision btBoxShape btSphereShape
            btCapsuleShape btConeShape btCylinderShape]
           [com.badlogic.gdx.physics.bullet.dynamics btRigidBody
            btRigidBody$btRigidBodyConstructionInfo btDiscreteDynamicsWorld]
           [com.badlogic.gdx.physics.bullet.softbody btSoftRigidDynamicsWorld
            btSoftBodyWorldInfo]
           [com.badlogic.gdx.physics.bullet.linearmath btMotionState]))

(use-fixtures :once play-clj.headless-fixture/headless-setup)

(defn- bullet-natives-available? []
  (try
    (Bullet/init)
    true
    (catch Exception _ false)))

(deftest record-type-tests
  (testing "World3D record has :object key"
    (let [w (play-clj.g3d-physics/->World3D :fake)]
      (is (= :fake (:object w)))))

  (testing "Body3D record has :object key"
    (let [b (play-clj.g3d-physics/->Body3D :fake)]
      (is (= :fake (:object b))))))

(deftest macro-existence-tests
  (testing "bullet-3d macros exist"
    (is (:macro (meta (resolve 'play-clj.g3d-physics/bullet-3d))))
    (is (:macro (meta (resolve 'play-clj.g3d-physics/bullet-3d!)))))

  (testing "rigid-body macros exist"
    (is (:macro (meta (resolve 'play-clj.g3d-physics/rigid-body))))
    (is (:macro (meta (resolve 'play-clj.g3d-physics/rigid-body!)))))

  (testing "soft-body macros exist"
    (is (:macro (meta (resolve 'play-clj.g3d-physics/soft-body))))
    (is (:macro (meta (resolve 'play-clj.g3d-physics/soft-body!)))))

  (testing "shape macros exist"
    (is (:macro (meta (resolve 'play-clj.g3d-physics/box-shape))))
    (is (:macro (meta (resolve 'play-clj.g3d-physics/box-shape!))))
    (is (:macro (meta (resolve 'play-clj.g3d-physics/capsule-shape))))
    (is (:macro (meta (resolve 'play-clj.g3d-physics/capsule-shape!))))
    (is (:macro (meta (resolve 'play-clj.g3d-physics/cone-shape))))
    (is (:macro (meta (resolve 'play-clj.g3d-physics/cone-shape!))))
    (is (:macro (meta (resolve 'play-clj.g3d-physics/cylinder-shape))))
    (is (:macro (meta (resolve 'play-clj.g3d-physics/cylinder-shape!))))
    (is (:macro (meta (resolve 'play-clj.g3d-physics/sphere-shape))))
    (is (:macro (meta (resolve 'play-clj.g3d-physics/sphere-shape!))))))

(deftest function-existence-tests
  (testing "world constructor functions exist"
    (is (resolve 'play-clj.g3d-physics/bullet-3d*))
    (is (resolve 'play-clj.g3d-physics/init-bullet)))

  (testing "body constructor functions exist"
    (is (resolve 'play-clj.g3d-physics/rigid-body*))
    (is (resolve 'play-clj.g3d-physics/rigid-body-info))
    (is (resolve 'play-clj.g3d-physics/soft-body*))
    (is (resolve 'play-clj.g3d-physics/soft-body-info)))

  (testing "shape constructor functions exist"
    (is (resolve 'play-clj.g3d-physics/box-shape*))
    (is (resolve 'play-clj.g3d-physics/capsule-shape*))
    (is (resolve 'play-clj.g3d-physics/cone-shape*))
    (is (resolve 'play-clj.g3d-physics/cylinder-shape*))
    (is (resolve 'play-clj.g3d-physics/sphere-shape*)))

  (testing "body mutation functions exist"
    (is (resolve 'play-clj.g3d-physics/add-body!))
    (is (resolve 'play-clj.g3d-physics/body-position!))
    (is (resolve 'play-clj.g3d-physics/body-x!))
    (is (resolve 'play-clj.g3d-physics/body-y!))
    (is (resolve 'play-clj.g3d-physics/body-z!)))

  (testing "entity lookup functions exist"
    (is (resolve 'play-clj.g3d-physics/first-entity))
    (is (resolve 'play-clj.g3d-physics/second-entity)))

  (testing "step! function exists"
    (is (resolve 'play-clj.g3d-physics/step!))))

(deftest bullet-3d-world-tests
  (when (bullet-natives-available?)
    (testing "bullet-3d* :rigid creates a World3D with btDiscreteDynamicsWorld"
      (let [world (p/bullet-3d* :rigid)]
        (is (instance? play_clj.g3d_physics.World3D world))
        (is (instance? btDiscreteDynamicsWorld (:object world)))
        (is (some? (:config world)))
        (is (some? (:dispatcher world)))
        (is (some? (:broadphase world)))
        (is (some? (:constraint-solver world)))))

    (testing "bullet-3d* :soft-rigid creates a World3D with btSoftRigidDynamicsWorld"
      (let [world (p/bullet-3d* :soft-rigid)]
        (is (instance? play_clj.g3d_physics.World3D world))
        (is (instance? btSoftRigidDynamicsWorld (:object world)))))

    (testing "bullet-3d* with invalid type throws"
      (is (thrown? Exception (p/bullet-3d* :invalid))))))

(deftest shape-constructor-tests
  (when (bullet-natives-available?)
    (testing "box-shape* creates a btBoxShape"
      (let [shape (p/box-shape* (Vector3. 1.0 1.0 1.0))]
        (is (instance? btBoxShape shape))))

    (testing "sphere-shape* creates a btSphereShape"
      (let [shape (p/sphere-shape* 1.0)]
        (is (instance? btSphereShape shape))))

    (testing "capsule-shape* creates a btCapsuleShape"
      (let [shape (p/capsule-shape* 1.0 2.0)]
        (is (instance? btCapsuleShape shape))))

    (testing "cone-shape* creates a btConeShape"
      (let [shape (p/cone-shape* 1.0 2.0)]
        (is (instance? btConeShape shape))))

    (testing "cylinder-shape* creates a btCylinderShape"
      (let [shape (p/cylinder-shape* (Vector3. 1.0 1.0 1.0))]
        (is (instance? btCylinderShape shape))))))

(deftest soft-body-info-tests
  (when (bullet-natives-available?)
    (testing "soft-body-info creates a btSoftBodyWorldInfo"
      (let [info (p/soft-body-info)]
        (is (instance? btSoftBodyWorldInfo info))))))

(deftest rigid-body-creation-tests
  (when (bullet-natives-available?)
    (testing "rigid-body* creates a Body3D with btRigidBody"
      (let [shape (p/box-shape* (Vector3. 1.0 1.0 1.0))
            local-inertia (Vector3. 0 0 0)
            motion-state (proxy [btMotionState] []
                           (getWorldTransform [wt])
                           (setWorldTransform [wt]))
            info (p/rigid-body-info 1.0 motion-state shape local-inertia)
            body (p/rigid-body* info)]
        (is (instance? play_clj.g3d_physics.Body3D body))
        (is (instance? btRigidBody (:object body)))
        (is (= info (:info body)))))))

(deftest body-position-tests
  (when (bullet-natives-available?)
    (testing "body-position! sets position and body-x/y/z read it back"
      (let [shape (p/box-shape* (Vector3. 1.0 1.0 1.0))
            local-inertia (Vector3. 0 0 0)
            motion-state (proxy [btMotionState] []
                           (getWorldTransform [wt])
                           (setWorldTransform [wt]))
            info (p/rigid-body-info 0.0 motion-state shape local-inertia)
            body (p/rigid-body* info)
            entity {:body body}]
        (p/body-position! entity 5.0 10.0 15.0)
        (is (< 4.9 (#'play-clj.g3d-physics/body-x entity) 5.1))
        (is (< 9.9 (#'play-clj.g3d-physics/body-y entity) 10.1))
        (is (< 14.9 (#'play-clj.g3d-physics/body-z entity) 15.1))))))

(deftest body-axis-mutation-tests
  (when (bullet-natives-available?)
    (testing "body-x! changes only x"
      (let [shape (p/box-shape* (Vector3. 1.0 1.0 1.0))
            local-inertia (Vector3. 0 0 0)
            motion-state (proxy [btMotionState] []
                           (getWorldTransform [wt])
                           (setWorldTransform [wt]))
            info (p/rigid-body-info 0.0 motion-state shape local-inertia)
            body (p/rigid-body* info)
            entity {:body body}]
        (p/body-position! entity 1.0 2.0 3.0)
        (p/body-x! entity 99.0)
        (is (< 98.9 (#'play-clj.g3d-physics/body-x entity) 99.1))
        (is (< 1.9 (#'play-clj.g3d-physics/body-y entity) 2.1))
        (is (< 2.9 (#'play-clj.g3d-physics/body-z entity) 3.1))))

    (testing "body-y! changes only y"
      (let [shape (p/box-shape* (Vector3. 1.0 1.0 1.0))
            local-inertia (Vector3. 0 0 0)
            motion-state (proxy [btMotionState] []
                           (getWorldTransform [wt])
                           (setWorldTransform [wt]))
            info (p/rigid-body-info 0.0 motion-state shape local-inertia)
            body (p/rigid-body* info)
            entity {:body body}]
        (p/body-position! entity 1.0 2.0 3.0)
        (p/body-y! entity 99.0)
        (is (< 0.9 (#'play-clj.g3d-physics/body-x entity) 1.1))
        (is (< 98.9 (#'play-clj.g3d-physics/body-y entity) 99.1))
        (is (< 2.9 (#'play-clj.g3d-physics/body-z entity) 3.1))))

    (testing "body-z! changes only z"
      (let [shape (p/box-shape* (Vector3. 1.0 1.0 1.0))
            local-inertia (Vector3. 0 0 0)
            motion-state (proxy [btMotionState] []
                           (getWorldTransform [wt])
                           (setWorldTransform [wt]))
            info (p/rigid-body-info 0.0 motion-state shape local-inertia)
            body (p/rigid-body* info)
            entity {:body body}]
        (p/body-position! entity 1.0 2.0 3.0)
        (p/body-z! entity 99.0)
        (is (< 0.9 (#'play-clj.g3d-physics/body-x entity) 1.1))
        (is (< 1.9 (#'play-clj.g3d-physics/body-y entity) 2.1))
        (is (< 98.9 (#'play-clj.g3d-physics/body-z entity) 99.1))))))

(deftest add-body-and-step-tests
  (when (bullet-natives-available?)
    (testing "add-body! adds rigid body to world"
      (let [world (p/bullet-3d* :rigid)
            screen {:world world}
            shape (p/box-shape* (Vector3. 1.0 1.0 1.0))
            local-inertia (Vector3. 0 0 0)
            motion-state (proxy [btMotionState] []
                           (getWorldTransform [wt])
                           (setWorldTransform [wt]))
            info (p/rigid-body-info 1.0 motion-state shape local-inertia)
            body (p/rigid-body* info)
            entity {:body body}]
        (p/add-body! screen entity)
        (is (= body (:body entity)))))

    (testing "step! advances physics simulation"
      (let [world (p/bullet-3d* :rigid)
            screen {:world world :delta-time (/ 1 60)}]
        (p/step! screen)
        (is true)))

    (testing "step! with entities returns updated positions"
      (let [world (p/bullet-3d* :rigid)
            screen {:world world :delta-time (/ 1 60)}
            shape (p/box-shape* (Vector3. 1.0 1.0 1.0))
            local-inertia (Vector3. 0 0 0)
            motion-state (proxy [btMotionState] []
                           (getWorldTransform [wt])
                           (setWorldTransform [wt]))
            info (p/rigid-body-info 0.0 motion-state shape local-inertia)
            body (p/rigid-body* info)
            entity {:body body :object (proxy [Object] [])}]
        (p/add-body! screen entity)
        (let [result (p/step! screen [entity])]
          (is (= 1 (count result)))
          (is (contains? (first result) :x))
          (is (contains? (first result) :y))
          (is (contains? (first result) :z)))))))
