(ns fun-club.word-stats-test
  (:require [fun-club.word-stats :as t])
  (:use clojure.test))

(deftest words
  (is (= (t/words '("foo bar" "baz")) '("foo" "bar" "baz")))
  (is (= (t/words '()) '())))

(deftest n-most-frequent
  (let [xs (concat (repeat 5 "foo") (repeat 3 "bar") (repeat 4 "baz"))]
    (is (= (map first (t/n-most-frequent 3 xs)) (list "foo" "baz" "bar")))
    (is (= (map second (t/n-most-frequent 3 xs)) (list 5 4 3)))
    (is (= (count (t/n-most-frequent 10 xs)) 3))
    (is (= (count (t/n-most-frequent 2 xs)) 2))))
