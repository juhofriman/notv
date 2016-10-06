(ns notv.xml-test
  (:require [clojure.test :refer :all]
            [notv.xml :refer :all]))

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
     <programme start=\"20160304210000 +0200\" stop=\"20160304220000 +0200\" channel=\"jim.telvis.fi\">
       <title lang=\"fi\">Grillit huurussa</title>
       <desc lang=\"fi\">First programme description</desc>
     </programme>
     <programme start=\"20160304220000 +0200\" stop=\"20160304230000 +0200\" channel=\"jim.telvis.fi\">
       <title lang=\"fi\">Firmat kuntoon</title>
       <desc lang=\"fi\">Second programme description</desc>
     </programme>
   </tv>")

(def without-stop-time-xml
  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
   <!DOCTYPE tv>
   <tv source-info-url=\"multiple\" source-data-url=\"multiple\" generator-info-name=\"XMLTV\" generator-info-url=\"http://xmltv.org/\">
     <channel id=\"channel-1\">
       <display-name lang=\"fi\">CHANNEL-1</display-name>
     </channel>
     <programme start=\"20160921200000 +0300\" channel=\"jim.telvis.fi\">
       <title lang=\"fi\">Grillit huurussa</title>
       <desc lang=\"fi\">First programme description</desc>
     </programme>
     <programme start=\"20160921203000 +0300\" channel=\"jim.telvis.fi\">
       <title lang=\"fi\">Firmat kuntoon</title>
       <desc lang=\"fi\">Second programme description</desc>
     </programme>
   </tv>")

(deftest xml-parsing-test
  (testing "Parsing channels from xml"
    (is (= (count (get-channels trivial-xml)) 0))
    (is (= (count (get-channels non-trivial-xml)) 2))
    (is (= (:id (first (get-channels non-trivial-xml))) "channel-1"))
    (is (= (:name (first (get-channels non-trivial-xml))) "CHANNEL-1"))
    (is (= (:id (second (get-channels non-trivial-xml))) "channel-2"))
    (is (= (:name (second (get-channels non-trivial-xml))) "CHANNEL-2"))))

    ; TODO: test programme mapping and times
