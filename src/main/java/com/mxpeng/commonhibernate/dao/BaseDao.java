package com.mxpeng.commonhibernate.dao;

import com.mxpeng.commonhibernate.core.Paging;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;


@Repository
@Transactional
public class BaseDao  {

	@Resource  
    private SessionFactory sessionFactory;

	/********************************************************************************
	 * 单表常用操作
	 */
	public <T> T  get( Class<T> clazz,Serializable id){
		Session session = sessionFactory.getCurrentSession();
		return session.get(clazz, Objects.requireNonNull(id));
	}
	public <T> T persist(T entity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(entity);
		return entity;
	}

	public <T> T update(T entity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(entity);
		return entity;
	}
	public <T> T delete(T entity){
		Session session = sessionFactory.getCurrentSession();
		session.delete(Objects.requireNonNull(entity));
		return entity;
	}

	public <T> boolean deleteById(Class<T> clazz,Serializable id){
		String sql = "DELETE FROM " + getTableName(clazz) + " WHERE "+getPkColunmName(clazz)+"=?";
		return executeUpdate(sql,id);
	}

	public <T> List<T> queryAll(Class<T> clazz) {
		List<T> result = new ArrayList<>();
		String sql = "SELECT * FROM " + getTableName(clazz);
		result.addAll(sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity(clazz).list());
		return result;
	}

	public <T> int getTotal(Class<T> clazz){
		String sql = "select count(*) from " + getTableName(clazz);

		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);

		return Integer.parseInt(query.uniqueResult().toString());
	}

	/**
	 * 查询clazz类的唯一对象返回
	 * @return 实体对象
	 */
	public <T> T uniqueResult(String sql, Class<T> clazz, Object ... param) {
		List<T> result = this.queryList(sql,clazz,param);
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public <T> boolean addBentch(List<T> entitys) {
		Session session = sessionFactory.getCurrentSession();

		for (int i = 0; i < entitys.size(); i++) {
			session.saveOrUpdate(Objects.requireNonNull(entitys.get(i)));
			if( i%20 == 0){
				session.flush();
				session.clear();
			}
		}
		return true;
	}

	public <T> boolean deleteBentch(List<T> entitys) {
		Session session = sessionFactory.getCurrentSession();
		for (int i = 0; i < entitys.size(); i++) {
			session.delete(Objects.requireNonNull(entitys.get(i)));
			if( i%20 == 0){
				session.flush();
				session.clear();
			}
		}
		return true;
	}

	/********************************************************************************
	 * 单表多表列表查询操作
	 */
	public <T> List<T> queryList(String sql,Class<T> clazz, Object ... param) {
		Session session = sessionFactory.getCurrentSession();
		List<T> result = new ArrayList<>();
		SQLQuery q = session.createSQLQuery(sql);
		result.addAll(bindSQLQueryParams(q,param).addEntity(clazz).list());
		return result;
	}

	public List<Map<String, Object>> queryListMap(String sql, Object ... param){
		Session session = sessionFactory.getCurrentSession();
		List<Map<String, Object>> result = new ArrayList<>();
		result.addAll(bindSQLQueryParams(session.createSQLQuery(sql), param).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list());
		return result;
	}


	/******************************************************************************************************
	 * 单表多表其他操作
	 */
	public Object uniqueResult(String sql,Object ... param) {
		Session session = sessionFactory.getCurrentSession();
		Object result = bindSQLQueryParams(session.createSQLQuery(sql),param).uniqueResult();
		return result;
	}

	public boolean executeUpdate(String sql, Object ... param){
		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = bindSQLQueryParams(session.createSQLQuery(sql),param);
		return query.executeUpdate() >= 0;
	}

	/**
	 * 查询某一项的列表，比如全部id的列表
	 */
	public List<Object> queryListObject(String sql,Object ... param){
		Session session = sessionFactory.getCurrentSession();
		List<Object> result = new ArrayList<>();
		result.addAll(bindSQLQueryParams( session.createSQLQuery(sql),param).list());
		return result;
	}


	/******************************************************************************************************
	 * 不使用paging类-分页
	 */
	public <T> List<T> queryListEntityByPage(String sql,Class<T> clazz, int start, int limit, Object ... param) {
		Session session = sessionFactory.getCurrentSession();
		List<T> result = new ArrayList<>();

		String limit_sql = String.format(" limit %d , %d", start, limit );
		SQLQuery query = bindSQLQueryParams(session.createSQLQuery(sql + limit_sql ),param);

		result.addAll(query.addEntity(clazz).list());
		return result;
	}

	public List<Map<String, Object>> queryListMapByPage(String sql, int start, int limit, Object ... param) {
		Session session = sessionFactory.getCurrentSession();
		List<Map<String, Object>> result = new ArrayList<>();
		String limit_sql = String.format(" limit %d , %d", start, limit );

		result.addAll(bindSQLQueryParams(session.createSQLQuery(sql + limit_sql), param).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list());
		return result;
	}

	public int getQueryTotal(String sql, Object ... param) {
		Session session = sessionFactory.getCurrentSession();

		SQLQuery q = bindSQLQueryParams(session.createSQLQuery(sql), param);

		return Integer.parseInt(q.uniqueResult().toString());
	}

	/******************************************************************************************************
	 * 使用paging类-分页
	 */
	public <T> Paging<T> queryListEntityByPage(String sql, Class<T> clazz, Paging paging, Object ... param) {
		Session session = sessionFactory.getCurrentSession();

		int total = bindSQLQueryParams(session.createSQLQuery(sql),param ).addEntity(clazz).list().size();

		List<T> result = new ArrayList<>();

		String limit_sql = String.format(" limit %d , %d", paging.getStart(), paging.getLimit() );

		result.addAll(bindSQLQueryParams(session.createSQLQuery(sql + limit_sql ),param).addEntity(clazz).list());

		paging.setTotal(total);
		paging.setRows(result);

		return paging;

	}
	public <T> Paging<T> queryListMapByPage(String sql, Paging paging, Object ... param) {
		Session session = sessionFactory.getCurrentSession();

		int total = bindSQLQueryParams(session.createSQLQuery(sql), param).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list().size();
		System.err.println(bindSQLQueryParams(session.createSQLQuery(sql), param).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list());
		List<Map<String, Object>> result = new ArrayList<>();

		String limit_sql = String.format(" limit %d , %d", paging.getStart(), paging.getLimit() );

		result.addAll(bindSQLQueryParams(session.createSQLQuery(sql + limit_sql), param).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list());

		paging.setTotal(total);
		paging.setRows(result);

		return paging;
	}


	/******************************************************************************************************
	 * 其他
	 */
	
	/**
	 * 获取主键名
	 */
	private <T> String getPkColunmName(Class<T> clazz){
		AbstractEntityPersister classMetadata  = (AbstractEntityPersister) sessionFactory.getClassMetadata(clazz);
		return classMetadata.getIdentifierPropertyName();
	}
	
	/**
	 * 获取实体类型名
	 */
	private <T> String getEntityName(Class<T> clazz) {
		AbstractEntityPersister classMetadata  = (AbstractEntityPersister) sessionFactory.getClassMetadata(clazz);
		return classMetadata.getEntityName();
	}

	/**
	 * 获取表名称
	 */
	private <T> String getTableName(Class<T> clazz){
		AbstractEntityPersister classMetadata  = (AbstractEntityPersister) sessionFactory.getClassMetadata(clazz);
		return classMetadata.getTableName();
	}


	/**
	 * 绑定数据到sql
	 */
	private SQLQuery bindSQLQueryParams(SQLQuery sqlQuery, Object [] objects){
		if(Objects.isNull(objects) || objects.length < 1)
			return sqlQuery;

		for(int i=0; i< objects.length; i++ ){
			if(objects[i] instanceof Date){

				sqlQuery.setDate(i, (Date)objects[i]);

			}else if(objects[i] instanceof Integer){

				sqlQuery.setInteger(i, (Integer)objects[i]);

			}else if(objects[i] instanceof Float){

				sqlQuery.setFloat(i, (Float)objects[i]);

			}else if(objects[i] instanceof Double){

				sqlQuery.setDouble(i, (Double)objects[i]);

			}else if(objects[i] instanceof Long){

				sqlQuery.setLong(i, (Long)objects[i]);

			}else{
				sqlQuery.setParameter(i, objects[i]);
			}
		}
		return sqlQuery;
	}


}
