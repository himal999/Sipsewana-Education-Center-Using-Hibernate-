package dao;/*
author :Himal
version : 0.0.1
*/


import entity.Student;

import java.io.IOException;
import java.util.List;

public interface StudentDAO {
    public boolean saveStudent(Student student) throws IOException;

    public boolean deleteStudent(Student student) throws IOException;

    public boolean updateStudent(Student student) throws IOException;

    public List<Student> allStudent() throws IOException;

}
