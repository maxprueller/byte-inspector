(ns byte-inspector.rep
  (:require [clojure.set])
  (:import (java.nio.charset StandardCharsets)))

(defn hex [b]  b)

(defn integer [b] (try (BigInteger. (str b) 16)
                       (catch NumberFormatException e "...")))

(defn pad [s] (let [pad-amount (- 8 (count s))
                    zeroes (apply str (take pad-amount (repeat "0")))]
                (str zeroes s)))

(defn binary [b]
  (-> (integer b)
      (.toString 2)
      pad))

(defn byte-collection [b]
  (let [nth (fn [n] (subs b (* 2 n) (+ (* 2 n) 2)))
        length-in-byte  (/ (count b) 2)]
    (for [i (range length-in-byte)]
      (-> (nth i)
          (Integer/parseInt 16)))))

(defn ascii [b]
  (try
    (let [bytes (byte-collection b)]
      (->> (map char bytes)
           (apply str)))))

(defn utf8 [b]
  (let [bytes (byte-collection b)
        bytes (byte-array bytes)]
    (String. bytes StandardCharsets/UTF_8)))
    
  
(def repmap {hex "Hexadecimal"
             integer "Integer"
             ascii "ASCII"
             utf8 "UTF-8"
             binary "Binary"})

(defn from-string [s] (get (clojure.set/map-invert repmap) s))

(def strings (object-array (vals repmap)))


