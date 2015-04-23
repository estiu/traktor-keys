(require 'clojure.java.io)
(require '[claudio.id3 :as id3])

(.setLevel (java.util.logging.Logger/getLogger "org.jaudiotagger") java.util.logging.Level/OFF)

(def tracks (clojure.java.io/file "/Users/vemv/Dropbox/Tracks"))

(def equivs {
  "1m" "A min"
  "2m" "E min "
  "3m" "B min"
  "4m" "Gb min"
  "5m" "Db min"
  "6m" "Ab min"
  "7m" "Eb min"
  "8m" "Bb min"
  "9m" "F min"
  "10m" "C min"
  "11m" "G min"
  "12m" "D min"
  "1d" "C maj"
  "2d" "G maj"
  "3d" "D maj"
  "4d" "A maj"
  "5d" "E maj"
  "6d" "B maj"
  "7d" "Gb maj"
  "8d" "Db maj"
  "9d" "Ab maj"
  "10d" "Eb maj"
  "11d" "Bb maj"
  "12d" "F maj"
})
  
(doseq [mp3 (filter #(.endsWith (.getName %) "mp3") (file-seq tracks))]
  (let [k (:key (id3/read-tag mp3))]
    (when (some (set [k]) (keys equivs))
      (id3/write-tag! mp3 :key (equivs k)))))
