package com.personal.website.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name = "personal_view")
public class PersonalModel {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Personal_id;

    public int getPersonal_id() {
        return Personal_id;
    }

    public void setPersonal_id(int personal_id) {
        this.Personal_id = personal_id;
    }

    @Column(name = "personal_imageurl")
    private String personalImageURL;

    public String getPersonalImageURL() {
        return personalImageURL;
    }

    public void setPersonalImageURL(String personalImageURL) {
        this.personalImageURL = personalImageURL;
    }
}
