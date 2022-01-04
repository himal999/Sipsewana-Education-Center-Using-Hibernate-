package bo;/*
author :Himal
version : 0.0.1
*/


import entity.Student;

import model.StuRegister;

import java.io.IOException;
import java.util.List;

public interface StudentBO  {
     boolean saveStudent(Student student) throws IOException;
     boolean deleteStudent(Student student) throws IOException;
     boolean updateStudent(Student student) throws IOException;
     List<Student> allStudent() throws IOException;
     boolean alreadyStudentId(String id) throws IOException;
     boolean insertStudentData(Student student) throws IOException;
     boolean checkNewStudent(String nic) throws IOException;
     boolean updateStatus(Student student) throws IOException;
     boolean insertStuRegisterData(StuRegister register) throws IOException;
}
