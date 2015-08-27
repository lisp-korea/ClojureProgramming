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


;(let [[t d] (split-with #(< % 12) (range 1e8))]
;  [(count t) (count d)])
;= [12 99999988]

(def m {:a 1, :b 2, :c 3})
;= #'user/m
(get m :b)
;= 2
(get m :d)
;= nil
(get m :d "not-found")
;= "not-found"
(assoc m :d 4)
;= {:a 1, :b 2, :c 3, :d 4}
(dissoc m :b)
;= {:a 1, :c 3}

(assoc m
  :x 4
  :y 5
  :z 6)
;= {:z 6, :y 5, :x 4, :a 1, :c 3, :b 2}
(dissoc m :a :c)
;= {:b 2}

(def v [1 2 3])
;= #'user/v
(get v 1)
;= 2
(get v 10)
;= nil
(get v 10 "not-found")
;= "not-found"
(assoc v
  1 4
  0 -12
  2 :p)
;= [-12 4 :p]

(assoc v 3 10)
;= [1 2 3 10]

(get #{1 2 3} 2)
;= 2
(get #{1 2 3} 4)
;= nil
(get #{1 2 3} 4 "not-found")
;= "not-found"

(when (get #{1 2 3} 2)
  (println "it contains `2`!"))
; it contains `2`!

(contains? [1 2 3] 0)
;= true
(contains? {:a 5 :b 6} :b)
;= true
(contains? {:a 5 :b 6} 42)
;= false
(contains? #{1 2 3} 1)
;= true

 (contains? [1 2 3] 3)
;= false
(contains? [1 2 3] 2)
;= true
(contains? [1 2 3] 0)
;= true

(get "Clojure" 3)
;= \j
(contains? (java.util.HashMap.) "not-there")
;= false
(get (into-array [1 2 3]) 0)
;= 1

(get {:ethel nil} :lucy)
;= nil
(get {:ethel nil} :ethel)
;= nil

(find {:ethel nil} :lucy)
;= nil
(find {:ethel nil} :ethel)
;= [:ethel nil]

(if-let [e (find {:a 5 :b 6} :a)]
  (format "found %s => %s" (key e) (val e))
  "not found")
;= "found :a => 5"
(if-let [[k v] (find {:a 5 :b 6} :a)]
  (format "found %s => %s" k v)
  "not found")
;= "found :a => 5"

(nth [:a :b :c] 2)
;= :c
(get [:a :b :c] 2)
;= :c
;(nth [:a :b :c] 3)
;= java.lang.IndexOutOfBoundsException
(get [:a :b :c] 3)

;= nil
;(nth [:a :b :c] -1)
;= java.lang.IndexOutOfBoundsException
(get [:a :b :c] -1)
;= nil

(nth [:a :b :c] -1 :not-found)
;= :not-found
(get [:a :b :c] -1 :not-found)
;= :not-found

(get 42 0)
;= nil
;(nth 42 0)
;= java.lang.UnsupportedOperationException: nth not supported on this type: Long


(conj '() 1)
;= (1)
(conj '(2 1) 3)
;= (3 2 1)
(peek '(3 2 1))
;= 3
(pop '(3 2 1))
;= (2 1)
(pop '(1))
;= ()

(conj [] 1)
;= [1]
(conj [1 2] 3)
;= [1 2 3]
(peek [1 2 3])
;= 3
(pop [1 2 3])
;= [1 2]
(pop [1])
;= []

(get #{1 2 3} 2)
;= 2
(get #{1 2 3} 4)
;= nil
(get #{1 2 3} 4 "not-found")
;= "not-found"

(disj #{1 2 3} 3 1)
;= #{2}

(def sm (sorted-map :z 5 :x 9 :y 0 :b 2 :a 3 :c 4))
;= #'user/sm
sm
;= {:a 3, :b 2, :c 4, :x 9, :y 0, :z 5}
(rseq sm)
;= ([:z 5] [:y 0] [:x 9] [:c 4] [:b 2] [:a 3])
(subseq sm <= :c)
;= ([:a 3] [:b 2] [:c 4])
(subseq sm > :b <= :y)
;= ([:c 4] [:x 9] [:y 0])
(rsubseq sm > :b <= :y)
;= ([:y 0] [:x 9] [:c 4])

(compare 2 2)
;= 0
(compare "ab" "abc")
;= -1
(compare ["a" "b" "c"] ["a" "b"])
;= 1
(compare ["a" 2] ["a" 2 0])
;= -1

(sort < (repeatedly 10 #(rand-int 100)))
;= (12 16 22 23 41 42 61 63 83 87)
(sort-by first > (map-indexed vector "Clojure"))
;= ([6 \e] [5 \r] [4 \u] [3 \j] [2 \o] [1 \l] [0 \C])

(sorted-map-by compare :z 5 :x 9 :y 0 :b 2 :a 3 :c 4)
;= {:a 3, :b 2, :c 4, :x 9, :y 0, :z 5}
(sorted-map-by (comp - compare) :z 5 :x 9 :y 0 :b 2 :a 3 :c 4)
;= {:z 5, :y 0, :x 9, :c 4, :b 2, :a 3}

(defn magnitude
  [x]
  (-> x Math/log10 Math/floor))
;= #'user/magnitude
(magnitude 100)
;= 2.0
(magnitude 100000)
;= 5.0

(defn compare-magnitude
  [a b]
  (- (magnitude a) (magnitude b)))
((comparator compare-magnitude) 10 10000)
;= -1
((comparator compare-magnitude) 100 10)
;= 1
((comparator compare-magnitude) 10 75)
;= 0

(sorted-set-by compare-magnitude 10 1000 500)
;= #{10 500 1000}
(conj *1 600)      
;= #{10 500 1000}
(disj *1 750)      
;= #{10 1000}
(contains? *1 1239)
;= true
