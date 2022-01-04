package dao;/*
author :Himal
version : 0.0.1
*/

import db.FactoryConfig;
import entity.Program;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.List;

public interface ProgramDAO {
     boolean saveProgram(Program program) throws IOException;
     boolean deleteProgram(Program program) throws IOException;
     boolean updateProgram(Program program) throws IOException;
     List<Program> allProgram() throws IOException;
     boolean alreadyProgramId(String id) throws IOException;
     Program getProgram(String id) throws IOException;
}
