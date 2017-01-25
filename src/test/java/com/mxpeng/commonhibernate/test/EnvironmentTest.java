package com.mxpeng.commonhibernate.test;


import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by tian_ on 2016/11/16.
 */
public class EnvironmentTest {

    @Test
    public void testHibernate() {
        ApplicationContext ac = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});

        System.out.println(ac.getBean(SessionFactory.class)!=null);
    }
    @Test
    public void testTransactionManager() {
        ApplicationContext ac = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});

        System.out.println(ac.getBean("transactionManager")!=null);
    }

    @Test
    public void testSpring(){
        ApplicationContext ac = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
        System.out.println(ac!=null);
    }

}