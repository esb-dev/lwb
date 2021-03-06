; lwb Logic WorkBench -- Linear Temporal Logic: Examples form the book of Richard Bornat:
; Proof and Disproof in Formal Logic

; Copyright (c) 2018 Burkhardt Renz, THM. All rights reserved.
; The use and distribution terms for this software are covered by the
; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php).
; By using this software in any fashion, you are agreeing to be bound by
; the terms of this license.

; The examples of Richard Bornat are formulas of the intuitionistic propositional logic.
; According to the so-called Gödel translation (see Chagrov, A., Zakharyaschev, M.: Modal Logic, p. 96)
; we transform the formulas into LTL and define the Kripke models accordingly.

(ns lwb.ltl.examples.bornat
  (:require [lwb.ltl :refer :all]                           ; needed for macroexpand-1 of xor etc !!
            [lwb.ltl.eval :refer :all]
            [lwb.ltl.kripke :as ks]                         ; needed for instrument
            [clojure.spec.test.alpha :as stest]
            [clojure.spec.alpha :as s]))

(stest/instrument `eval-phi)

;; We have to translate to formula of intuitionistic propositional logic into a formula
;; of linear temporal logical:
;; 1. each literal P or (not p) gets (always P) or (always (not P)) respectively
;; 2. each implication (impl P Q) gets (always (impl P q)

;; Furthermore we have to constructs Kripke structures, that have the following properties:
;; 1. Since the Kripke models for the intuitionistic logic are lattices
;;    i.e. they are reflexiv, anti-symmetric and transitiv and every two elements have a unique supremum
;;    and a unique infimum,
;;    the Kripke structure is basically the Hasse diagram, the transitive reduction of the
;;    lattice. Kripke models have just one unique infimum.
;;    To get a proper Kripke structure, we extend the Hasse diagram with a loop for each supremum. 
;; 2. The labeling function is monotonic, 
;;    i.e. if [s_1 s_2] is a transition, then the set at s_1 is a subset of the set at s_2.

;; Remark: The spec :ks/model does not comprises these additional constraints for a
;; Kripke structure for the intuitionistic logic.


;; Tertium non datur is not true in intuitionistic logic
(def k   {:atoms   '#{E}
          :nodes   {:s_1 '#{}
                    :s_2 '#{E}}
          :initial :s_1
          :edges   #{[:s_1 :s_2]
                     [:s_2 :s_2]}})

(s/conform ::ks/model k)

(comment
  (ks/texify k "bornat")
  )

(eval-phi '(always E) k) ; => false
(eval-phi '(always (not E)) k) ; => false
(eval-phi '(or (always E) (always (not E))) k) ; => false

; Examples p.142

(def f1 '(always E))
(def f2 '(and (always E) (always F)))
(def f3 '(or (always E) (always F)))
(def f4 '(always (impl E F)))
(def f5 '(always (impl E (always (impl F G)))))
(def f6 '(always (impl (always (impl E F)) G)))
(def f7 '(always (impl (always (impl E F)) (always (impl F G)))))

(def ka  {:atoms   '#{E F G}
          :nodes   {:s_1 '#{}}
          :initial :s_1
          :edges   #{[:s_1 :s_1]}})

(s/conform ::ks/model ka)

(comment
  (ks/texify ka "bornat")
  )

(eval-phi f1 ka) ; => false
(eval-phi f2 ka) ; => false
(eval-phi f3 ka) ; => false
(eval-phi f4 ka) ; => true
(eval-phi f5 ka) ; => true
(eval-phi f6 ka) ; => false
(eval-phi f7 ka) ; => true

(def kb  {:atoms   '#{E F G}
          :nodes   {:s_1 '#{E}}
          :initial :s_1
          :edges   #{[:s_1 :s_1]}})

(s/conform ::ks/model kb)

(comment
  (ks/texify kb "bornat")
  )

(eval-phi f1 kb) ; => true
(eval-phi f2 kb) ; => false
(eval-phi f3 kb) ; => true
(eval-phi f4 kb) ; => false
(eval-phi f5 kb) ; => true
(eval-phi f6 kb) ; => true
(eval-phi f7 kb) ; => true

(def kc  {:atoms   '#{E F G}
          :nodes   {:s_1 '#{F}}
          :initial :s_1
          :edges   #{[:s_1 :s_1]}})

(s/conform ::ks/model kc)

(comment
  (ks/texify kc "bornat")
  )

(eval-phi f1 kc) ; => false
(eval-phi f2 kc) ; => false
(eval-phi f3 kc) ; => true
(eval-phi f4 kc) ; => true
(eval-phi f5 kc) ; => true
(eval-phi f6 kc) ; => false
(eval-phi f7 kc) ; => false

(def kd  {:atoms   '#{E F G}
          :nodes   {:s_1 '#{E F G}}
          :initial :s_1
          :edges   #{[:s_1 :s_1]}})

(s/conform ::ks/model kd)

(comment
  (ks/texify kd "bornat")
  )

(eval-phi f1 kd) ; => true
(eval-phi f2 kd) ; => true
(eval-phi f3 kd) ; => true
(eval-phi f4 kd) ; => true
(eval-phi f5 kd) ; => true
(eval-phi f6 kd) ; => true
(eval-phi f7 kd) ; => true

(def ke  {:atoms   '#{E F G}
          :nodes   {:s_1 '#{}
                    :s_2 '#{E}}
          :initial :s_1
          :edges   #{[:s_1 :s_2]
                     [:s_2 :s_2]}})

(s/conform ::ks/model ke)

(comment
  (ks/texify ke "bornat")
  )

(eval-phi f1 ke) ; => false
(eval-phi f2 ke) ; => false
(eval-phi f3 ke) ; => false
(eval-phi f4 ke) ; => false
(eval-phi f5 ke) ; => true
(eval-phi f6 ke) ; => true
(eval-phi f7 ke) ; => true

(def kf  {:atoms   '#{E F G}
          :nodes   {:s_1 '#{}
                    :s_2 '#{E}
                    :s_3 '#{F}
                    :s_4 '#{E F}}
          :initial :s_1
          :edges   #{[:s_1 :s_2]
                     [:s_1 :s_3]
                     [:s_2 :s_4]
                     [:s_3 :s_4]
                     [:s_4 :s_4]}})

(s/conform ::ks/model kf)

(comment
  (ks/texify kf "bornat")
  )

(eval-phi f1 kf) ; => false
(eval-phi f2 kf) ; => false
(eval-phi f3 kf) ; => false
(eval-phi f4 kf) ; => false
(eval-phi f5 kf) ; => false
(eval-phi f6 kf) ; => false, although f4 is false
(eval-phi f7 kf) ; => false

(def kg  {:atoms   '#{E F G}
          :nodes   {:s_1 '#{}
                    :s_2 '#{F}
                    :s_3 '#{E F}}
          :initial :s_1
          :edges   #{[:s_1 :s_2]
                     [:s_2 :s_3]
                     [:s_3 :s_3]}})

(s/conform ::ks/model kg)

(comment
  (ks/texify kg "bornat")
  )

(eval-phi f1 kg) ; => false
(eval-phi f2 kg) ; => false
(eval-phi f3 kg) ; => false
(eval-phi f4 kg) ; => true
(eval-phi f5 kg) ; => false
(eval-phi f6 kg) ; => false
(eval-phi f7 kg) ; => false

