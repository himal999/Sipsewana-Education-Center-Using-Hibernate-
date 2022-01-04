package bo.impl;/*
author :Himal
version : 0.0.1
*/


import bo.StudentBO;
import dao.StudentDAO;
import dao.impl.StudentDAOImpl;
import db.FactoryConfig;
import entity.Student;
import javafx.scene.control.Alert;
import model.StuRegister;
import model.TM.ProgramTM;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;


import java.io.IOException;

import java.util.List;

public class StudentBOImpl implements StudentBO {

    StudentDAO studentDAO = new StudentDAOImpl();


    public boolean saveStudent(Student student) throws IOException {

        return studentDAO.saveStudent(student);
    }

    public boolean deleteStudent(Student student) throws IOException {

        return studentDAO.deleteStudent(student);
    }

    public boolean updateStudent(Student student) throws IOException {
       return studentDAO.updateStudent(student);
    }

    public List<Student> allStudent() throws IOException {
       return studentDAO.allStudent();
    }



    public boolean alreadyStudentId(String id) throws IOException {
        List<Student> students =  studentDAO.allStudent();

        for(Student temp:students){
            if(temp.getNic().equalsIgnoreCase(id)){
                return true;
            }
        }
        return false;
    }

    public boolean insertStudentData(Student student) throws IOException {
        if(studentDAO.saveStudent(student)){
            return  true;
        }
        return false;

    }
    public boolean checkNewStudent(String nic) throws IOException {
        if(alreadyStudentId(nic)){
            new Alert(Alert.AlertType.WARNING,"This nic number already exists").show();
            return false;
        }
        return true;
    }

    public boolean updateStatus(Student student) throws IOException {
        if(studentDAO.updateStudent(student)){

            return true;
        }
        return false;
    }
    public boolean insertStuRegisterData(StuRegister register) throws IOException {
        boolean bool = false;

        for (ProgramTM temp : register.getPrograms()) {
            String sql = "INSERT INTO `student_program` VALUES (?,?)";
            Session session = FactoryConfig.getInstance().getSession();
            Transaction transaction = session.beginTransaction();
            NativeQuery sqlQuery = session.createSQLQuery(sql);
            sqlQuery.setParameter(1, register.getNic());
            sqlQuery.setParameter(2, temp.getId());
            if (sqlQuery.executeUpdate() > 0) {
                bool = true;
                transaction.commit();
                session.close();

            } else {
                bool = false;
                transaction.commit();
                session.close();
                return false;

            }
        }
        return true;
    }
}
