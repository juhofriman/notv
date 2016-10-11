(ns notv.util
  (:gen-class))

(defn- defs-to-map 
  [defs]
  (zipmap (take-nth 2 defs) (take-nth 2 (rest defs))))

(defn- apply-fn
  [dmap type val]
  (if-let [fn (get dmap type)]
    (apply fn [val])
    val))

(defn type-map
  "Applies type mappings to d. Type mappings are defined as varargs: java.lang.Long inc java.lang.String identity and so on. If does not contain mapping for type, value is returned as is"
  [d & defs]
  (let [dmap (defs-to-map defs)]
    (into {} (for [[k v] d]
               [k (apply-fn dmap (type v) v)]))))


