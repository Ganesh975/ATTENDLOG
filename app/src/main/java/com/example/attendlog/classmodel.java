package com.example.attendlog;

public class classmodel {

    String classemail;
    String classcourse;
    String classbranch;
    String classid;
    String classsection;

    public classmodel(String classemail, String classcourse, String classbranch, String classid, String classsection, String classpassword, String studentcount) {
        this.classemail = classemail;
        this.classcourse = classcourse;
        this.classbranch = classbranch;
        this.classid = classid;
        this.classsection = classsection;
        this.classpassword = classpassword;
        this.studentcount = studentcount;
    }

    String classpassword;

    public String getStudentcount() {
        return studentcount;
    }

    public void setStudentcount(String studentcount) {
        this.studentcount = studentcount;
    }

    public classmodel(String studentcount) {
        this.studentcount = studentcount;
    }

    String studentcount;

    public String getClassemail() {
        return classemail;
    }

    public void setClassemail(String classemail) {
        this.classemail = classemail;
    }

    public String getClasscourse() {
        return classcourse;
    }

    public void setClasscourse(String classcourse) {
        this.classcourse = classcourse;
    }

    public String getClassbranch() {
        return classbranch;
    }

    public void setClassbranch(String classbranch) {
        this.classbranch = classbranch;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getClasssection() {
        return classsection;
    }

    public void setClasssection(String classsection) {
        this.classsection = classsection;
    }

    public String getClasspassword() {
        return classpassword;
    }

    public void setClasspassword(String classpassword) {
        this.classpassword = classpassword;
    }

    public classmodel() {
    }

    public classmodel(String classemail, String classcourse, String classbranch, String classid, String classsection, String classpassword) {
        this.classemail = classemail;
        this.classcourse = classcourse;
        this.classbranch = classbranch;
        this.classid = classid;
        this.classsection = classsection;
        this.classpassword = classpassword;
    }
}
