(ns fun-club.demo-test
  (:require [fun-club.demo :as t])
  (:use clojure.test))

(deftest fib
  (is (= 89  (nth (t/fib 1 1) 10)))
  (is (= 610 (nth (t/fib 5 8) 10))))
