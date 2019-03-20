; lwb Logic WorkBench -- Natural deduction

; Copyright (c) 2016 Burkhardt Renz, THM. All rights reserved.
; The use and distribution terms for this software are covered by the
; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php).
; By using this software in any fashion, you are agreeing to be bound by
; the terms of this license.

(ns lwb.nd.error
  (:import (clojure.lang ExceptionInfo)))

;; # Custom exceptions for natural deduction in lwb

(defn ex-error
  "ExceptionInfo reporting an error."
  [msg]
  (ex-info msg {:type :error}))

(defn ex-warning
  "ExceptionInfo reporting a warning."
  [msg]
  (ex-info msg {:type :warning}))

(defn handle-exception
  "Prints message according to type of exception.      
   Requires: throwing `ex-info`s in lwb."
  [e]
  (if
    (instance? ExceptionInfo e)
    (let [type (if (= (-> e ex-data :type) :error) "Error: " "Warning: ")]
      (println (str "[lwb] " type (.getMessage e))))
    (println (str "Exception: " (.getMessage e)))))
