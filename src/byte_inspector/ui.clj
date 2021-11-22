(ns byte-inspector.ui
  (:require [byte-inspector.debug :as debug]
            [byte-inspector.defaults :as defaults]
            [byte-inspector.helpers :refer :all]
            [byte-inspector.info-dialog :as dialog]
            [byte-inspector.label :as label]
            [byte-inspector.panels :as panels]
            [byte-inspector.region :as r]
            [byte-inspector.rep :as rep]
            [byte-inspector.search :as s]
            [byte-inspector.state :as state])
  (:import (javax.swing BoxLayout
                        JButton
                        JComboBox
                        JDialog
                        JFileChooser
                        JFrame
                        JLabel
                        JOptionPane
                        JPanel
                        JScrollPane
                        JTextField)
           (java.awt CardLayout
                     Dimension
                     FlowLayout
                     Rectangle)
           (java.awt.event ActionListener
                           ComponentAdapter
                           MouseAdapter
                           WindowAdapter)))

(def frame (JFrame. "Byte Reader"))

(def button-panel (let [panel (JPanel.)
                        hg defaults/button-panel-hgap
                        vg defaults/button-panel-vgap]
                    (.setLayout panel (FlowLayout. FlowLayout/LEFT hg vg))
                    panel))

(def scroll-pane (JScrollPane. button-panel))

(def content-pane (let [panel (JPanel.)
                        x (.-width defaults/scroll-pane-size)
                        y (.-height defaults/scroll-pane-size)]
                   (.setLayout panel nil)
                   (.add panel scroll-pane)
                   (.setBounds scroll-pane 0 0 x y)
                   panel))

(defn exit-unless-debug [frame]
  (let [debug? debug/debug?]
    (if-not debug?
      (.setDefaultCloseOperation frame JFrame/EXIT_ON_CLOSE))))

(defn start []
  (let [debug? debug/debug?]
    (doto frame
      (.setContentPane content-pane)
      (exit-unless-debug)
      (.setMinimumSize defaults/frame-size)
      (.pack)
      (.setLocationRelativeTo nil)
      (.setVisible true))))

(declare make-button add-button)

(defn update-button [context]
  (let [region (:region context)
        button (r/button region)
        label (label/make context)]
    (.setText button label)))

(defn bytebutton [s] (let [button (JButton. s)]
                       (.setPreferredSize button defaults/bytebutton-size)
                       button))

(def plusbutton (bytebutton "+"))

(defn register-region [button index] 
  (r/add-region button index @state/current-rep))

(declare make-and-add-button)

(defn make-dialog [frame context]
  (dialog/make frame context make-and-add-button update-button))

(defn make-button [index file]
  (let [button (bytebutton "")
        _ (register-region button index)
        region (r/from-button button)
        context {:region region
                 :file file}
        label (label/make context)]
    (.setText button label)
    (add-action button (fn [_] (make-dialog frame context)))
    button))

(defn add-button [button]
  (doto button-panel
    (.remove plusbutton)
    (.add button)
    (.add plusbutton)
    (.revalidate)
    (.repaint)))

(defn make-and-add-button [index file]
  (-> (make-button index file)
      add-button))

(defn run [file]
  (do
    (add-action plusbutton (fn [_]
                           (make-and-add-button (state/next-byte file) file)))
    (.add button-panel plusbutton)
    (.repaint frame)
    (.revalidate frame)))

(def file-chooser (JFileChooser.))

(defn get-file []
  (let [val (.showOpenDialog file-chooser frame)]
    (if (= val JFileChooser/APPROVE_OPTION)
      (.getSelectedFile file-chooser)
      :none)))
