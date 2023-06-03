(ns cybermonday.lowering
  (:require
   [cybermonday.utils :refer [hiccup? make-hiccup-node gen-id]]
   [clojure.string :as str]
   [clojure.walk :as walk])
  #?(:clj (:import java.lang.Integer)))

#?(:clj (set! *warn-on-reflection* true))

(def default-tags
  "Deafult mappings from IR tags to HTML tags where transformation isn't required"
  {:markdown/hard-line-break :br
   :markdown/inline-math :code
   :markdown/html-comment nil
   :markdown/reference nil})

(defn lower-soft-line-break [[_]] "\n")

(defn lower-heading [[_ attrs & body :as node]]
  (make-hiccup-node
   (keyword (str "h" (:level attrs)))
   (dissoc
    (assoc attrs
           :id (if (nil? (:id attrs))
                 (gen-id node)
                 (:id attrs)))
    :level)
   body))

(defn lower-fenced-code-block [[_ attrs & body]]
  [:pre {}
   (make-hiccup-node
    :code (dissoc (assoc attrs :class (str "language-" (:language attrs))) :language) body)])

(defn lower-indented-code-block [[_ attrs & body]]
  [:pre attrs
   (make-hiccup-node
    :code body)])

(defn lower-table-cell [[_ attrs & body]]
  (make-hiccup-node
   (if (:header? attrs) :th :td)
   (if-let [align (:alignment attrs)]
     (dissoc (assoc attrs :align align) :alignment)
     {})
   body))

(defn lower-mail-link [[_ {:keys [address] :as attrs}]]
  [:a (dissoc (assoc attrs :href (str "mailto:" address)) :address)])

(defn lower-autolink [[_ {:keys [href]}]]
  [:a {:href href} href])

; FIXME pretty footnotes at bottom

(defn lower-footnote [[_ {:keys [id]}]]
  [:sup {:id (str "fnref-" id)}
   [:a {:href (str "#fn-" id)}]])

(defn lower-footnote-block [[_ {:keys [id content]}]]
  [:li {:id (str "fn-" id)}
   [:p
    [:span content]
    [:a {:href (str "#fnref-" id)} "↩"]]])

(defn lower-link-ref [[_ {:keys [reference]} body]]
  (when reference
    [:a {:href (:url (second reference))
         :title (:title (second reference))} body]))

(defn lower-image-ref [[_ {:keys [reference]} body]]
  (when reference
    [:img {:src (:url (second reference))
           :title (:title (second reference))} body]))

(defn lower-task-list-item [[_ {:keys [checked?]} & body]]
  (make-hiccup-node
   :li
   (conj body [:input {:checked checked? :disabled true :type "checkbox"}])))

(defn lower-list-item [[_ attrs & body]]
  (make-hiccup-node
   :li attrs (if (= 1 (count body))
               (drop 2 (first body))
               body)))

(defn lower-toc [[_ {:keys [style]}] full-ir]
  (letfn [(get-levels-from-style
            ;; Returns a set of valid integer levels from the style attribute or
            ;; nil if level list is malformed
            ;; See https://github.com/vsch/flexmark-java/wiki/Table-of-Contents-Extension#syntax
            [style]
            (try
              (let [levels-string (->> style (re-find #"levels=(\S*)") second)
                    levels-list (->> (str/split levels-string #",")
                                     (map #(str/split % #"-"))
                                     (map (fn [x] (map #?(:clj #(Integer/parseInt %)
                                                          :cljs #(js/parseInt %)) x))))]
                (reduce (fn [acc item]
                          (cond
                            (= 1 (count item)) (conj acc (first item))
                            (= 2 (count item)) (apply conj acc (range (first item)
                                                                      (+ 1 (second item))))
                            :else (reduced nil)))
                        #{}
                        levels-list))
              (catch #?(:clj Throwable :cljs :default) _ nil)))
          (find-correct-insert-path
            ;; Determine the key path to use as update-in argument in order to
            ;; insert a heading into the correct place in the heading tree
            [insert-level heading-tree]
            ;; If tree is empty or last heading in tree is less significant, return nil to
            ;; communicate that the heading should simply be conjed onto the end of tree
            (when (and (seq heading-tree) (-> heading-tree first :level (< insert-level)))
              ;; Start looking for a place to add the heading at the last top-level heading
              (loop [heading-path [(- (count heading-tree) 1)]]
                (let [{:keys [level children] :as heading} (get-in heading-tree heading-path)
                      node-children-count (count children)]
                  ;; If this heading is non-existent or less significant than the new heading,
                  ;; append the new heading to the children vector containing this heading
                  (if (or (not heading) (< insert-level level)) (pop heading-path)
                    ;; Else look at the last top-level heading in this heading's children next
                      (recur (conj heading-path :children (- node-children-count 1))))))))
          (heading->hiccup [{:keys [children title id]}]
            [:li [:a {:href (str "#" id)} title]
             (when children (into [:ul] children))])]
    (let [toc-levels (or (get-levels-from-style style) #{1 2 3 4 5 6})]
      (->> full-ir
           (filter (fn find-headings [ir-node]
                     (and (vector? ir-node)
                          (= :markdown/heading (first ir-node))
                          (contains? toc-levels (-> ir-node second :level)))))
           ;; Create tree of map nodes with :children keys instead of vector
           ;; nodes to make insertion a bit easier
           (map (fn prepare-headings-tree [[_tag attr title :as heading]]
                  (assoc attr :title title :id (gen-id heading) :children [])))
           (reduce (fn create-headings-tree [tree {:keys [level] :as heading}]
                     (if-let [insert-path (find-correct-insert-path level tree)]
                       (update-in tree insert-path #(conj % heading))
                       (conj tree heading)))
                   [])
           ;; Turn all the headings into Hiccup, starting with leaf nodes
           (walk/postwalk
            (fn [node]
              (if (:children node)
                (heading->hiccup node)
                node)))
           (into [:ul {:class "table-of-contents"}])))))

(defn lower-fallback [[tag attrs & body]]
  (if (contains? default-tags tag)
    (when-let [new-tag (default-tags tag)]
      (make-hiccup-node new-tag attrs body))
    (make-hiccup-node tag attrs body)))

(def default-lowering
  "Mapping from the IR nodes to transformation fns"
  {:markdown/heading lower-heading
   :markdown/fenced-code-block lower-fenced-code-block
   :markdown/indented-code-block lower-indented-code-block
   :markdown/table-cell lower-table-cell
   :markdown/mail-link lower-mail-link
   :markdown/footnote lower-footnote
   :markdown/autolink lower-autolink
   :markdown/footnote-block lower-footnote-block
   :markdown/task-list-item lower-task-list-item
   :markdown/link-ref lower-link-ref
   :markdown/image-ref lower-image-ref
   :markdown/bullet-list-item lower-list-item
   :markdown/ordered-list-item lower-list-item
   :markdown/table-of-contents lower-toc
   :markdown/soft-line-break lower-soft-line-break})

(defn lower-ir
  "Transforms the IR tree by lowering nodes to their HTML representation"
  ([ir lowering-map]
   (let [final-map (conj default-lowering lowering-map)]
     (walk/prewalk
      (fn [item]
        (if (hiccup? item)
          (let [tag (first item)]
            (if-let [transform-fn (tag final-map)]
              (if (= :markdown/table-of-contents tag)
                (transform-fn item ir)
                (transform-fn item))
              (if (contains? default-tags tag)
                (lower-fallback item)
                item)))
          item))
      ir)))
  ([ir] (lower-ir ir default-lowering)))

(defn deep-merge
  "Recursively merges maps and vectors"
  [& contents]
  (cond
    (every? vector? contents) (reduce into contents)
    (some map? contents) (apply merge-with deep-merge contents)))

(defn apply-post-attrs
  "Merges in attributes for the final HTML ast from given map of html keywords and attributes"
  [hiccup attr-map]
  (walk/postwalk
   (fn [item]
     (if (hiccup? item)
       (let [[tag attrs & body] item]
         (apply vector tag (deep-merge attrs (attr-map tag)) body))
       item))
   hiccup))

(defn to-html-hiccup
  "Transforms a cybermonday IR into standard HTML hiccup
  In an optional options map:
  `:lower-fns` supplies a mapping from IR keyword to lowering fn
  `default-attrs` supplies a mapping from HTML keyword to default node attributes"
  [ir & [{:keys [lower-fns default-attrs]}]]
  (-> ir
      (lower-ir (or lower-fns default-lowering))
      (apply-post-attrs (or default-attrs {}))))
