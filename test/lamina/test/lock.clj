;;   Copyright (c) Zachary Tellman. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;   which can be found in the file epl-v10.html at the root of this distribution.
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any other, from this software.

(ns lamina.test.lock
  (:use
    [lamina.core lock]
    [clojure test])
  (:require
    [criterium.core :as c]))

(defmacro bench [name & body]
  `(do
     (println "\n-----\n lamina.core.lock -" ~name "\n-----\n")
     (c/quick-bench
       (dotimes [_# (int 1e6)]
         ~@body)
       :reduce-with #(and %1 %2))))

(deftest ^:benchmark benchmark-locks
  (let [lock (asymmetric-reentrant-lock)]
    (bench "reentrant acquire/release"
      (acquire lock)
      (release lock))
    (bench "reentrant non-exclusive"
      (with-reentrant-lock lock 1))
    (bench "reentrant exclusive"
      (with-exclusive-reentrant-lock lock 1)))
  (let [lock (asymmetric-lock)]
    (bench "acquire/release"
      (acquire lock)
      (release lock))
    (bench "non-exclusive"
      (with-lock lock 1))
    (bench "exclusive"
      (with-exclusive-lock lock 1))
    (bench "non-exclusive*"
      (with-lock* lock 1))
    (bench "exclusive*"
      (with-exclusive-lock* lock 1))))
