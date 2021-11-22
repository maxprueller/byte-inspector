(ns byte-inspector.region
  (:require [byte-inspector.helpers :refer :all]
            [byte-inspector.rep :as rep]))

(def max-id (atom -1))

(def regions (atom []))

(def min-size 1)

(defn entry [region] (get @regions region))

(defn change! [region kw v]
  (let [old-entry (entry region)
        new-entry (assoc old-entry kw v)]
    (swap! regions assoc region new-entry)))

(defn add-region [button index rep]
  (swap! regions
         #(conj % {:button button
                   :index index
                   :id (swap! max-id inc)
                   :representation rep
                   :size 1
                   :surroundings 1})))

(defn size [region]
  (:size (entry region)))
(defn index [region]
  (:index (entry region)))
(defn rep [region]
  (:representation (entry region)))
(defn surroundings [region]
  (:surroundings (entry region)))
(defn button [region]
  (:button (entry region)))

(defn from-button [btn]
  (let [keys (take (count @regions) (range))
        found? #(= (button %) btn)]
    (first (filter found? keys))))

(defn set-size! [region size file]
  (let [max-size (- (count file) (index region))
        min-size 1
        size (min size max-size)
        size (max size min-size)]
    (change! region :size size)))

(defn set-index! [region i] (change! region :index i))
(defn set-rep! [region f] (change! region :representation f))
(defn set-surroundings! [region i] (change! region :surroundings i))

(defn enforce-constraints [context]
  (let [{:keys [region file]} context
        size (size region)
        size (max size min-size)]
    (set-size! region size file)))

(defn value [context]
  (let [{:keys [region file]} context
        index (index region)
        offset index
        size (size region)
        size (capped-size size index file)
        indices (map #(+ % offset)
                     (take size (range)))
        bytes (map #(nth-byte % file)
                indices)]
    (apply str bytes)))

(defn display-value [context]
  (let [region (:region context)
        rep (rep region)]
    (rep (value context))))

(defn display-indices
  "To be used within an html string"
  ([region]
   (let [size (size region)
         index (index region)]
     (display-indices index size)))
  ([index size]
   (let [last-index (+ index (dec size))]
     (if (= size 1)
       (str "&lt;" index ">")
       (str "&lt;" index " - " last-index ">")))))
