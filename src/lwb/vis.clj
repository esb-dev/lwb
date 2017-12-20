; lwb Logic WorkBench -- Visualisation of formulae

; Copyright (c) 2016 Burkhardt Renz, Juan Markowich THM. 
; All rights reserved.
; The use and distribution terms for this software are covered by the
; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php).
; By using this software in any fashion, you are agreeing to be bound by
; the terms of this license.

(ns lwb.vis
  (:require [clojure.zip :as zip]
            [clojure.string :as str]
            [clojure.java.shell :as shell]
            [clojure.java.browse :as browse]))

(defn man
  "Manual"
  []
  (browse/browse-url "https://github.com/esb-dev/lwb/wiki/vis"))

;; # Visualisation of formulas
;;
;; The syntax tree of a formula of propositional logic, predicate logic or
;; linear temporal logic is transformed into code for tikz and the package
;; `tikz-tree`.

;; With the help of `texi2pdf` a pdf file is generated.

;; A running TeX is a prerequisite.

(def ^:private tikz-header
  "\\documentclass{standalone}
   \\standaloneconfig{border=8pt}
   \\usepackage{MnSymbol}
   \\usepackage[english]{babel}
   \\usepackage{tikz-qtree}
   \\tikzset{every tree node/.style={shape=rectangle,minimum size=6mm,rounded corners=3mm,draw},
      edge from parent/.style={draw, edge from parent path={(\\tikzparentnode) -- (\\tikzchildnode)}},
      sibling distance=8pt
   }

   \\begin{document}
   \\begin{tikzpicture}
   \\Tree")

(def ^:private tikz-footer
  "\\end{tikzpicture}
   \\end{document}")

(defn- first?
  "Is loc the most left location of siblings?"
  [loc]
  (nil? (zip/left loc)))

(defn- end?
  "Is loc a node marked with `:end`?"
  [loc]
  (= :end (zip/node loc)))

(defn- mark-end-of-branch
  "To facilitate the generation of code in tikz, we mark the ends of
   lists with `:end`"
  [phi]
  (loop [loc (zip/seq-zip (seq phi))]
    (if (zip/end? loc)
      (zip/root loc)
      (recur (zip/next
               (if (zip/branch? loc)
                 (let [inserted-loc (zip/insert-right (-> loc zip/down zip/rightmost) :end)]
                   (zip/leftmost inserted-loc))
                 loc))))))

(defn- process-head
  "Generates texcode for the head of a list"
  [node]
  (let [symbols {:and     "\\land"
                 :or      "\\lor"
                 :not     "\\lnot"
                 :impl    "\\to"
                 :equiv   "\\leftrightarrow"
                 :true    "\\top"
                 :false   "\\bot"
                 :xor     "\\oplus"
                 :ite     "\\mathsf{ite}"
                 :always  "\\medsquare"
                 :finally "\\lozenge"
                 :atnext  "\\medcircle"
                 :until   "\\mathcal{U}"
                 :release "\\mathcal{R}"}
        nkey (keyword (name node))]
    (if (contains? symbols nkey)
      (str " [.\\node{$" (nkey symbols) "$};")
      (str " [.\\node{$" node "$};"))))

(defn- process-quantor
  "Generates texcode for quantors"
  [node vars]
  (let [quantors {:forall "\\forall"
                  :exists "\\exists"}
        nkey (keyword (name node))]
    (str " [.\\node{$" (nkey quantors) " "
         (str/join "\\, " vars) "$};")))

(defn- process-atom
  "Generates texcode for atoms        
   Since `{` and `}` are a reserved character in Clojure, 
   one can use `<` and `>`
   as characters for grouping subscripts e.g."
  [node]
  (let [node-str (str node)
        node-str' (str/replace node-str \< \{)
        node-str'' (str/replace node-str' \> \})]
    (str " $" node-str'' "$")))

(defn- mapfn
  "Mapping function that generates the tikz code from the traversing of the tree."
  [loc]
  (let [n (zip/node loc)]
    (cond
      (vector? n) ""                                        ; already processed
      (first? loc)                                          ; head with special case of quantor
      (if (or (= n 'forall) (= n 'exists))
        (let [n' (-> loc zip/next zip/node)]
          (process-quantor n n'))
        (process-head n))
      (end? loc) " ]"                                       ; last in list
      :else (process-atom n))))                             ; in the middle of the list

(defn- vis-tikz-body
  "Visualization with tikz, the body"
  [phi]
  (let [phi-n (if (symbol? phi) (list phi) phi)             ; special case
        phi' (mark-end-of-branch phi-n)
        loc (zip/seq-zip (seq phi'))]
    (str/join
      (map mapfn (remove zip/branch?
                         (take-while (complement zip/end?)
                                     (iterate zip/next loc)))))))

(defn texify
  "Visualisation of the syntax tree of formula `phi`.       
   Generates code for tikz.       
   With the filename given:      
   Makes a pdf file with the visualisation of the syntax tree of `phi`.        
   `filename` is the name of the file to be generated, must have no extension.       
   The function uses the shell command `texi2pdf` that compiles tex code,
   and `open` to show the generated file."
  ([phi]
   (let [tikz-body (vis-tikz-body phi)]
     (str tikz-header "\n" tikz-body "\n" tikz-footer)))
  ([phi filename]
   (let [tex-code (texify phi)
         sys-name (System/getProperty "os.name")]
     (spit (str filename ".tex") tex-code)
     (shell/sh "texi2pdf" (str filename ".tex"))
     (condp str/includes? sys-name
       "Linux" (shell/sh "xdg-open" (str filename ".pdf"))
       "Mac" (shell/sh "open" (str filename ".pdf"))
       "Windows" (shell/sh "start" (str filename ".pdf"))))))
