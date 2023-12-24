package com.example.dbfinalproject;

public class Appointment{
    private int turn;
    private int patId;
    private int docId;
    private String patName;
    private String docName;
    private String specName;
    private int statId;
    public Appointment(int turn, int patId, int docId, String patName, String docName, String specName, int statId) {
        this.turn = turn;
        this.patId = patId;
        this.docId = docId;
        this.patName = patName;
        this.docName = docName;
        this.specName = specName;
        this.statId = statId;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getPatId() {
        return patId;
    }

    public void setPatId(int patId) {
        this.patId = patId;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public String getPatName() {
        return patName;
    }

    public void setPatName(String patName) {
        this.patName = patName;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public int getStatId() {
        return statId;
    }

    public void setStatId(int statId) {
        this.statId = statId;
    }
}
