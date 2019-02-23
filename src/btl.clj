(ns btl
  (:require [clj-http.client :as http]
            [cheshire.core :as json]))

(defn authenticate [{:keys [userName password userZehut]
                     :as credentials}]
  (let [{:keys [body]}
        (try
          (http/request
            {:method :post
             :url "https://ps.btl.gov.il/api/loginApi/authenticate"
             :content-type :json
             :form-params (merge {:StationId (java.util.UUID/randomUUID)}
                                 credentials)
             :as :json})
          (catch clojure.lang.ExceptionInfo e
            (throw (ex-info "שגיאה בהתחברות לביטח לאומי"
                            (-> e ex-data :body (json/parse-string true))))))
        err (-> body :ErrorMessage)]
    (if err
      (throw (ex-info err body))
      body)))
