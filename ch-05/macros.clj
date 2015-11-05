(ns macros
  (:require [clojure.string :as str]
            [clojure.walk :as walk]))

;;
;; Example 5.1
;;
(defmacro reverse-it
  [form]
  (walk/postwalk #(if (symbol? %)
                    (symbol (str/reverse (name %)))
                    %)
    form))

(comment
  (reverse-it (nltnirp "foo"))
  ;;=> foo
  
  (reverse-it
    (qesod [gra (egnar 5)]
      (nltnirp (cni gra))))
  ;; 1
  ;; 2
  ;; 3
  ;; 4
  ;; 5
  ;; nil
  )

