(ns clojure.examples.hello
	(:gen-class))

'(a b :name 12.5) ;; list
['a 'b :name 12.5] ;; vector
{:name "Chas" :age 31} ;; map
#{1 2 3} ;; set
{Math/PI "~3.14"
 [:composite "key"] 42
 nil "nothing"} ;; another map
#{{:first-name "chas" :last-name "emerick"}
 {:first-name "brian" :last-name "carper"}
 {:first-name "christophe" :last-name "grand"}} ;; a set of maps
 
 (def v [1 2 3])
;= #'user/v
(conj v 4)
;= [1 2 3 4]
(conj v 4 5)
;= [1 2 3 4 5]
(seq v)
;= (1 2 3)

(def m {:a 5 :b 6})
;= #'user/m
(conj m [:c 7])
;= {:a 5, :c 7, :b 6}
(seq m)
;= ([:a 5] [:b 6])

(def s #{1 2 3})
;= #'user/s
(conj s 10)
;= #{1 2 3 10}
(conj s 3 4)
;= #{1 2 3 4}
(seq s)
;= (1 2 3)

(def lst '(1 2 3))
;= #'user/lst
(conj lst 0)
;= (0 1 2 3)
(conj lst 0 -1)
;= (-1 0 1 2 3)
(seq lst)
;= (1 2 3)

(into v [4 5])
;= [1 2 3 4 5]
(into m [[:c 7] [:d 8]])
;= {:a 5, :c 7, :b 6, :d 8}
(into #{1 2} [2 3 4 5 3 3 2])
;= #{1 2 3 4 5}
(into [1] {:a 1 :b 2})
;= [1 [:a 1] [:b 2]]

(conj '(1 2 3) 4)
;= (4 1 2 3)
(into '(1 2 3) [:a :b :c])
;= (:c :b :a 1 2 3)

(defn swap-pairs
 [sequential]
 (into (empty sequential)
 (interleave
 (take-nth 2 (drop 1 sequential))
 (take-nth 2 sequential))))
(swap-pairs (apply list (range 10)))
;= (8 9 6 7 4 5 2 3 0 1)
(swap-pairs (apply vector (range 10)))
;= [1 0 3 2 5 4 7 6 9 8]

(defn map-map
 [f m]
 (into (empty m)
 (for [[k v] m]
 [k (f v)])))
 
 (map-map inc (hash-map :z 5 :c 6 :a 0))
;= {:z 6, :a 1, :c 7}
(map-map inc (sorted-map :z 5 :c 6 :a 0))
;= {:a 1, :c 7, :z 6}

(count [1 2 3])
;= 3
(count {:a 1 :b 2 :c 3})
;= 3
(count #{1 2 3})
;= 3
(count '(1 2 3))
;= 3

(seq "Clojure")
;= (\C \l \o \j \u \r \e)
(seq {:a 5 :b 6})
;= ([:a 5] [:b 6])
(seq (java.util.ArrayList. (range 5)))
;= (0 1 2 3 4)
(seq (into-array ["Clojure" "Programming"]))
;= ("Clojure" "Programming")
(seq [])
;= nil
(seq nil)
;= nil

(map str "Clojure")
;= ("C" "l" "o" "j" "u" "r" "e")
(set "Programming")
;= #{\a \g \i \m \n \o \P \r}

(first "Clojure")
;= \C
(rest "Clojure")
;= (\l \o \j \u \r \e)
(next "Clojure")
;= (\l \o \j \u \r \e)

(rest [1])
;= ()
(next [1])
;= nil
(rest nil)
;= ()
(next nil)
;= nil

;; Unable to resolve symbol : x
;(= (next x)
; (seq (rest x)))

(doseq [x (range 3)]
 (println x))
; 0
; 1
; 2

(let [r (range 3)
 rst (rest r)]
 (prn (map str rst))
 (prn (map #(+ 100 %) r))
 (prn (conj r -1) (conj rst 42)))
; ("1" "2")
; (100 101 102)
; (-1 0 1 2) (42 1 2)

;; time over
;(let [s (range 1e6)]
; (time (count s)))
; "Elapsed time: 147.661 msecs"
;= 1000000
;(let [s (apply list (range 1e6))]
; (time (count s)))
; "Elapsed time: 0.03 msecs"
;= 1000000

(cons 0 (range 1 5))
;= (0 1 2 3 4)

(cons :a [:b :c :d])
;= (:a :b :c :d)

(cons 0 (cons 1 (cons 2 (cons 3 (range 4 10)))))
;= (0 1 2 3 4 5 6 7 8 9)
(list* 0 1 2 3 (range 4 10))
;= (0 1 2 3 4 5 6 7 8 9)

(lazy-seq [1 2 3])
;= (1 2 3)

(defn random-ints
 "Returns a lazy seq of random integers in the range [0,limit)."
 [limit]

 (lazy-seq
 (cons (rand-int limit)
 (random-ints limit))))
(take 10 (random-ints 50))
;= (32 37 8 2 22 41 19 27 34 27)

(defn random-ints
 [limit]
 (lazy-seq
 (println "realizing random number")
 (cons (rand-int limit)
 (random-ints limit))))
(def rands (take 10 (random-ints 50)))
;= #'user/rands
(first rands)
; realizing random number
;= 39
(nth rands 3)
; realizing random number
; realizing random number
; realizing random number
;= 44
(count rands)
; realizing random number
; realizing random number
; realizing random number
; realizing random number
; realizing random number
; realizing random number
;= 10
(count rands)
;= 10

(repeatedly 10 (partial rand-int 50))
;= (47 19 26 14 18 37 44 13 41 38)

(def x (next (random-ints 50)))
; realizing random number
; realizing random number

(def x (rest (random-ints 50)))
; realizing random number

(let [[x & rest] (random-ints 50)])
; realizing random number
; realizing random number
;= nil

(dorun (take 5 (random-ints 50)))
; realizing random number
; realizing random number
; realizing random number
; realizing random number
; realizing random number
;= nil


(apply str (remove (set "aeiouy")
             "vowels are useless! or maybe not..."))
;= "vwls r slss! r mb nt..."

(split-with neg? (range -5 5))
;= [(-5 -4 -3 -2 -1) (0 1 2 3 4)]

;(let [[t d] (split-with #(< % 12) (range 1e8))]
;  [(count d) (count t)])
;= #<OutOfMemoryError java.lang.OutOfMemoryError: Java heap space>

(let [[t d] (split-with #(< % 12) (range 1e8))]
  [(count t) (count d)])
;= [12 99999988]

