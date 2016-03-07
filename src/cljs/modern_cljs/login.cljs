(ns modern-cljs.login
  (:require [domina.core :refer [append! by-class by-id destroy! set-value! value]]
            [hiccups.runtime])
  (:require-macros [hiccups.core :refer [html]]))

(defn validate-form []
  (if (and (> (count (value (by-id "email"))) 0)
           (> (count (value (by-id "password"))) 0))
    true
    (do (js/alert "Please, complete the form!")
        false)))

(defn ^:export init []
  ;; verify that js/document exists and that it has a getElementById
  ;; property
  (if (and js/document
           (aget js/document "getElementById"))
    ;; get loginForm by element id and set its onsubmit property to
    ;; our validate-form function
    (let [login-form (.getElementById js/document "loginForm")]
      (set! (.-onsubmit login-form) validate-form))))
