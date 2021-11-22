(ns byte-inspector.left-panel
  (:import (javax.swing JPanel)
           (java.awt FlowLayout)))

(defn make []
  (doto (JPanel.)
    (.setLayout (FlowLayout. FlowLayout/LEFT))))
