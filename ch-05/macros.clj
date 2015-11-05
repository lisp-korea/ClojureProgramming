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

