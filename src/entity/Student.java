package entity;
/*
author :Himal
version : 0.0.1
*/

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "student")
public class Student {
        @Id

        String nic;

        String name;

        String address;

        Date dob;

        String tel;

        String email;

        String status;
        @ManyToMany
        @Cascade({CascadeType.SAVE_UPDATE,CascadeType.DELETE})
        List<Program> programs = new ArrayList<>();

        public Student() {
        }

    public Student(String nic, String name, String address, Date dob, String tel, String email, String status, List<Program> programs) {
        this.nic = nic;
        this.name = name;
        this.address = address;
        this.dob = dob;
        this.tel = tel;
        this.email = email;
        this.status = status;
        this.programs = programs;
    }

    public Student(String nic, String name, String address, Date dob, String tel, String email, String status) {
        this.nic = nic;
        this.name = name;
        this.address = address;
        this.dob = dob;
        this.tel = tel;
        this.email = email;
        this.status = status;

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

    public List<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }
}
