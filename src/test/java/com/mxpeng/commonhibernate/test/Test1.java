package com.mxpeng.commonhibernate.test;

import com.mxpeng.commonhibernate.entity.User;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.SequenceGenerator;
import java.util.*;

import static javafx.scene.input.KeyCode.T;

/**
 * Created by tian_ on 2016/12/22.
 */
public class Test1 {
    public static SessionFactory sessionFactory;

    @BeforeClass
    public static void befor() {
        ApplicationContext ac = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
        sessionFactory = ac.getBean(SessionFactory.class);
    }

    @Test
    public void test1() {


        AbstractEntityPersister classMetadata = (AbstractEntityPersister) sessionFactory.getClassMetadata(User.class);
        System.out.println(classMetadata.getEntityName());                  //类名称
        System.out.println(classMetadata.getTableName());                   //表名称
        System.out.println(classMetadata.getIdentifierPropertyName());      //主键名称

    }

    @Test
    public void test2() {
        //ClassMetadata meta = sessionFactory.getClassMetadata(getEntityClass());
    }

    @Test
    public void test3() {

    }

    @Test
    public void test4() {
        Class clazz = User.class;

        Session session = this.sessionFactory.openSession();

        List<User> result = new ArrayList<>();
        String sql = "SELECT * FROM t_user";
        SQLQuery sqlQuery = session.createSQLQuery(sql).addEntity(clazz);

        System.out.println(sqlQuery.toString());

        result.addAll(sqlQuery.list());
        System.out.println(result);
    }

    @Test
    public void test5() {
        String sql = "select * from t_user user where user.pwd=?";

        Object[] param = new Object[]{"1234560"};

        Session session = this.sessionFactory.openSession();
        List<User> result = new ArrayList<>();
        SQLQuery q = session.createSQLQuery(sql);

        q = bindSQLQueryParams(q, param);

        System.out.println(q);

        result.addAll(q.addEntity(User.class).list());
        System.out.println(result);

    }

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

}
