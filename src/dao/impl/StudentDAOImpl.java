package dao.impl;/*
author :Himal
version : 0.0.1
*/

import dao.StudentDAO;
import db.FactoryConfig;
import entity.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.List;

public class StudentDAOImpl implements StudentDAO {
    public boolean saveStudent(Student student) throws IOException {
        Session session = FactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.save(student);
        transaction.commit();
        session.close();
        return true;
    }

    public boolean deleteStudent(Student student) throws IOException {
        Session session = FactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(student);
        transaction.commit();
        session.close();
        return true;
    }

    public boolean updateStudent(Student student) throws IOException {
        Session session = FactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.update(student);
        transaction.commit();
        session.close();
        return true;
    }

    public List<Student> allStudent() throws IOException {
        Session session = FactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM student";
        Query query = session.createQuery(hql);
        List<Student> resultList = query.list();
        transaction.commit();
        session.close();
        return resultList;
    }






}
