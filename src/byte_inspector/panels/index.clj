(ns byte-inspector.panels.index
  (:require [byte-inspector.defaults :as defaults]
            [byte-inspector.helpers :refer :all]
            [byte-inspector.left-panel :as left-panel]
            [byte-inspector.region :as r])
  (:import (javax.swing JButton
                        JLabel
                        JPanel
                        JTextField)
           (java.awt CardLayout
                     Dimension)
           (java.awt.event MouseAdapter)))

(defn text-field-action [tf context refresh-action]
   (fn [_]
     (let [region (:region context)
           text (.getText tf)
           refresh refresh-action]
       (try (let [i (Integer/parseInt text)]
              (r/set-index! region i)
              (refresh))
            (catch Exception e ())))))

(defn make-int-displayer [context index refresh-action]
  (let [panel (JPanel. (CardLayout.))
        c (.getLayout panel)
        tf (JTextField. (str index))
        tf-action (text-field-action tf context refresh-action)]
    (add-action tf tf-action)
    (doto panel
      (.add (JLabel. (str index)) "LABEL")
      (.add tf "TBOX")
      (.setPreferredSize defaults/index-field-size)
      (.addMouseListener (proxy [MouseAdapter] []
                           (mouseReleased [_]
                             (.show c panel "TBOX")))))))

(defn left-button [context refresh-action]
  (let [{:keys [region file]} context
        b (JButton. "<")
        index (fn [] (r/index region))
        wrap-dec-index (fn [] (wrap-dec (index) (highest-index file)))
        refresh refresh-action]
    (add-action b (fn [_]
                    (let [lowered (wrap-dec-index)]
                      (r/set-index! region lowered)
                      (refresh))))
    b))

(defn right-button [context refresh-action]
  (let [{:keys [region file]} context
        b (JButton. ">")
        index (fn [] (r/index region))
        wrap-inc-index (fn [] (wrap-inc (index) (highest-index file)))
        refresh refresh-action]
    (add-action b (fn [_]
                    (let [raised (wrap-inc-index)]
                      (r/set-index! region raised)
                      (refresh))))
    b))

(defn make [context refresh-action]
  (let [region (:region context)
        panel (left-panel/make)
        left-button (left-button context refresh-action)
        right-button (right-button context refresh-action)
        index (:index (r/entry region))
        int-displayer (make-int-displayer context index refresh-action)]
    (.add panel left-button)
    (.add panel (JLabel. "Index: "))
    (.add panel int-displayer)
    (.add panel right-button)
    (.setPreferredSize panel defaults/panel-size)
    panel))
