(ns sandbox.reitit
  (:require [ring.util.http-response :as response]
            [muuntaja.core :as m]
            [malli.core :as malli]
            [reitit.ring :as rr]
            [reitit.ring.coercion :as rrc]
            [reitit.ring.middleware.muuntaja :as rrmm]
            [reitit.ring.middleware.parameters :as rrmp]
            [reitit.coercion.malli :as rcm]
            [atschool.api.middleware.core :as middleware])
 (:gen-class))

(require '[reitit.coercion.schema])
(require '[schema.core :as s])

(def PositiveInt (s/constrained s/Int pos? 'PositiveInt))

(def multi-handler
  (fn [{:keys [parameters]}]
    (let [total (* (-> parameters :path :first)
                   (-> parameters :query :second)
                   (-> parameters :body :third))]
      {:status 200
       :body {:total total}})))

(def app
  (rr/ring-handler
    (rr/router
      ["/api"
       ["/ping" {:name ::ping
                 :get (fn [_]
                        {:status 200
                         :body "pong"})}]
       ["/plus/:z" {:name ::plus
                    :post {:coercion reitit.coercion.schema/coercion
                           :parameters {:query {:x s/Int}
                                        :body {:y s/Int}
                                        :path {:z s/Int}}
                           :responses {200 {:body {:total PositiveInt}}}
                           :handler (fn [{:keys [parameters]}]
                                      (let [total (+ (-> parameters :query :x)
                                                     (-> parameters :body :y)
                                                     (-> parameters :path :z))]
                                        {:status 200
                                         :body {:total total}}))}}]]
       ["/multi/:first" {:name ::plus
                        :coercion rcm/coercion
                        :parameters {:path [:map [:first int?]]
                                     :query [:map [:second int?]]
                                     :body [:map [:third int?]]}
                        :responses {200 {:body [:map [:total int?]]}}
                        :post {:handler multi-handler}}]
      {:data {:middleware [rrc/coerce-exceptions-middleware
                           rrc/coerce-request-middleware
                           rrc/coerce-response-middleware]}})))

;;;sample
(app {:request-method :post
      :uri "/api/plus/3"
      :query-params {"x" "1"}
      :body-params {:y 2}})

;;metosin.fi retiti-ring
(defn sshandler [_]
  {:status 200 :body "ok"})

(defn sswrap [handler id]
  (fn [request]
    (update (handler request) :via (fnil conj '()) id)))

(def ssapp
  (rr/ring-handler
    (rr/router
      ["/api" {:middleware [#(sswrap % :api)]}
       ["/ping" sshandler]
       ["/admin" {:middleware [[sswrap :admin]]}
        ["/db" {:middleware [[sswrap :db]]
                :delete {:middleware [[sswrap :delete]]
                         :handler sshandler}}]]]
      {:data {:middleware [[sswrap :top]]}}) ;; all routes
    (rr/create-default-handler)))

(ssapp {:request-method :delete
      :uri "/api/admin/db"})

;;metosin.fi reitit-ring
;;middleware magic
(require '[clojure.set :as set])

(defn wrap-enforce-roles [handler]
  (fn [{:keys [roles] :as request}]
    (let [required (some-> request (rr/get-match) :data :roles)]
      (if (and (seq required) (not (set/subset? required roles)))
        {:status 403 :body "forbidden"}
        (handler request)))))

(def rrapp
  (rr/ring-handler
    (rr/router
      ["/api"
       ["/ping" sshandler]
       ["/admin" {:roles #{:admin}}
        ["/ping" sshandler]]]
      {:data {:middleware [wrap-enforce-roles]}})))

(rrapp {:request-method :get :uri "/api/ping"})
(rrapp {:request-method :get :uri "/api/admin/ping"})
(rrapp {:request-method :get :uri "/api/admin/ping" :roles #{:admin}})

;;metosin.fi reitit-ring
;;coercion
(require '[reitit.ring.coercion :as rrc])
(require '[reitit.coercion.spec])
(require '[reitit.ring :as rr])

(def ccapp
  (rr/ring-handler
    (rr/router
      ["/api"
       ["/ping" (fn [_]
                  {:status 200
                   :body "pong"})]
       ["/plus/:z" {:post {:coercion reitit.coercion.spec/coercion
                           :parameters {:query {:x int?}
                                        :body {:y int?}
                                        :path {:z int?}}
                           :responses {200 {:body {:total pos-int?}}}
                           :handler (fn [{:keys [parameters]}]
                                      ;;parameters are coerced
                                      (let [x (-> parameters :query :x)
                                            y (-> parameters :body :y)
                                            z (-> parameters :path :z)
                                            total (+ x y z)]
                                        {:status 200
                                         :body {:total total}}))}}]]
      {:data {:middleware [rrc/coerce-exceptions-middleware
                           rrc/coerce-request-middleware
                           rrc/coerce-response-middleware]}})))

;;valid
(ccapp {:request-method :post
        :uri "/api/plus/3"
        :query-params {"x" "1"}
        :body-params {:y 2}})

;;invalid
(ccapp {:request-method :post
        :uri "/api/plus/3"
        :query-params {"x" "abba"}
        :body-params {:y 2}})

;; muuntaja
(require '[reitit.ring :as ring])
(require '[reitit.ring.coercion :as rrc])
(require '[reitit.coercion.spec :as rcs])
(require '[reitit.coercion.malli :as rcm])
(require '[reitit.ring.middleware.muuntaja :as muuntaja])
(require '[muuntaja.core :as m])

(require '[ring.adapter.jetty :as jetty])

(def mmapp
  (ring/ring-handler
    (ring/router
      [["/math"
        {:post {:summary "negotiated request & response (json, edn, transit)"
                :parameters {:body [:map [:x int?] [:y int?]]}
                :responses {200 {:body [:map [:total int?]]}}
                :handler (fn [{{{:keys [x y]} :body} :parameters}]
                           {:status 200
                            :body {:total (+ x y)}})}}]]
      {:data {:muuntaja m/instance
              :coercion rcm/coercion
              :middleware [rrmp/parameters-middleware
                           muuntaja/format-negotiate-middleware
                           muuntaja/format-request-middleware
                           muuntaja/format-response-middleware
                           rrc/coerce-exceptions-middleware
                           rrc/coerce-request-middleware
                           rrc/coerce-response-middleware]}})))

;;                           muuntaja/format-middleware
(defonce srvr (jetty/run-jetty #'mmapp {:port 3000, :join? false}))
(.stop srvr)
(.start srvr)

(:body (mmapp {:uri "/math"
        :request-method :post
        :query-params {"x" "45" "y" "12"}
        :body-params {:x 111 :y 234}}))

