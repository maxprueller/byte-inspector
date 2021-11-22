(ns byte-inspector.panels.surroundings-size
  (:require [byte-inspector.left-panel :as left-panel]
            [byte-inspector.helpers :refer :all]
            [byte-inspector.region :as r])
  (:import (javax.swing JButton)))

(defn -action [context refresh-function]
  (fn [_]
    (let [region (:region context)
          surroundings (r/surroundings region)
          surroundings (floor-dec surroundings 0)
          refresh refresh-function]
      (r/set-surroundings! region surroundings)
      (refresh))))

(defn +action [context refresh-function]
  (fn [_]
    (let [{:keys [region file]} context
          surroundings (r/surroundings region)
          surroundings (ceil-inc surroundings (count file))
          refresh refresh-function]
      (r/set-surroundings! region surroundings)
      (refresh))))

(defn make [context refresh-function]
  (let [{:keys [region file]} context
        panel (left-panel/make)
        surroundings (r/surroundings region)
        -button (JButton. "-")
        +button (JButton. "+")
        -action (-action context refresh-function)
        +action (+action context refresh-function)]
    (add-action -button -action)
    (add-action +button +action)
    (doto panel
      (.add -button)
      (.add +button))))
