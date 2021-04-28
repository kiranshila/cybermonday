(ns cybermonday.utils)

(defn make-hiccup-node
  "Creates a hiccup node, consuming a sequence of children"
  ([tag children]
   (apply vector tag {} (filter identity children)))
  ([tag attrs children]
   (apply vector tag attrs (filter identity children))))

(defn hiccup?
  "Tests to see if an element is valid hiccup"
  [element]
  (and (vector? element)
       (keyword? (first element))))
