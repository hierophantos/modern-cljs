(ns modern-cljs.login
  (:require [domina.core :refer [append! by-class by-id destroy! set-value!
                                 value prepend! value attr] :as dom]
            [domina.events :refer [listen! prevent-default] :as evt]
            [hiccups.runtime]
            )
  (:require-macros [hiccups.core :refer [html]]))

#_(defn validate-email [email]
  (destroy! (by-class "email"))
  (if (not (re-matches *email-re* (value email)))
    (do
      (prepend! (by-id "loginForm") (html [:div.help.email "Wrong email"]))
      false)
    true))

(defn validate-email [email]
  (destroy! (by-class "email"))
  (if (not (re-matches (re-pattern (attr email :pattern)) (value email)))
    (do
      (prepend! (by-id "loginForm") (html [:div.help.email (attr email :title)]))
      false)
    true))

(defn validate-password [password]
  (destroy! (by-class "password"))
  (if (not (re-matches (re-pattern (attr password :pattern)) (value password)))
    (do
      (append! (by-id "loginForm") (html [:div.help.password (attr password :title)]))
      false)
    true))

;; Updated fn in tutorial 10 using evt/prevent-default and other domina.events
(defn validate-form [evt]
  (let [email (by-id "email")
        password (by-id "password")
        email-val (value password)
        password-val (value password)]
    (if (or (empty? email-val) (empty? password-val))
      (do
        (destroy! (by-class "help"))
        (prevent-default evt)
        (append! (by-id "loginForm") (html [:div.help
                                            "Please complete the form"])))
      (if (and (validate-email email)
               (validate-password password))
        true
        (prevent-default evt)))))


(defn ^:export init []
  (if (and js/document
           (aget js/document "getElementById"))
    (let [email (by-id "email")
          password (by-id "password")]
      (listen! (by-id "submit") :click (fn [e] (validate-form e)))
      (listen! email :blur (fn [evt] (validate-email email)))
      (listen! password :blur (fn [evt] (validate-password password))))))



