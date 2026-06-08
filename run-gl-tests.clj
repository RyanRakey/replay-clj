(require '[clojure.test :as t])
(require 'play-clj.gl-fixture)
(require 'play-clj.gl-entities-test)
(require 'play-clj.gl-core-test)
(require 'play-clj.gl-g2d-test)

(let [results (t/run-all-tests #"play-clj\.gl-.*-test")]
  (println "\n=== GL Test Summary ===")
  (println (format "Ran %d tests in %d namespaces" (:test results) (:ns results)))
  (println (format "%d failures, %d errors" (:fail results) (:error results)))
  (System/exit (if (and (zero? (:fail results)) (zero? (:error results))) 0 1)))
