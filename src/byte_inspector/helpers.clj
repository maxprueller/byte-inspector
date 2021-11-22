(ns byte-inspector.helpers
  (:import (java.awt.event ActionListener)))

(defn wrap-dec [i highest-index]
  (if (= i 0)
    highest-index
    (dec i)))

(defn wrap-inc [i highest-index]
  (if (= i highest-index)
    0
    (inc i)))

(defn floor-dec [i lowest]
  (if (= i lowest)
    lowest
    (dec i)))

(defn ceil-inc [i highest]
  (if (= i highest)
    highest
    (inc i)))

(defn highest-index [coll]
  (dec (count coll)))

(defn nth-byte [i file] (nth file i))

(defn as-hex [i]
  (let [s (format "%X" i)]
    (if (= (count s) 1)
      (str "0" s)
      s)))

(defn capped-size [size index file]
  (let [length (count file)
        max-size (- length index)]
    (if (< max-size size)
      max-size
      size)))

(defn byte-size [s] (/ (count s) 2))

(defn alphanumeric? [s]
  (boolean
   (re-matches #"[a-zA-Z0-9]+" s)))

(defn byte-code? [s]
   (and
    (even? (count s))
    (alphanumeric? s)))

(defn add-action [element f]
  (.addActionListener element
                      (proxy [ActionListener] []
                        (actionPerformed [e]
                          (f e)))))
