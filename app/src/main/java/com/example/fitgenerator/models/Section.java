package com.example.fitgenerator.models;

import java.util.List;

public class Section {

    private String sectionName;
    private List<Fit> sectionItems;

    public Section(String sectionName, List<Fit> sectionItems) {
        this.sectionName = sectionName;
        this.sectionItems = sectionItems;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public List<Fit> getSectionItems() {
        return sectionItems;
    }

    public void setSectionItems(List<Fit> sectionItems) {
        this.sectionItems = sectionItems;
    }

    @Override
    public String toString() {
        return "Section{" +
                "sectionName='" + sectionName + '\'' +
                ", sectionItems=" + sectionItems +
                '}';
    }
}
