(ns byte-inspector.panels.surroundings
  (:require [byte-inspector.helpers :refer :all]
            [byte-inspector.left-panel :as left-panel]
            [byte-inspector.region :as r])
  (:import (javax.swing JLabel)))

(defn label-before [i index file]
  (let [index (- index (inc i))]
    (JLabel. (nth-byte index file))))

(defn add-n-labels-before [panel n index file]
  (dotimes [i n]
    (let [label (label-before i index file)]
      (.add panel label))))

(defn label-at [context]
  (JLabel. (str "|" (r/display-value context) "|")))

(defn label-after [n index size file]
  (JLabel. (nth-byte (+ index size n) file)))

(defn add-n-labels-after [panel n index size file]
  (dotimes [i n]
    (let [label (label-after i index size file)]
      (.add panel label))))

(defn make [context]
  (let [{:keys [region file]} context
        panel (left-panel/make)
        index (r/index region)
        surroundings (r/surroundings region)
        size (r/size region)
        n-before (min surroundings index)
        n-after (min surroundings (- (count file) index size))]
    (doto panel
      (.add (JLabel. "Surroundings: "))
      (add-n-labels-before n-before index file)
      (.add  (label-at context))
      (add-n-labels-after n-after index size file))))

