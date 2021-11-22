(ns byte-inspector.defaults
  (:import (java.awt Dimension
                     Rectangle)))

(def panel-size (Dimension. 80 20))

(def button-panel-hgap 10)
(def button-panel-vgap 5)

(def scroll-pane-size (Dimension. 1200 800))

(def frame-size (Dimension. 1500 900))

(def bytebutton-size (Dimension. 100 50))

(def info-dialog-bounds (Rectangle. 600 400 250 500))

(def index-field-size (Dimension. 30 20))
