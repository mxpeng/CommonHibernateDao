package com.mxpeng.commonhibernate.test;

import com.mxpeng.commonhibernate.entity.User;
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
import java.util.Map;

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


        AbstractEntityPersister classMetadata  = (AbstractEntityPersister) sessionFactory.getClassMetadata(User.class);
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
}
