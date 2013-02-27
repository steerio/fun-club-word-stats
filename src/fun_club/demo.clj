(ns fun-club.demo
  (:use [clojure.repl :only [doc apropos]]))

; This namespace hosts a couple of functions and macros that help demonstrate
; how lazy sequences work in Clojure.

(defn fib
  "Returns an infinite sequence of Fibonacci numbers"
  [a b]
  (lazy-seq
    (cons a
          (fib b (+' a b)))))

; These macros help us suppress output when we benchmark the fib function

(defmacro muzzle
  "Executes `forms`, returns nil"
  [& forms]
  `(do ~@forms nil))

(defmacro silent-time
  "Run `form` through the time macro, then count the characters of the
  stringified output instead of returning it"
  [form]
  `(format
     "Output of %d characters suppressed." 
     (count (pr-str (time ~form)))))
