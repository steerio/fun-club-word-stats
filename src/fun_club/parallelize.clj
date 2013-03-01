(ns fun-club.parallelize
  (:refer-clojure :exclude [frequencies])
  (:require [clojure.core :as c]))

; Parallelization that unfortunately only makes it slower with its overhead.

(def ^:dynamic *parallel-threads*
  (+ 2 (.. Runtime getRuntime availableProcessors)))

(defn round-robin [n coll]
  (letfn [(part [curr coll*]
            (lazy-seq
              (if (pos? curr)
                (cons
                  (take-nth n coll*)
                  (if-let [rst (next coll*)]
                    (part (dec curr) rst))))))]
    (part n coll)))

(defn frequencies [xs]
  (apply
    merge-with +
    (pmap c/frequencies (round-robin *parallel-threads* xs))))
