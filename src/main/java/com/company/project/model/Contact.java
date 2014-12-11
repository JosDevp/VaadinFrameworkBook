/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.project.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author aski
 */
public class Contact implements Serializable {

    private final String id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private ArrayList<String> groupIds;
    
    public Contact(String id,String name, String address, String phone, String email){
        this.id=id;
        this.name=name;
        this.address=address;
        this.phone=phone;
        this.email=email;
        
    }
    
    public Contact(String id) {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(ArrayList<String> groupIds) {
        this.groupIds = groupIds;
    }

}
