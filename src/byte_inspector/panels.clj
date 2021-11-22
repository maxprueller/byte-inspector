(ns byte-inspector.panels
  (:require [byte-inspector.defaults :as defaults]
            [byte-inspector.helpers :refer :all]
            [byte-inspector.left-panel :as left-panel]
            [byte-inspector.region :as r]
            [byte-inspector.rep :as rep]
            [byte-inspector.panels.index :as index-panel]
            [byte-inspector.panels.search :as search-panel]
            [byte-inspector.panels.size :as size-panel]
            [byte-inspector.panels.surroundings :as surroundings-panel]
            [byte-inspector.panels.surroundings-size :as surroundings-size-panel])
  (:import (javax.swing JButton
                        JComboBox
                        JLabel
                        JPanel
                        JTextField)
           (java.awt CardLayout
                     Dimension
                     FlowLayout)
           (java.awt.event MouseAdapter)))

(defn index-panel [context refresh-action]
  (index-panel/make context refresh-action))

(defn size-panel [context refresh-action]
  (size-panel/make context refresh-action))

(defn value-panel [context]
  (let [panel (left-panel/make)]
    (.add panel (JLabel. "Value: "))
    (.add panel (JLabel. (str (r/display-value context))))
    (.setPreferredSize panel defaults/panel-size)
    panel))

(defn rep-panel [context refresh-action]
  (let [region (:region context)
        panel (left-panel/make)
        box (JComboBox. rep/strings)
        refresh refresh-action]
    (.add panel box)
    (.setSelectedItem box (get rep/repmap (r/rep region)))
    (add-action box (fn [e]
                      (let [s (.getSelectedItem box)]
                        (r/set-rep! region (rep/from-string s))
                        (refresh))))
    (.setPreferredSize panel defaults/panel-size)
    panel))

(defn surroundings-panel [context]
  (surroundings-panel/make context))

(defn surroundings-size-panel [context refresh-function]
  (surroundings-size-panel/make context refresh-function))

(defn search-panel [context add-button-function]
  (search-panel/make context add-button-function))

(defn exit-panel [exit-function]
  (let [panel (JPanel.)
        button (JButton. "Exit")
        exit exit-function]
    (.setLayout panel (FlowLayout. FlowLayout/RIGHT))
    (.add panel button)
    (add-action button (fn [_] (exit)))
    (.setPreferredSize panel defaults/panel-size)
    panel))
