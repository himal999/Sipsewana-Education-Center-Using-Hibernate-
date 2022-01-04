package bo.impl;/*
author :Himal
version : 0.0.1
*/

import bo.ProgramBO;
import dao.ProgramDAO;
import dao.impl.ProgrammingDAOImpl;

import entity.Program;
import javafx.scene.control.Alert;


import java.io.IOException;

import java.util.List;

public class ProgramBOImpl implements ProgramBO {

    ProgramDAO programmingDAO = new ProgrammingDAOImpl();

    public boolean saveProgram(Program program) throws IOException {
        return programmingDAO.saveProgram(program);
    }

    public boolean deleteProgram(Program program) throws IOException {

        return programmingDAO.deleteProgram(program);
    }

    public boolean updateProgram(Program program) throws IOException {
        return programmingDAO.updateProgram(program);
    }

    public List<Program> allProgram() throws IOException {
        return programmingDAO.allProgram();
    }


    public boolean alreadyProgramId(String id) throws IOException {
        List<Program> programs = programmingDAO.allProgram();

        for (Program temp : programs) {
            if (temp.getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public Program getProgram(String id) throws IOException {
        return programmingDAO.getProgram(id);
    }

    public boolean checkProgramId(String id) throws IOException {
        if(programmingDAO.alreadyProgramId(id)){
            new Alert(Alert.AlertType.ERROR,"Sorry,This Program Id Already Exists.").show();
            return false;
        }
        return true;
    }
}
