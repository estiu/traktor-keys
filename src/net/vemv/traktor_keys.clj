(ns net.vemv.traktor-keys
  "Converts Traktor notation to traditional notation, as used by Rekordbox and Ableton."
  (:require
   [claudio.id3 :as id3]
   [clojure.java.io]
   [nedap.speced.def :as speced])
  (:import
   (java.io File)
   (java.util.logging Level Logger)
   (org.jaudiotagger.audio AudioFileIO)
   (org.jaudiotagger.audio.mp3 MP3File)))

(def equivs
  {"1m"  "Am"
   "2m"  "Em"
   "3m"  "Bm"
   "4m"  "F#m"
   "5m"  "Dbm"
   "6m"  "Abm"
   "7m"  "Ebm"
   "8m"  "Bbm"
   "9m"  "Fm"
   "10m" "Cm"
   "11m" "Gm"
   "12m" "Dm"
   "1d"  "C"
   "2d"  "G"
   "3d"  "D"
   "4d"  "A"
   "5d"  "E"
   "6d"  "B"
   "7d"  "F#"
   "8d"  "Db"
   "9d"  "Ab"
   "10d" "Eb"
   "11d" "Bb"
   "12d" "F"})

(defn set-logging! []
  (-> "org.jaudiotagger"
      Logger/getLogger
      (.setLevel Level/OFF)))

(defn mp3-seq []
  (->> "/Users/vemv/Dropbox/Tracks"
       clojure.java.io/file
       file-seq
       (filter (speced/fn [^File file]
                 (-> file
                     .getName
                     (.endsWith "mp3"))))))

(defn convert! []

  (set-logging!)

  (doseq [^File mp3 (mp3-seq)
          :let [{k :key :as tag} (id3/read-tag mp3)]
          :when (->> equivs
                     keys
                     (some #{k}))]
    (id3/write-tag! mp3 :key (equivs k))))

(defn report-subpar-files! []

  (set-logging!)

  (->> (for [^File mp3 (mp3-seq)
             :let [tag (id3/read-tag mp3)
                   bitrate (-> mp3 ^MP3File (AudioFileIO/read) .getMP3AudioHeader .getBitRate)]
             :when (not= bitrate "320")]
         [bitrate tag])
       (sort-by first)
       (run! println)))
