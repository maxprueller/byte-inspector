(ns byte-inspector.search
  (:require [byte-inspector.helpers :refer :all]))

(defn find-by-indcoll [index s coll file]
  (let [size (byte-size s)
        bytes (fn [i] (subvec file i (+ i size)))
        bytetext (fn [i] (apply str (bytes i)))]
    (some #(when (= s (bytetext %)) %) coll)))

(defn find-next [i s file]
  (let [last-index (inc (- (count file) (byte-size s)))
        coll (range i last-index)]
    (find-by-indcoll i s (range i last-index) file)))

(defn find-prev [i s file]
  (let [coll (-> (range i)
                 vec
                 rseq)]
    (find-by-indcoll i s coll file)))

(defn next-index [s file old-result]
  (let [index (if (= s (:str old-result))
                (inc (:ind old-result))
                0)]
    (if-let [new-position (find-next index s file)]
      {:ind new-position
       :str s})))

(defn prev-index [s file old-result]
  (let [index (if (= (:str old-result))
                (:ind old-result)
                (highest-index file))]
    (if-let [new-position (find-prev index s file)]
      {:ind new-position
       :str s})))

