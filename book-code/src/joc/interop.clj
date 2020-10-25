(ns joc.core)

;;interop
(defn xors [max-x max-y]
  (for [x (range max-x) y (range max-y)]
    [x y (bit-xor x y)]))

(xors 2 2)

(def frame (java.awt.Frame.))
(.setVisible frame true)
(.setSize frame (java.awt.Dimension. 200 200))
(def gfx (.getGraphics frame))
;;(.fillRect gfx 100 100 50 75)
;;(.setColor gfx (java.awt.Color. 255 128 0))
(doseq [[x y xor] (xors 200 200)]
  (.setColor gfx (java.awt.Color. xor xor xor))
  (.fillRect gfx x y 1 1))

(def a 1.0e50)
(def b -1.0e50)
(def c 17.0e00)

(+ (+ a b) c)
(+ a (+ b c))

(def aa (rationalize 1.0e50))
(def bb (rationalize -1.0e50))
(def cc (rationalize 17.0e00))

(+ (+ aa bb) cc)
(+ aa (+ bb cc))

