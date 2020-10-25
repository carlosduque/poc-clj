(ns ca.ch1.recipe)

(defrecord Recipe
  [name         ;; string
   author       ;; recipe creator
   description  ;; string
   ingredients  ;; list of ingredients
   steps        ;; sequence of strings
   servings     ;; number of servings
   ])

(defrecord Person
  [fname    ;; first name
   lname    ;; last name
   ])

(def toast
  (->Recipe
            "Toast"
            (->Person "Alex" "Miller") ;; nested
            "Crispy bread"
            ["Slice of bread"]
            ["Toast bread in toaster"]
            1))

(def people
  {"p1" (->Person "Alex" "Miller")})

(def recipes
  {"r1" (->Recipe
                  "Toast"
                  "p1"   ;; Person id
                  "Crispy bread"
                  ["Slice of bread"]
                  ["Toast bread in toaster"]
                  1)})

(defrecord Recipe
  [name          ;; string
   description   ;; string
   ingredients   ;; sequence of Ingredients
   steps         ;; sequence of string
   servings      ;; number of servings
   ])

(defrecord Ingredient
  [name          ;; string
   quantity      ;; amount
   unit          ;; keyword
   ])

(def spaghetti-tacos
  (map->Recipe
               {:name "Spaghetti tacos"
                :description "It's spaghetti... in a taco."
                :ingredients [(->Ingredient "Spaghetti" 1 :lb)
                              (->Ingredient "Spaghetti sauce" 16 :oz)
                              (->Ingredient "Taco shell" 12 :shell)]
                :steps ["Cook spaghetti according to box."
                        "Heat spaghetti sauce until warm."
                        "Mix spaghetti and sauce."
                        "Put spaghetti in taco shells and serve."]
                :servings 4}))

