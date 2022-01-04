package dao.impl;/*
author :Himal
version : 0.0.1
*/

import db.FactoryConfig;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewDetailDAOImpl {
    public  boolean deleteRegisterDetail(String id) throws IOException {
        String sql = "DELETE FROM `stu course register` WHERE c_id = ?";
        Session session = FactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        NativeQuery nativeQuery = session.createSQLQuery(sql).setParameter(1, id);
        if(nativeQuery.executeUpdate()>0){
            transaction.commit();
            session.close();
            return true;
        }
        transaction.commit();
        session.close();
       return false;
    }

    public void getViewData(String id) throws IOException {
        List<String> st = new ArrayList<>();
        Session session = FactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        String sql = "SELECT * FROM `student_program` WHERE students_nic=?";
        NativeQuery sqlQuery = session.createSQLQuery(sql);
        st = sqlQuery.setParameter(1, id).list();



        for(String t:st){
            System.out.println(t);
        }
       /* for(String te:list){
            st.add(te);
        }
        transaction.commit();
        session.close();
        return st;*/

    }
}
