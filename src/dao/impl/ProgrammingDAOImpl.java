package dao.impl;/*
author :Himal
version : 0.0.1
*/


import dao.ProgramDAO;
import db.FactoryConfig;

import entity.Program;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;



import java.io.IOException;
import java.util.List;


public class ProgrammingDAOImpl implements ProgramDAO {

    public boolean saveProgram(Program program) throws IOException {
        Session session = FactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.save(program);
        transaction.commit();
        session.close();
        return true;
    }

    public boolean deleteProgram(Program program) throws IOException {
        Session session = FactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(program);
        transaction.commit();
        session.close();
        return true;
    }

    public boolean updateProgram(Program program) throws IOException {
        Session session = FactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.update(program);
        transaction.commit();
        session.close();
        return true;
    }

    public  List<Program>allProgram() throws IOException {
        Session session = FactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM program";
        Query query = session.createQuery(hql);
        List<Program> resultList = query.getResultList();

        transaction.commit();
        session.close();
        return resultList;
    }



    public boolean alreadyProgramId(String id) throws IOException {
      List<Program> programs =  allProgram();

      for(Program temp:programs){
          if(temp.getId().equalsIgnoreCase(id)){
              return true;
          }
      }
      return false;
    }

    public Program getProgram(String id) throws IOException {
        Program program =null;
        Session session = FactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM program WHERE id=:te";
        List<Program>te = session.createQuery(hql).setParameter("te", id).list();
        transaction.commit();
        session.close();
        for(Program pp:te){
            program = pp;
        }
        return program;
    }
}
