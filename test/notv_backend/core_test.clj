(ns notv-backend.core-test
  (:require [clojure.test :refer :all]
            [notv-backend.core :refer :all]))

(def trivial-xml
  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
   <!DOCTYPE tv>
   <tv source-info-url=\"multiple\" source-data-url=\"multiple\" generator-info-name=\"XMLTV\" generator-info-url=\"http://xmltv.org/\">
   </tv>")

(def non-trivial-xml
  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
   <!DOCTYPE tv>
   <tv source-info-url=\"multiple\" source-data-url=\"multiple\" generator-info-name=\"XMLTV\" generator-info-url=\"http://xmltv.org/\">
     <channel id=\"channel-1\">
       <display-name lang=\"fi\">CHANNEL-1</display-name>
     </channel>
     <channel id=\"channel-2\">
       <display-name lang=\"fi\">CHANNEL-2</display-name>
     </channel>
   </tv>")

(deftest xml-parsing-test
  (testing "Parsing channels from xml"
    (is (= (count (get-channels trivial-xml)) 0))
    (is (= (count (get-channels non-trivial-xml)) 2))
    (is (= (:id (first (get-channels non-trivial-xml))) "channel-1"))
    (is (= (:name (first (get-channels non-trivial-xml))) "CHANNEL-1"))
    (is (= (:id (second (get-channels non-trivial-xml))) "channel-2"))
    (is (= (:name (second (get-channels non-trivial-xml))) "CHANNEL-2"))))
