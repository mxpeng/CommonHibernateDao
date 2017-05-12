# CommonHibernateDao

### 说明

通用的hibernate dao，使用后可以节省编写各类dao的大量时间，在使用时只需注入该类，调用该类中适合方法执行sql即可

### 注意

- 虽然使用hibernate 但是为了方便开发，不使用hql，仅支持原生sql语句
- 若调用需要传入class的方法，请保证该类已经使用jpa和数据库表进行映射
- 需要传入参数的方法 参数顺序需要和sql 中的 `?`一一对应，少量的参数可用可变参数一一传入，大量的参数可用Object数组传入

### 方法

- `T get(Class<T> clazz, Serializable id)`

    描述：从数据库获取对象 ，id指的该类的持久化标识符，也就是该类对应的数据库表主键值

    返回：传入class的对象

- `T persist(T entity)`

  描述：持久化传入的对象，也就是保存该对象到数据

  返回：持久化后的对象

- `T update(T entity)`

    描述：更新传入的对象，若该对象的主键值在对应数据库表中存在就更新，否则就保存

    返回：更新或者保存后的对象

- `T delete(T entity)`

    描述：删除传入的对象，若该对象的主键值在对应数据库表中存在就删除

    返回：删除的对象


- `boolean deleteById(Class<T> clazz, Serializable id)`

    描述：删除传入对象，id指的该类的持久化标识符

    返回：是否删除成功

- `List<T> all(Class<T> clazz)`

    描述：获取传入类对应表中的全部数据

    返回：包含该类数据库表中全部数据的list

- `int total(Class<T> clazz)`

    描述：获取传入类对应表的数据量 

    返回：整数数量

- `boolean addBentch(List<T> entitys)`

    描述：批量保存数据

    返回：是否成功

- `boolean deleteBentch(List<T> entitys)`

    描述：批量删除数据

    返回：是否成功

- `T uniqueResultEntity(String sql, Class<T> clazz, Object... param)`

    描述：根据传入的sql语句class，获取唯一的对象结果

    返回：查询的实体或者null

- `Object uniqueResultObject(String sql, Object... param)`

    描述：根据传入的sql语句查询唯一的结果，结果可以是字符串，数字等

    返回：查询的唯一结果

- `boolean executeUpdate(String sql, Object... param)`

    描述：根据sql 执行各类sql语句，可以是insert语句，也可以是update语句 等等

    返回：是否执行成功

- `int getQuerySize(String sql, Object... param)`

    描述：获取该sql 执行后得到的结果数据的数量

    返回：整形数量

- `List<T> queryListEntity(String sql, Class<T> clazz, Object... param)`

    描述：将查询结果包装成实体类

    返回：list entity

- `List<Map<String, Object>> queryListMap(String sql, Object... param)`

    描述：将查询结果包装成map

    返回：list map

- `List<T> queryListObject(String sql, Object... param)`

    描述：查询结果

    返回：list object

- `List<T> queryListEntityByPage(String sql, Class<T> clazz, int start, int limit, Object... param)`

    描述：将查询结果包装成实体类，分页查询，没有分页信息返回

    返回：list entity

- `List<Map<String, Object>> queryListMapByPage(String sql, int start, int limit, Object... param)`

    描述：将查询结果包装成map，分页查询，没有分页信息返回

    返回：list map

- `List<T> queryListObjectByPage(String sql, int start, int limit, Object... param)`

    描述：查询结果，分页查询，没有分页信息返回

    返回：list object

- `Paging<T> queryListEntityByPage(String sql, Class<T> clazz, Paging paging, Object... param)`

    描述：将查询结果包装成实体类，分页查询，将分页信息包装到paging类中返回

    返回：Paging entity

- `Paging<Map<String, Object>> queryListMapByPage(String sql, Paging paging, Object... param)`

    描述：将查询结果包装成map，分页查询，将分页信息包装到paging类中返回

    返回：Paging map

- `Paging<T> queryListObjectByPage(String sql, Paging paging, Object... param)`

    描述：查询结果，分页查询，将分页信息包装到paging类中返回

    返回：Paging object

- `SessionFactory getSessionFactory()`

    描述：获取session工厂

    返回：session工厂

- `Session openSession()`

    描述：获取一个打开的session

    返回：session

