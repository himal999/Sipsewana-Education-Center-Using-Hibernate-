package model.TM;/*
author :Himal
version : 0.0.1
*/

import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.Date;

public class StudentTM {
    String nic;
    String name;
    String address;
    Date dob;
    String tel;
    String email;
    String status;
    HBox action;

    public StudentTM() {
    }

    public StudentTM(String nic, String name, String address, Date dob, String tel, String email, String status, HBox action) {
        this.nic = nic;
        this.name = name;
        this.address = address;
        this.dob = dob;
        this.tel = tel;
        this.email = email;
        this.status = status;
        this.action = action;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HBox getAction() {
        return action;
    }

    public void setAction(HBox action) {
        this.action = action;
    }
}
