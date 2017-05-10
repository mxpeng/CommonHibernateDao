package com.mxpeng.commonhibernate.dao;

import com.mxpeng.commonhibernate.core.Paging;
import com.mxpeng.commonhibernate.entity.Order;
import com.mxpeng.commonhibernate.entity.User;
import org.hibernate.SessionFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;

/**
 * Created by tian_ on 2016/12/22.
 */
public class BaseDaoTest {

    public static SessionFactory sessionFactory;
    public static BaseDao baseDao;
    @BeforeClass
    public static void setUp() throws Exception{
        ApplicationContext ac = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
        sessionFactory = ac.getBean(SessionFactory.class);
        baseDao = ac.getBean(BaseDao.class);
    }

    @Test
    public void get() throws Exception {
        User user = baseDao.get(User.class, "0446cba4-0f08-4026-822d-958749e66bd4");
        System.out.println(user);
    }

    @Test
    public void persist() throws Exception {
        User user = new User(UUID.randomUUID().toString(), "jason", "123456");
        baseDao.persist(user);

    }

    @Test
    public void update() throws Exception {
        User user = baseDao.update(new User("0446cba4-0f08-4026-822d-958749e66bd4", "jason", "123"));
        System.out.println(user);
    }

    @Test
    public void delete() throws Exception {
        User user = baseDao.delete(new User("0446cba4-0f08-4026-822d-958749e66bd4", "jason", "123"));
        System.out.println(user);
    }

    @Test
    public void deleteById() throws Exception {
        baseDao.deleteById(User.class,"fa003c71-5517-4fa6-97e3-e7d2dc731d07");

    }

    @Test
    public void all() throws Exception {
        List<User> users = baseDao.all(User.class);
        System.out.println(users);
    }

    @Test
    public void total() throws Exception {
        int total = baseDao.total(User.class);
        System.out.println(total);
    }


    @Test
    public void addBentch() throws Exception {
/*        List<User> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add( new User(UUID.randomUUID().toString(), "jason-"+i, "123456") );
        }
        boolean b = baseDao.addBentch(list);
        System.out.println(b);*/

        List<Order> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            list.add( new Order(UUID.randomUUID().toString(), "goods-"+i,new Date().toString(),new Date().toString(),"0446cba4-0f08-4026-822d-958749e66bd4") );
        }
        baseDao.addBentch(list);
    }

    @Test
    public void deleteBentch() throws Exception {
        List<User> list = new ArrayList<>();
        User user1 = new User("01c98f63-1393-4937-8474-837bcd98d938");
        User user2 = new User("13773599-3088-45a6-bfed-da0fe2213b98");
        User user3 = new User("18dec89b-8227-4c67-913d-ae04cae3b969");
        list.add(user1);list.add(user2);list.add(user3);
        boolean b = baseDao.deleteBentch(list);
        System.out.println(b);

    }

    @Test
    public void uniqueResultEntity() throws Exception {
        String sql = "select * From t_user user where user.id=?";
        String id = "app";
        User user = baseDao.uniqueResultEntity(sql, User.class, id);
        System.out.println(user);
    }

    @Test
    public void uniqueResultObject() throws Exception {
        String sql = "select count(*) from t_user user where user.pwd=?";
        Object result = baseDao.uniqueResultObject(sql, "1234560");
        System.out.println(result);
    }

    @Test
    public void executeUpdate() throws Exception {
        String sql = "update t_user user set user.pwd=? where user.id=?";
        boolean b = baseDao.executeUpdate(sql, "pwd-----pwd", "2f7fb358-c332-4ec4-b7e1-8013e6c3a077");
        System.out.println(b);

    }

    @Test
    public void getQuerySize() throws Exception {
        long start = new Date().getTime();
        String sql = "SELECT * FROM t_order t_order ";
        sql = "SELECT *  FROM `resource_report_leads_src_his`";
        int count = baseDao.getQuerySize(sql);
        System.out.println(count);
        System.out.println(new Date().getTime()-start);

    }

    @Test
    public void queryListEntity() throws Exception {
        String sql = "select * from t_user user where user.pwd=?";
        List<User> users = baseDao.queryListEntity(sql, User.class, "1234560");
        System.out.println(users);
    }

    @Test
    public void queryListMap() throws Exception {
        String sql = "SELECT t_user.name, t_order.createed, t_order.goods FROM t_order t_order LEFT JOIN t_user t_user ON t_user.id = t_order.userId";
        List<Map<String, Object>> list = baseDao.queryListMap(sql);
        System.out.println(list);
    }

    @Test
    public void queryListObject() throws Exception {
        String sql = "select user.id from t_user user";
        List<Object> objects = baseDao.queryListObject(sql);
        System.out.println(objects.size());
        System.out.println(objects);
    }

    @Test
    public void queryListEntityByPage() throws Exception {
        String sql = "SELECT t_order.* FROM t_order t_order ";
        List<Order> orders = baseDao.queryListEntityByPage(sql, Order.class, 0, 3);
        System.out.println(orders);

    }

    @Test
    public void queryListMapByPage() throws Exception {
        String sql = "SELECT t_user.name, t_order.createed, t_order.goods FROM t_order t_order LEFT JOIN t_user t_user ON t_user.id = t_order.userId";
        List<Map<String, Object>> list = baseDao.queryListMapByPage(sql, 0, 3);
        System.out.println(list);

    }

    @Test
    public void queryListObjectByPage() throws Exception {
        String sql = "SELECT t_order.id FROM t_order t_order ";
        List<Object> objects = baseDao.queryListObjectByPage(sql, 0, 3);
        System.out.println(objects);

    }


    @Test
    public void queryListEntityByPage1() throws Exception {
        Paging paging = new Paging();
        paging.setStart(0);
        paging.setLimit(3);

        String sql = "SELECT t_order.* FROM t_order t_order ";
        Paging<Order> paging1 = baseDao.queryListEntityByPage(sql, Order.class, paging);
        System.out.println(paging1.getRows());
    }

    @Test
    public void queryListMapByPage1() throws Exception {
        Paging paging = new Paging();
        paging.setStart(0);
        paging.setLimit(3);

        String sql = "SELECT t_user.name, t_order.createed, t_order.goods FROM t_order t_order LEFT JOIN t_user t_user ON t_user.id = t_order.userId";
        Paging<Map<String,Object>> paging1 = baseDao.queryListMapByPage(sql, paging);
        System.out.println(paging1);
    }

    @Test
    public void queryListObjectByPage1() throws Exception {
        Paging paging = new Paging();
        paging.setStart(0);
        paging.setLimit(3);

        String sql = "SELECT t_order.id FROM t_order t_order ";
        Paging<Object> paging1 = baseDao.queryListObjectByPage(sql, paging);
        System.out.println(paging1);
    }


}