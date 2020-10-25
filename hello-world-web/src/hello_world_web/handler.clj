(ns hello-world-web.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
  (GET "/" [r :as request] (str "Hello World, your request: " request))
  (context "/user/:user-id" [user-id]
    ;;http localhost:3000/user/carlos/29
    (GET "/:number{[0-9]+}" [number]
        (str "<h1>Hello <em>" user-id "</em> your number is: " number "</h1>"))

    ;;http localhost:3000/user/carlos greeting==hola
    (GET "/:alias" [alias greeting]
        {:status 200
          :headers {"Content-Type" "text/html; charset=utf-8"}
          :body (str "<h2> [" greeting "] @" alias ":<em>" user-id "</em></h2>")}))

  ;;http :3000/destructure name==carlos last==duque x==myX y==myY age==39 z==myZ addr==Stgo num==29
  (GET "/destructure" [x y & z :as r]
       (str "x: " x ", y: " y ", z: " z " \n\n|full request: " r))
  (GET "/whats-my-ip" [r :as request]
       {:status 200
        :headers {"Content-Type" "text/plain"}
        :body (:remote-addr request)})
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
