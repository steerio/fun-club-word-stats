(ns fun-club.word-stats
  (:require [clojure.java.io :as io]
            [criterium.core :as cr])
  (:use [clojure.string :only [lower-case]]))

; The three "general purpose" functions doing the job

(defn lines
  "Returns a sequence of lines from an inputstream"
  [stream]
  (line-seq (io/reader stream)))

(defn words
  "Returns a sequence of words consuming a sequence of lines"
  [ls]
  (mapcat 
    (fn [line] (re-seq #"[a-z]+" (lower-case line)))
    ls))

(defn n-most-frequent
  "Returns the `n` most frequent items from `xs` as a sequence of [item count]
   vectors"
  [n xs]
  (take n
        (sort-by
          second >
          (seq (frequencies xs)))))

; Separate benchmark function for easy access from the REPL

(defn bench [stream]
  (let [ls (lines stream)]
    (cr/bench (n-most-frequent 10 (words ls)))))

; Command line entry point

(defn -main [& args]
  (case (first args)
    ; The -b option does a benchmark. This implies head retention.
    ; There's no other way: we cannot reprocess standard input.
    ("-b" "--bench") (bench System/in)

    ; Room for more switches here.

    ; Default: run once, don't keep a reference to the sequence.
    (doseq [[word freq] (n-most-frequent 10 (words (lines System/in)))]
      (println (format "%s: %d" word freq)))))
