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

; Command line entry point

(defn main*
  "Prints the ten most frequent words in a sequence of lines along with the
   number of their appearances"
  [ls]
  (doseq [[word freq] (n-most-frequent 10 (words ls))]
    (println (format "%s: %d" word freq))))

(defn bench [stream]
  (let [ls (lines stream)]
    (cr/bench (n-most-frequent 10 (words ls)))))

(defn -main [& args]
  (case (first args)
    ; The -b option does a benchmark. This implies head retention.
    ; There's no other way: we cannot reprocess standard input.
    ("-b" "--bench") (bench System/in)

    ; This form also uses let, but the reference is not being used later.
    ; Clojure's compiler realizes that the reference is not being used and
    ; does optimization to avoid head retention.
    ("-l" "--let") (let [ls (lines System/in)]
                      (main* ls))

    ; This becomes obvious if we actually use the reference after processing.
    ; Head retention is not being avoided here.
    ("-k" "--keep") (let [ls (lines System/in)]
                      (main* ls)
                      (second ls))

    ; Default: run once, don't keep a reference to the sequence.
    (main* (lines System/in))))
