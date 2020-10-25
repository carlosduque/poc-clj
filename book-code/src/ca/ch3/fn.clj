(ns ca.ch3.fn)

(defn nth-page
  "Return up to page-size results for the nth (0-based) page
  of source."
  [source page-size page]
  (->> source
       (drop (* page page-size))
       (take page-size)))

(defn page-and-rest
  [source page-size]
  (split-at page-size source))

(take 5 (sort (map (:name planets))))
(take 5 (sort-by :name planets))

(defn smallest-n
  [planets n]
  (->> planets
       (sort-by :volume)
       (take n)))

(defn index-planets
  [planets]
  (group-by #(first (:name %)) planets))

(defn has-moons?
  [planet]
  (pos? (:moons planet)))

(defn split-moons
  [planets]
  (group-by has-moons? planets))

