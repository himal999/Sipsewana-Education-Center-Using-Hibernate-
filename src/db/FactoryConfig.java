package db;/*
author :Himal
version : 0.0.1
*/


import entity.Program;
import entity.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.FileInputStream;

import java.io.IOException;
import java.util.Properties;


public class FactoryConfig {

    private  static  FactoryConfig factoryConfig;
    private SessionFactory sessionFactory;

    private  FactoryConfig() throws IOException {

        Properties properties = new Properties();
        properties.load(new FileInputStream("hibernate.properties"));
        Configuration configuration = new Configuration().addProperties(properties).addAnnotatedClass(Student.class).addAnnotatedClass(Program.class);
        sessionFactory = configuration.buildSessionFactory();

    }

    public static FactoryConfig getInstance() throws IOException {
        return (factoryConfig==null)? factoryConfig = new FactoryConfig():factoryConfig;
    }

    public Session getSession(){
        return sessionFactory.openSession();
    }



}
