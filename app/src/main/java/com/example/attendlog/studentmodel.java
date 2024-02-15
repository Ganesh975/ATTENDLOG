package com.example.attendlog;

public class studentmodel implements Comparable<studentmodel> {
    String stusino, stupino, stuname;
    int stusino_numeric; // New field for numeric representation of stusino

    public String getStusino() {
        return stusino;
    }

    public void setStusino(String stusino) {
        this.stusino = stusino;
        // Set stusino_numeric based on the string value
        this.stusino_numeric = Integer.parseInt(stusino);
    }

    public String getStupino() {
        return stupino;
    }

    public void setStupino(String stupino) {
        this.stupino = stupino;
    }

    public String getStuname() {
        return stuname;
    }

    public void setStuname(String stuname) {
        this.stuname = stuname;
    }

    public int getStusino_numeric() {
        return stusino_numeric;
    }

    public void setStusino_numeric(int stusino_numeric) {
        this.stusino_numeric = stusino_numeric;
    }

    public studentmodel(String stusino, String stupino, String stuname,Integer stusino_numeric) {
        this.stusino = stusino;
        this.stupino = stupino;
        this.stuname = stuname;
        // Set stusino_numeric based on the string value
        this.stusino_numeric = stusino_numeric;
    }

    public studentmodel() {
    }

    @Override
    public int compareTo(studentmodel otherStudent) {
        // Implement the comparison logic based on stusino_numeric
        return Integer.compare(this.stusino_numeric, otherStudent.getStusino_numeric());
    }
}