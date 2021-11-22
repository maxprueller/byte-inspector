(ns byte-inspector.state
  (:require [byte-inspector.helpers :refer :all]
            [byte-inspector.rep :as rep]))

(def current-byte (atom -1))

(def current-rep (atom rep/hex))

(defn next-byte [file]
  (let [max (highest-index file)]
    (swap! current-byte ceil-inc max)))

(defn set-old-byte [num file]
  (let [maximum (highest-index file)]
    (swap! current-byte (fn [b]
                          (min num maximum)))))

(defn set-current-rep [f] (swap! current-rep (fn [old-f] f)))
