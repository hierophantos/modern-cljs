(ns modern-cljs.login
  (:require [domina.core :refer [append! by-class by-id destroy! set-value!
                                 value prepend! value]]
            [domina.events :refer [listen! prevent-default] :as evt]
            [hiccups.runtime]
            )
  (:require-macros [hiccups.core :refer [html]]))

;; 4 to 8, at least one numeric digit
(def ^:dynamic *password-re* 
  #"^(?=.*\d).{4,8}$")

(def ^:dynamic *email-re* 
  #"^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$")

#_(defn validate-form []
  (if (and (> (count (value (by-id "email"))) 0)
           (> (count (value (by-id "password"))) 0))
    true
    (do (js/alert "Please, complete the form!")
        false)))

(defn validate-email [email]
  (destroy! (by-class "email"))
  (if (not (re-matches *email-re* (value email)))
    (do
      (prepend! (by-id "loginForm") (html [:div.help.email "Wrong email"]))
      false)
    true))

(defn validate-password [password]
  (destroy! (by-class "password"))
  (if (not (re-matches *password-re* (value password)))
    (do
      (append! (by-id "loginForm") (html [:div.help.password "Wrong password"]))
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


#_(defn ^:export init []
  ;; verify that js/document exists and that it has a getElementById
  ;; property
  (if (and js/document
           (aget js/document "getElementById"))
    ;; get loginForm by element id and set its onsubmit property to
    ;; our validate-form function
    (let [login-form (.getElementById js/document "loginForm")]
      (set! (.-onsubmit login-form) validate-form))))

;; updated init fn from tut 10
(defn ^:export init []
  (if (and js/document
           (aget js/document "getElementById"))
    (let [email (by-id "email")
          password (by-id "password")]
      (listen! (by-id "submit") :click (fn [e] (validate-form e)))
      (listen! email :blur (fn [evt] (validate-email email)))
      (listen! password :blur (fn [evt] (validate-password password))))))











#_(defn sqrt [n]
  (.sqrt js/Math n))

#_(def phi
    (/ (+ 1 (sqrt 5)) 2))
