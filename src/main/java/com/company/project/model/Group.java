/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.project.model;

import java.io.Serializable;

/**
 *
 * @author vikrant.thakur
 */
public class Group implements Serializable {

    private final String id;
    private String name;
    
    public Group(String id, String name){
        this.id=id;
        this.name=name;
        
    }

    public Group(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
