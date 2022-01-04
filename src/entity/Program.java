package entity;/*
author :Himal
version : 0.0.1
*/

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "program")
public class Program {
        @Id
        String id;

        String name;

        String duration;

        double fee;
        @ManyToMany(mappedBy = "programs")

        @Cascade({CascadeType.SAVE_UPDATE,CascadeType.DELETE})
        List<Student> students  = new ArrayList<>();


    public Program() {

    }

    public Program(String id, String name, String duration, double fee, List<Student> students) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.fee = fee;
        this.students = students;
    }
    public Program(String id, String name, String duration, double fee) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.fee = fee;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
