(ns byte-inspector.core
  (:gen-class)
  (:require [clojure.java.io :as io]
            [byte-inspector.debug :as debug]
            [byte-inspector.helpers :refer :all]
            [byte-inspector.ui :as ui]))

;; Surroundings can be larger than intuitive.

(defn remove-ends [string]
  (subs string 1 (dec (count string))))

(defn file->byte-array [file]
  (with-open [in (io/input-stream file)
              out (java.io.ByteArrayOutputStream.)]
    (io/copy in out)
    (.toByteArray out)))

(defn file->vec [file]
  (->> file
       file->byte-array
       (map #(as-hex %))
       vec))

(defn -main [& args]
  (let [debug? debug/debug?]
    (do
      (ui/start)
      (def filename (ui/get-file))
      (if (= filename :none)
        (if debug?
          (-main)
          (System/exit 0))
        (let [file (file->vec filename)]
          (def file file)
          (ui/run file))))))
