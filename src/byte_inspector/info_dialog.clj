(ns byte-inspector.info-dialog
  (:require [byte-inspector.defaults :as defaults]
            [byte-inspector.panels :as panels]
            [byte-inspector.region :as r]
            [byte-inspector.state :as state])
  (:import (javax.swing BoxLayout
                        JDialog)
           (java.awt Rectangle)
           (java.awt.event ComponentAdapter
                           WindowAdapter)))


(def dbounds (atom {}))

(defn get-bounds [dialog] (get @dbounds dialog))

(defn save-dialog-bounds [dialog] (swap! dbounds #(assoc % dialog (.getBounds dialog))))

(defn remove-dialog-bounds [dialog] (swap! dbounds #(dissoc % dialog)))

(defn exit [context update-button-function]
  (let [{:keys [region dialog file]} context
        update-button update-button-function]
    (r/enforce-constraints context)
    (.setVisible dialog false)
    (.remove (.getOwner dialog) dialog)
    (remove-dialog-bounds dialog)
    (update-button context)
    (state/set-current-rep (r/rep region))
    (state/set-old-byte (dec (+ (r/index region) (r/size region))) file)))

(defn refresh [context add-button-function update-button-function]
  (let [dialog (:dialog context)
        cp (.getContentPane dialog)
        refresh-action (fn []
                         (refresh context
                                  add-button-function
                                  update-button-function))
        exit-function (fn []
                        (exit context update-button-function))]
    (.removeAll cp)
    (doto dialog
      (.add (panels/index-panel context refresh-action))
      (.add (panels/size-panel context refresh-action))
      (.add (panels/value-panel context))
      (.add (panels/rep-panel context refresh-action))
      (.add (panels/surroundings-panel context))
      (.add (panels/surroundings-size-panel context refresh-action))
      (.add (panels/search-panel context add-button-function))
      (.add (panels/exit-panel exit-function))
      (.setTitle (r/value context))
      (.setBounds (get-bounds dialog))
      (.repaint)
      (.revalidate))))

(defn make [frame context add-button-function update-button-function]
  (let [dialog (JDialog. frame (r/value context))
        context (assoc context :dialog dialog)
        cp (.getContentPane dialog)
        layout (BoxLayout. cp BoxLayout/Y_AXIS)
        menu (.getJMenuBar dialog)]
    (.addComponentListener dialog (proxy [ComponentAdapter] []
                                    (componentMoved [_]
                                      (save-dialog-bounds dialog))
                                    (componentResized [_]
                                      (save-dialog-bounds dialog))))
    (.setBounds dialog defaults/info-dialog-bounds)
    (save-dialog-bounds dialog)
    (.setLayout cp layout)
    (refresh context add-button-function update-button-function)
    (doto dialog
      (.setDefaultCloseOperation (JDialog/DO_NOTHING_ON_CLOSE))
      (.addWindowListener(proxy [WindowAdapter] []
                           (windowClosing [_] (exit context update-button-function))))
      (.setVisible true))))
