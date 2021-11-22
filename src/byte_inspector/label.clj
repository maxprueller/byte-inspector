(ns byte-inspector.label
  (:require [byte-inspector.region :as r]))

(defn make
  ([context]
   (let [region (:region context)
         index (r/index region)
         size (r/size region)
         s (r/display-value context)]
     (make index size s)))
  ([index size s]
   (let [index-string (r/display-indices index size)
         value-string s]
     (str "<html>" index-string "<br>"
          value-string "</html>"))))
