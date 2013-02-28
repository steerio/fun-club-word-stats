(ns fun-club.parallelize
  (:refer-clojure :exclude [frequencies])
  (:require [clojure.core :as c]))

; Parallelization that unfortunately only makes it slower with its overhead.

(def ^:dynamic *parallel-threads*
  (+ 2 (.. Runtime getRuntime availableProcessors)))

(defn- round-robin [n coll]
  (letfn [(part [i c]
            (lazy-seq
              (if (pos? i)
                (cons
                  (take-nth n c)
                  (if-let [rst (next c)]
                    (part (dec i) rst))))))]
    (part n coll)))

(defn frequencies [xs]
  (apply
    merge-with +
    (map deref
         (map #(future (c/frequencies %))
              (round-robin *parallel-threads* xs)))))
