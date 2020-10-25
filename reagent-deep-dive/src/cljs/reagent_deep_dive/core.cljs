(ns reagent-deep-dive.core
  (:require [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary :include-macros true]
            [accountant.core :as accountant]
            [clojure.string :as string]
            [cljsjs.three :as THREE]))

;; -------------------------
;; Views

(defn home-page []
  [:div [:h2 "Welcome to reagent-deep-dive"]
   [:div [:a {:href "/simple-button"} "go to simple-button page"]]
   [:div [:a {:href "/simple-form"} "go to simple-form page"]]
   [:div [:a {:href "/concentric-circles"} "go to concentric-circles page"]]
   [:div [:a {:href "/many-circles"} "go to many-circles page"]]
   [:div [:a {:href "/counter"} "go to counter page"]]
   [:div [:a {:href "/waldo"} "go to waldo page"]]
   [:div [:a {:href "/reaction"} "go to reaction (sorting as reaction) page"]]
   [:div [:a {:href "/func-ret-func"} "go to func-ret-func (mousetraps) page"]]
   [:div [:a {:href "/with-let"} "go to with-let page"]]
   [:div [:a {:href "/announcement"} "go to announcement page"]]
   [:div [:a {:href "/mouse-pos"} "go to mouse-pos page"]]
   ;[:div [:a {:href "/vendor"} "go to lambda-3d page"]]
   [:div [:a {:href "/circles"} "go to circles page"]]
   [:div [:a {:href "/favorites-by-order-and-name"} "go to favorites-by-order-and-name page"]]
   [:div [:a {:href "/dessertinator"} "go to dessertinator page"]]])
   [:div [:a {:href "/scribble"} "go to scribble page"]]])

(defn simple-button []
  [:div#my-button.my-class1.my-class2 [:h2 "reagent-deep-dive:simple-button"]
   [:div [:button {:on-click
                   (fn [e]
                     (js/alert "You pressed the button!"))}
          "Do not press"]]])

(defn simple-form []
  [:div
   [:h3 "Greetings human"]
   [:form
    {:on-submit
     (fn [e]
       (.preventDefault e)
       (js/alert
        (str "You said: " (.. e -target -elements -message -value))))}
    [:label
     "Say something:"
     [:input
      {:name "message"
       :type "text"
       :default-value "Hello"}]]
    [:input {:type "submit"}]]])

(defn concentric-circles []
  [:svg {:style {:boder "1px solid"
                 :background "white"
                 :width "150px"
                 :height "150px"}}
   [:circle {:r 50, :cx 75, :cy 75, :fill "green"}]
   [:circle {:r 25, :cx 75, :cy 75, :fill "blue"}]
   [:path {:stroke-width 12
           :stroke "white"
           :fill "none"
           :d "M 30,40 C 100,40 50,110 120,110"}]])

(defn many-circles []
  (into
   [:svg {:style {:border "1px solid"
                  :background "white"
                  :width "600px"
                  :height "600px"}}]
   (for [i (range 12)]
     [:g
      {:transform (str
                   "translate(300,300) "
                   "rotate(" (* i 30) ") "
                   "translate(100)")}
      [concentric-circles]])))

(def c (reagent/atom 1))
(defn counter []
  [:div
   [:div "Current counter value: " @c]
   [:button
    {:disabled (>= @c 4)
     :on-click
     (fn clicked [e]
       (swap! c inc))}
    "inc"]
   [:button
    {:disabled (<= @c 1)
     :on-click
     (fn clicked [e]
       (swap! c dec))}
    "dec"]
   (into [:div] (repeat @c [concentric-circles]))])

(defn waldo []
  (let [show? (reagent/atom false)]
    (fn w []
      [:div
       (if @show?
         [:div
          [:h3 "You found me!"]
          [:img
           {:src "https://goo.gl/EzvMNp"
            :style {:height "320px"}}]]
         [:div
          [:h3 "Where are you now?"]
          [:img
           {:src "https://i.ytimg.com/vi/HKMlPDwmTYM/maxresdefault.jpg"
            :style {:height "320px"}}]])
       [:button
        {:on-click
         (fn [e]
           (swap! show? not))}
        (if @show? "reset" "search")]])))

(def rolls (reagent/atom [1 2 3 4]))
(def sorted-rolls (reagent.ratom/reaction (sort @rolls)))
(defn sorted-d20 []
  [:div
   [:button {:on-click (fn [e] (swap! rolls conj (rand-int 20)))} "Roll!"]
   [:p (pr-str @sorted-rolls)]
   [:p (pr-str (reverse @sorted-rolls))]])

;;part II
(defn a-better-mouse-trap [mouse]
  (let [mice (reagent/atom 1)]
    (fn render-mouse-trap [mouse]
      (into
       [:div
        [:button
         {:on-click
          (fn [e]
            (swap! mice (fn [m] (inc (mod m 4)))))}
         "Catch!"]]
       (repeat @mice mouse)))))
(defn mouse-trap []
  [:div
   [a-better-mouse-trap
    [:img
     {:src "https://www.domyownpestcontrol.com/images/content/mouse.jpg"
      :style {:width "150px" :border "1px solid"}}]]
   [a-better-mouse-trap
    [:img
     {:src "https://avatars1.githubusercontent.com/u/9254615?v=3&s=150"
      :style {:border "1px solid"}}]]])

(defn lambda [rotation x y]
  [:g {:transform (str "translate(" x "," y ")"
                       "rotate(" rotation ") ")}
   [:circle {:r 50, :fill "green"}]
   [:circle {:r 25, :fill "blue"}]
   [:path {:stroke-width 12
           :stroke "white"
           :fill "none"
           :d "M -45,-35 C 25,-35 -25,35 45,35 M 0,0 -45,45"}]])

(defn spinnable []
  (reagent/with-let [rotation (reagent/atom 0)]
                    [:svg
                     {:width 150 :height 150
                      :on-mouse-move
                      (fn [e]
                        (swap! rotation + 30))}
                     [lambda @rotation 75 75]]))

(defn several-spinnables []
  [:div
   [:h3 "Move your mouse over me"]
   [a-better-mouse-trap [spinnable]]])

(defn announcement []
  (reagent/create-class
   {:reagent-render
    (fn []
      [:h3 "I for one welcome our new insect overlords."])}))

(defn mouse-position []
  (reagent/with-let [pointer (reagent/atom nil)
                     handler (fn [e]
                               (swap! pointer assoc
                                      :x (.-pageX e)
                                      :y (.-pageY e)))
                     _ (js/document.addEventListener "mousemove" handler)]
                    [:div "Pointer moved to: " (str @pointer)]
                    (finally
                      (js/document.removeEventListener "mousemove" handler))))

;(defn create-renderer [element]
;  (doto (js/THREE.WebGLRenderer. #js {:canvas element :antialias true})
;    (.setPixelRatio js/window.devicePixelRatio)))

;(defn three-canvas [attributes camera scene tick]
;  (let [requested-animation (atom nil)]
;    (reagent/create-class
;     {:display-name "three-canvas"
;      :reagent-render
;      (fn three-canvas-render []
;        [:canvas attributes])
;      :component-did-mount
;      (fn three-canvas-did-mount [this]
;        (let [e (reagent/dom-node this)
;              r (create-renderer e)]
;          ((fn animate []
;             (tick)
;             (.render r scene camera)
;             (reset! requested-animation (js/window.requestAnimationFrame animate))))))
;      :component-will-unmount
;      (fn [this]
;        (js/window.cancelAnimationFrame @requested-animation))})))

;(defn create-scene[]
;  (doto (js/THREE.Scene.)
;        (.add (js/THREE.AmbientLight. 0x888888))
;        (.add (doto (js/THREE.DirectionalLight. 0xffff88 0.5)
;                    (-> (.-position) (.set -600 300 600))))
;        (.add (js/THREE.AxisHelper. 50))))

;(defn mesh [geometry color]
;  (js/THREE.SceneUtils.createMultiMaterialObject.
;      geometry
;      #js [(js/THREE.MeshBasicMaterial. #js {:color color :wireframe true})
;           (js/THREE.MeshLambertMaterial. #js {:color color})]))

;(defn fly-around-z-axis [camera scene]
;  (let [t (* (js/Date.now) 0.0002)]
;    (doto camera)))
;          (-> (.-position) (.set (* 100 (js/Math.cos t)) (* 100 (js/Math.sin t)) 100))
;          (.lookAt (.-position scene)))))

;(defn v3 [x y z]
;  (js/THREE.Vector3. x y z))

;(defn lambda-3d []
;  (let [camera (js/THREE.PerspectiveCamera. 45 1 1 2000)
;        curve (js/THREE.CubicBezierCurve3.
;               (v3 -30 -30 10)
;               (v3 0 -30 10)
;               (v3 0 30 10)
;               (v3 30 30 10))
;        path-geometry (js/THREE.TubeGeometry. curve 20 4 8 false)
;        scene (doto (create-scene)
;                    (.add
;                     (doto (mesh (js/THREE.CylinderGeometry. 40 40 5 24) "green")
;                           (-> (.-rotation) (.set (/ js/Math.PI 2) 0 0))))
;                    (.add
;                     (doto (mesh (js/THREE.CylinderGeometry. 20 20 10 24) "blue")
;                           (-> (.-rotation) (.set (/ js/Math.PI 2) 0 0))))
;                    (.add (mesh path-geometry "white")))
;        tick (fn []
;               (fly-around-z-axis camera scene))]
;    [three-canvas {:width 150 :height 150} camera scene tick]))

;(defn vendor []
;  [:div [lambda-3d]])

;lifecyle
;(defn puppy [x]
;  (log "puppy created, x:" x)
;  (let [mouse-over? (reagent/atom false)]
;    (fn [y]
;      (log "puppy rendered, x:" x " y:" y " mouse-over?:" @mouse-over?)
;      [:span {:on-mouse-over (fn [e] (reset! mouse-over? true))
;              :on-mouse-out (fn [e] (reset! mouse-over? false))}
;       [:img {:src "https://goo.gl/fMzXOU"
;              :style {:width "150px"
;                      :border "1px solid"
;                      :transform (str "scale(" (if @mouse-over? 1.1 1) ")")}}]])))
;(defn lifecyle-review []
;  (reagent/with-let [x (reagent/atom "1")]
;                    [:id [:label "type in a value for x: "
;                          [:input {:on-change (fn [e] (reset! x (.. e -target -value)))}]]
;                     [with-log [a-better-mouse-trap [puppy @x]]]]))

;; Part III
(defn circles []
  [:svg {:width 200 :height 200}
   (for [i (range 30)]
     [:circle
      {:r (* (inc i) 5)
       :cx 100
       :cy 100
       :fill "none"
       :stroke (str "rgb(0, " (* i 10) "," (* (- 30 i) 10) ")")}])])

(def favorites
  (reagent/atom
    {"d632" {:name "Princess torte." :order 2}
     "1ae2" {:name "Black forest gateau." :order 3}
     "5117" {:name "Ice cream." :order 1}}))

(defn list-by [entities sort-k]
  [:ul
   (for [[k v] (sort-by (comp sort-k val) @entities)]
     ^{:key k}
     [:li (:name v)])])

(defn favorites-by-order-and-name []
  [:div
   [:h3 "By order"]
   [list-by favorites :order]
   [:h3 "By name"]
   [list-by favorites :name]])

(def words
  ["ice" "cream" "chocolate" "pastry" "pudding" "raspberry" "mousse"
   "vanilla" "wafer" "waffle" "cake" "torte" "gateau" "pie" "cookie"
   "cupcake" "mini" "hot" "caramel" "meringue" "lemon" "marzipan" "mocha"
   "strawberry" "tart" "custard" "fruit" "baklava" "jelly" "banana" "coconut"])

(defn rand-name []
  (clojure.string/capitalize (clojure.string/join " " (take (+ 2 (rand-int 5)) (shuffle words)))))

(def desserts (reagent/atom ()))

(defn make-a-dessert [e]
  (swap! desserts conj {:id (random-uuid)
                        :name (rand-name)}))

(defn make-many-desserts [e]
  (dotimes [i 100]
    (make-a-dessert nil)))

(defn color-for [x]
  (str "#" (.toString (bit-and (hash x) 0xFFFFFF) 16)))

(defn dessert-item [{:keys [id name]}]
  [:li
   [:svg {:width 50 :height 50}
    [:circle
     {:r 20 :cx 25 :cy 25 :fill (color-for id)}]
    [:rect {:x 15 :y 15 :width 20 :height 20 :fill (color-for name)}]]
   [:span [:em [:strong name]]]])

(defn desserts-list []
  [:ol
   (for [dessert @desserts]
     ^{:key (:id dessert)}
     [dessert-item dessert])])

(defn dessertinator []
  [:div
   [:button {:on-click make-a-dessert} "Invent a new dessert"]
   [:button {:on-click make-many-desserts} "Invent 100 new desserts"]
   [desserts-list]])

;;part IV
(def my-drawing (atom []))
(swap! my-drawing conj [50 100 150 100])
(defn scribble1 [drawing]
  (into
   [:svg
    {:width "100%"
     :height 200
     :stroke "black"
     :fill "none"}]
   (for [[x y & more-options] @drawing]
     [:path {:d (str "M " x " " y " L " (string/join " " more-options))}])))
(defn scribble [] [scribble1 my-drawing])
;; -------------------------
;; Routes
(defonce page (atom #'home-page))

(defn current-page []
  [:div [@page]])

(secretary/defroute "/" []
                    (reset! page #'home-page))

(secretary/defroute "/simple-button" []
                    (reset! page #'simple-button))

(secretary/defroute "/simple-form" []
                    (reset! page #'simple-form))

(secretary/defroute "/concentric-circles" []
                    (reset! page #'concentric-circles))

(secretary/defroute "/many-circles" []
                    (reset! page #'many-circles))

(secretary/defroute "/counter" []
                    (reset! page #'counter))

(secretary/defroute "/waldo" []
                    (reset! page #'waldo))

(secretary/defroute "/reaction" []
                    (reset! page #'sorted-d20))

;;part II
(secretary/defroute "/func-ret-func" []
                    (reset! page #'mouse-trap))

(secretary/defroute "/with-let" []
                    (reset! page #'several-spinnables))

(secretary/defroute "/mouse-pos" []
                    (reset! page #'mouse-position))

(secretary/defroute "/announcement" []
                    (reset! page #'announcement))

;(secretary/defroute "/vendor" []
;                    (reset! page #'vendor))

;;part III
(secretary/defroute "/circles" []
  (reset! page #'circles))

(secretary/defroute "/favorites-by-order-and-name" []
  (reset! page #'favorites-by-order-and-name))

(secretary/defroute "/dessertinator" []
                    (reset! page #'dessertinator))
;part IV
(secretary/defroute "/scribble" []
                    (reset! page #'scribble))

;; -------------------------
;; Initialize app
(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
   {:nav-handler
    (fn [path]
      (secretary/dispatch! path))
    :path-exists?
    (fn [path]
      (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
