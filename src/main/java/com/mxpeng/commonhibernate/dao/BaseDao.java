package com.mxpeng.commonhibernate.dao;

import com.mxpeng.commonhibernate.core.Paging;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;


/**
 * Created by Peng on 2016/12/22.
 */
@Repository
@Transactional
public class BaseDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDao.class);
    private static final String TOTAL_SQL = "select count(*) from (%s) t";

    @Resource
    private SessionFactory sessionFactory;

    /********************************************************************************
     * 单表常用操作
     */
    public <T> T get(Class<T> clazz, Serializable id) {
        Session session = this.sessionFactory.openSession();
        try {
            return session.get(clazz, Objects.requireNonNull(id));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(String.format("get [%s] error", clazz.getName()), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public <T> T persist(T entity) {
        Session session = this.sessionFactory.openSession();
        try {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(entity);
            tx.commit();
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(String.format("persist [%s] error", entity.toString()), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public <T> T update(T entity) {
        Session session = this.sessionFactory.openSession();
        try {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(entity);
            tx.commit();
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(String.format("update [%s] error", entity.toString()), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public <T> T delete(T entity) {
        Session session = this.sessionFactory.openSession();
        try {
            Transaction tx = session.beginTransaction();
            session.delete(Objects.requireNonNull(entity));
            tx.commit();
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(String.format("delete [%s] error", entity.toString()), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public <T> boolean deleteById(Class<T> clazz, Serializable id) {
        String sql = "DELETE FROM " + getTableName(clazz) + " WHERE " + getPkColunmName(clazz) + "=?";
        return executeUpdate(sql, id);
    }

    public <T> List<T> all(Class<T> clazz) {
        Session session = this.sessionFactory.openSession();
        try {
            List<T> result = new ArrayList<>();
            String sql = "SELECT * FROM " + getTableName(clazz);
            result.addAll(session.createSQLQuery(sql).addEntity(clazz).list());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(String.format("queryAll [%s] error", clazz.getName()), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public <T> int total(Class<T> clazz) {
        Session session = this.sessionFactory.openSession();
        try {
            String sql = "select count(*) from " + getTableName(clazz);
            SQLQuery query = session.createSQLQuery(sql);
            return Integer.parseInt(query.uniqueResult().toString());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(String.format("getTotal [%s] error", clazz.getName()), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return 0;
    }


    public <T> boolean addBentch(List<T> entitys) {
        Session session = this.sessionFactory.openSession();
        try {
            Transaction tx = session.beginTransaction();
            for (int i = 0; i < entitys.size(); i++) {
                session.saveOrUpdate(Objects.requireNonNull(entitys.get(i)));
                if (i % 20 == 0) {
                    session.flush();
                    session.clear();
                }
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(String.format("addBentch [%s] error", entitys.toString()), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return false;
    }

    public <T> boolean deleteBentch(List<T> entitys) {
        Session session = this.sessionFactory.openSession();
        try {
            Transaction tx = session.beginTransaction();
            for (int i = 0; i < entitys.size(); i++) {
                session.delete(Objects.requireNonNull(entitys.get(i)));
                if (i % 20 == 0) {
                    session.flush();
                    session.clear();
                }
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(String.format("deleteBentch [%s] error", entitys.toString()), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return false;
    }

    /******************************************************************************************************
     *
     */

    /**
     * 查询clazz类的唯一对象返回
     *
     * @return 实体对象
     */
    public <T> T uniqueResultEntity(String sql, Class<T> clazz, Object... param) {
        List<T> result = this.queryListEntity(sql, clazz, param);
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public Object uniqueResultObject(String sql, Object... param) {
        Session session = this.sessionFactory.openSession();
        try {
            Object result = bindSQLQueryParams(session.createSQLQuery(sql), param).uniqueResult();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(String.format("query [%s] error", sql), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public boolean executeUpdate(String sql, Object... param) {
        Session session = this.sessionFactory.openSession();
        try {
            Transaction tx = session.beginTransaction();
            SQLQuery query = bindSQLQueryParams(session.createSQLQuery(sql), param);
            boolean flag = query.executeUpdate() >= 0;
            tx.commit();
            return flag;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(String.format("uniqueResult [%s] error", sql), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return false;
    }

    public int getQuerySize(String sql, Object... param) {
        Session session = this.sessionFactory.openSession();
        try {
            return bindSQLQueryParams(session.createSQLQuery(sql), param).list().size();
            // return Integer.parseInt(bindSQLQueryParams(session.createSQLQuery(String.format(TOTAL_SQL, sql)), param).uniqueResult().toString());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(String.format("query [%s] error", sql), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return 0;
    }

    /********************************************************************************
     * 单表,多表 查询操作
     */

    public <T> List<T> queryListEntity(String sql, Class<T> clazz, Object... param) {
        return this.queryList(sql, clazz, "entity", param);
    }

    public List<Map<String, Object>> queryListMap(String sql, Object... param) {
        return this.queryList(sql, null, "map", param);
    }

    /**
     * 查询某一项的列表，比如全部id的列表
     */
    public <T> List<T> queryListObject(String sql, Object... param) {
        return this.queryList(sql, null, "object", param);
    }

    private <T> List<T> queryList(String sql, Class<T> clazz, String type, Object... param) {
        Session session = this.sessionFactory.openSession();
        try {
            List<T> result = new ArrayList<>();
            SQLQuery sqlQuery = bindSQLQueryParams(session.createSQLQuery(sql), param);
            switch (type) {
                case "entity":
                    result.addAll(sqlQuery.addEntity(clazz).list());
                    break;
                case "map":
                    result.addAll(sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list());
                    break;
                case "object":
                    result.addAll(sqlQuery.list());
                    break;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(String.format("query [%s] error", sql), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    /******************************************************************************************************
     * 不使用paging类-分页
     */

    public <T> List<T> queryListEntityByPage(String sql, Class<T> clazz, int start, int limit, Object... param) {
        return this.queryListByPage(sql, clazz, start, limit, "entity", param);
    }

    public List<Map<String, Object>> queryListMapByPage(String sql, int start, int limit, Object... param) {
        return this.queryListByPage(sql, null, start, limit, "map", param);
    }

    public <T> List<T> queryListObjectByPage(String sql, int start, int limit, Object... param) {
        return this.queryListByPage(sql, null, start, limit, "object", param);
    }

    private <T> List<T> queryListByPage(String sql, Class<T> clazz, int start, int limit, String type, Object... param) {
        Session session = this.sessionFactory.openSession();
        try {
            List<T> result = new ArrayList<>();
            String limit_sql = String.format(" limit %d , %d", start, limit);

            SQLQuery query = bindSQLQueryParams(session.createSQLQuery(sql + limit_sql), param);

            switch (type) {
                case "entity":
                    result.addAll(query.addEntity(clazz).list());
                    break;
                case "map":
                    result.addAll(query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list());
                    break;
                case "object":
                    result.addAll(query.list());
                    break;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(String.format("query [%s] error", sql), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    /******************************************************************************************************
     * 使用paging类-分页
     */

    public <T> Paging<T> queryListEntityByPage(String sql, Class<T> clazz, Paging paging, Object... param) {
        return this.queryListByPage(sql, clazz, paging, "entity", param);
    }

    public Paging<Map<String, Object>> queryListMapByPage(String sql, Paging paging, Object... param) {
        return this.queryListByPage(sql, null, paging, "map", param);
    }

    public <T> Paging<T> queryListObjectByPage(String sql, Paging paging, Object... param) {
        return this.queryListByPage(sql, null, paging, "object", param);
    }

    private <T> Paging<T> queryListByPage(String sql, Class<T> clazz, Paging paging, String type, Object... param) {
        Session session = this.sessionFactory.openSession();

        try {
            int total = getQuerySize(sql, param);

            List result = new ArrayList<>();

            String limit_sql = String.format(" limit %d , %d", paging.getStart(), paging.getLimit());

            switch (type) {
                case "entity":
                    result.addAll(bindSQLQueryParams(session.createSQLQuery(sql + limit_sql), param).addEntity(clazz).list());
                    break;
                case "map":
                    result.addAll(bindSQLQueryParams(session.createSQLQuery(sql + limit_sql), param).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list());
                    break;
                case "object":
                    result.addAll(bindSQLQueryParams(session.createSQLQuery(sql + limit_sql), param).list());
                    break;
            }

            paging.setTotal(total);
            paging.setRows(result);

            return paging;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(String.format("query [%s] error", sql), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    /******************************************************************************************************
     * 其他
     */

    /**
     * 获取主键名
     */
    private <T> String getPkColunmName(Class<T> clazz) {
        AbstractEntityPersister classMetadata = (AbstractEntityPersister) this.sessionFactory.getClassMetadata(clazz);
        return classMetadata.getIdentifierPropertyName();
    }

    /**
     * 获取实体类型名
     */
    private <T> String getEntityName(Class<T> clazz) {
        AbstractEntityPersister classMetadata = (AbstractEntityPersister) this.sessionFactory.getClassMetadata(clazz);
        return classMetadata.getEntityName();
    }

    /**
     * 获取表名称
     */
    private <T> String getTableName(Class<T> clazz) {
        AbstractEntityPersister classMetadata = (AbstractEntityPersister) this.sessionFactory.getClassMetadata(clazz);
        return classMetadata.getTableName();
    }


    /**
     * 绑定数据到sql
     */
    private SQLQuery bindSQLQueryParams(SQLQuery sqlQuery, Object[] objects) {
        if (Objects.isNull(objects) || objects.length < 1)
            return sqlQuery;

        for (int i = 0; i < objects.length; i++) {
            if (objects[i] instanceof Date) {

                sqlQuery.setDate(i, (Date) objects[i]);

            } else if (objects[i] instanceof Integer) {

                sqlQuery.setInteger(i, (Integer) objects[i]);

            } else if (objects[i] instanceof Float) {

                sqlQuery.setFloat(i, (Float) objects[i]);

            } else if (objects[i] instanceof Double) {

                sqlQuery.setDouble(i, (Double) objects[i]);

            } else if (objects[i] instanceof Long) {

                sqlQuery.setLong(i, (Long) objects[i]);

            } else {
                sqlQuery.setParameter(i, objects[i]);
            }
        }
        return sqlQuery;
    }


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public Session openSession() {
        return this.sessionFactory.openSession();
    }
}

