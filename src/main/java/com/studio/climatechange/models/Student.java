package com.studio.climatechange.models;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "student")
public class Student {
    @Id
    private String sId;

    private String name;

    public String getName() {
        return name;
    }

    public String getSId() {
        return sId;
    }
}
