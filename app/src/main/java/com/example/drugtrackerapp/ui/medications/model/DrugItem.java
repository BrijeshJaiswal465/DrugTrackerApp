package com.example.drugtrackerapp.ui.medications.model;

import java.io.Serializable;

public class DrugItem implements Serializable {
    private String name;
    private String synonym;
    private String rxcui;

    public DrugItem(String name, String synonym, String rxcui) {
        this.name = name;
        this.synonym = synonym;
        this.rxcui = rxcui;
    }

    public String getName() {
        return name;
    }

    public String getSynonym() {
        return synonym;
    }

    public String getRxcui() {
        return rxcui;
    }
}
