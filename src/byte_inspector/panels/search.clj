(ns byte-inspector.panels.search
  (:require [byte-inspector.helpers :refer :all]
            [byte-inspector.label :as label]
            [byte-inspector.left-panel :as left-panel]
            [byte-inspector.region :as r]
            [byte-inspector.search :as s])
  (:import (javax.swing JButton
                        JLabel
                        JTextField))
 (:refer-clojure :exclude [update]))

(def last-results (atom {}))

(defn store-result [s index region]
  (swap! last-results assoc region {:str s
                              :ind index}))

(defn last-result [region]
  (get @last-results region))

(defn update [s search-f label textfield context]
  (let [{:keys [region file]} context
        byte-amount (byte-size s)]
    (if (byte-code? s)
      (if-let [result (search-f s file (last-result region))]
        (do
          (store-result (:str result) (:ind result) region)
          (.setText label (label/make (:ind result) byte-amount s))
          (.setText textfield s))))))

(defn make [context add-button-function]
  (let [{:keys [region file]} context
        panel (left-panel/make)
        -button (JButton. "<")
        +button (JButton. ">")
        maker-button (JButton. "+")
        tf (JTextField. 8)
        label (JLabel. "")
        this-index (r/index region)
        this-str (r/value context)
        search-string (fn []
                        (let [text (.getText tf)]
                          (if (clojure.string/blank? text)
                            this-str)))
        search-action (fn [text search-f]
                          (fn [_]
                            (update text search-f label tf context)))
        search-text (search-action (search-string) s/next-index)
        search-on (search-action (search-string) s/next-index)
        search-back (search-action (search-string) s/prev-index)
        add-button add-button-function]
    (store-result this-str this-index region)
    (.add panel tf)
    (add-action tf search-text)
    (.add panel label)
    (.add panel -button)
    (add-action -button search-back)
    (.add panel +button)
    (add-action +button search-on)
    (.add panel maker-button)
    (add-action maker-button
                (fn [_]
                  (add-button (:ind (last-result region))
                              file)))
    panel))
