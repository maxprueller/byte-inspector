(ns byte-inspector.panels.size
  (:require [byte-inspector.defaults :as defaults]
            [byte-inspector.helpers :refer :all]
            [byte-inspector.left-panel :as left-panel]
            [byte-inspector.region :as r])
  (:import (javax.swing JButton
                        JLabel)))

(defn +button [context refresh-action]
  (let [{:keys [region file]} context
        b (JButton. "+")
        size (fn [] (r/size region))
        +size (fn [] (ceil-inc (size) (count file)))
        refresh refresh-action]
    (add-action b (fn [_]
                    (do
                      (r/set-size! region (+size) file)
                      (refresh))))
    b))

(defn -button [context refresh-action]
  (let [{:keys [region file]} context
        b (JButton. "-")
        size (fn [] (r/size region))
        -size (fn [] (floor-dec (size) 0))
        refresh refresh-action]
    (add-action b (fn [_]
                    (do
                      (r/set-size! region (-size) file)
                      (refresh))))
    b))

(defn make [context refresh-action]
  (let [region (:region context)
        panel (left-panel/make)
        +button (+button context refresh-action)
        -button (-button context refresh-action)
        size (fn [] (r/size region))]
    (.add panel (JLabel. "Size: "))
    (.add panel (JLabel. (str (r/size region))))
    (.add panel -button)
    (.add panel +button)
    (.setPreferredSize panel defaults/panel-size)
    panel))
