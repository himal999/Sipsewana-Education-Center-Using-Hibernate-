package bo;/*
author :Himal
version : 0.0.1
*/

import entity.Program;

import java.io.IOException;
import java.util.List;

public interface ProgramBO {
     boolean saveProgram(Program program) throws IOException;
     boolean deleteProgram(Program program) throws IOException;
     boolean updateProgram(Program program) throws IOException;
     List<Program> allProgram() throws IOException;
     boolean alreadyProgramId(String id) throws IOException;
     Program getProgram(String id) throws IOException;
     boolean checkProgramId(String id) throws IOException;
}
