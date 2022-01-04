package model;/*
author :Himal
version : 0.0.1
*/

import model.TM.ProgramTM;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Date;


public class StuRegister {
    String nic;
    ArrayList<ProgramTM> programs;
    String regDate;
    Double amount;

    public StuRegister() {
    }

    public StuRegister(String nic, ArrayList<ProgramTM> programs, String  regDate, Double amount) {
        this.nic = nic;
        this.programs = programs;
        this.regDate = regDate;
        this.amount = amount;
    }


    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public ArrayList<ProgramTM> getPrograms() {
        return programs;
    }

    public void setPrograms(ArrayList<ProgramTM> programs) {
        this.programs = programs;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
