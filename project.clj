(defproject byte-inspector "1.0"
  :description "A quick and dirty inspector to view file bytes in different representations"
  :url "http://example.com/FIXME"
  :license {:name "MIT"
	    :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :main ^:skip-aot byte-inspector.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
