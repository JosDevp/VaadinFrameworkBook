/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.project.business;


import com.company.project.model.Contact;
import com.company.project.model.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vikrant.thakur
 */
public class Sql {

    // last_used_id related queries
    String selectKeyValueFromLastUsedKey(Connection conn, String tableName) {

        String keyValue = null;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("select key_value from _last_used_key where table_name = ?  ");

            stmt.setString(1, tableName);
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                keyValue = rs.getString("key_value");
            }

            rs.close();
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("selectKeyValueFromLastUsedKey(): " + ex.getMessage());
        }

        return keyValue;
    }

    public boolean updateLastUsedPrimaryKeyValue(Connection conn, String tableName, String keyValue) {

        boolean success = false;
        try {
            PreparedStatement stmt = conn
                    .prepareStatement("update _last_used_key set key_value = ? where table_name = ? ");

            stmt.setString(1, keyValue);
            stmt.setString(2, tableName);

            stmt.executeUpdate();
            success = true;
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("updateLastUsedPrimaryKeyValue: " + ex.getMessage());
        }
        return success;
    }

    // contacts related sl queries
    boolean insertIntoContact(Connection conn, String id, String name, String phone,String email) {
        boolean success = false;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(" insert into _contact "
                    + " (id,name, phone,email) "
                    + " values(?,?,?,?) ");

            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, phone);
            stmt.setString(4, email);

            stmt.executeUpdate();
            success = true;
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("insertIntoContact(): " + ex.getMessage());
        }
        return success;
    }

    boolean updateContactWhereIdEquals(
            Connection conn,
            String name,
            String address,
            String phone,
            String email,
            String contactId) {

        boolean success = false;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(" update _contact "
                    + " set name = ?, address = ?, phone = ?, email = ? "
                    + " where id = ? ");

            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.setString(3, phone);
            stmt.setString(4, email);
            stmt.setString(5, contactId);

            stmt.executeUpdate();
            success = true;
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("updateContactWhereIdEquals(): " + ex.getMessage());
        }
        return success;
    }

    ArrayList<Contact> selectAllContacts(Connection conn) {

        ArrayList<Contact> contacts = new ArrayList<Contact>();

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(" select c.* from _contact c");

            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                Contact contact = new Contact(rs.getString("c.id"));
                contacts.add(contact);

                contact.setName(rs.getString("c.name"));
                contact.setPhone(rs.getString("c.phone"));
                contact.setEmail(rs.getString("c.email"));
            }

            rs.close();
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("selectAllContacts(): " + ex.getMessage());
        }

        return contacts;
    }

    Contact selectContact(Connection conn, String id) {

        Contact contact = null;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(" select c.* from _contact c where id = ? ");

            stmt.setString(1, id);
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                contact = new Contact(rs.getString("c.id"));

                contact.setName(rs.getString("c.name"));
                contact.setAddress(rs.getString("c.address"));
                contact.setPhone(rs.getString("c.phone"));
                contact.setEmail(rs.getString("c.email"));

            }

            rs.close();
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("selectContact(): " + ex.getMessage());
        }

        return contact;
    }

    // group
    boolean insertIntoGroup(Connection conn, String id, String name) {
        boolean success = false;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(" insert into _group "
                    + " (id,name) "
                    + " values(?,?) ");

            stmt.setString(1, id);
            stmt.setString(2, name);

            stmt.executeUpdate();
            success = true;
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("insertIntoGroup(): " + ex.getMessage());
        }
        return success;
    }

    boolean updateGroupWhereIdEquals(
            Connection conn,
            String name,
            String groupId) {

        boolean success = false;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(" update _group "
                    + " set name = ? "
                    + " where id = ? ");

            stmt.setString(1, name);
            stmt.setString(2, groupId);

            stmt.executeUpdate();
            success = true;
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("updateGroupWhereIdEquals(): " + ex.getMessage());
        }
        return success;
    }

    ArrayList<Group> selectAllGroups(Connection conn) {

        ArrayList<Group> groups = new ArrayList<Group>();

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(" select g.* from _group g");

            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                Group group = new Group(rs.getString("g.id"));
                groups.add(group);

                group.setName(rs.getString("g.name"));

            }

            rs.close();
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("selectAllGroups(): " + ex.getMessage());
        }

        return groups;
    }

    // contact-group
    ArrayList<String> selectGroupIdsWhereContactIdEquals(Connection conn, String contactId) {

        ArrayList<String> groupIds = new ArrayList<String>();

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(" select cg.group_id from _contact_group cg where cg.contact_id = ? ");

            stmt.setString(1, contactId);
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                groupIds.add(rs.getString("cg.group_id"));
            }

            rs.close();
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("selectGroupIdsWhereContactIdEquals(): " + ex.getMessage());
        }

        return groupIds;
    }

    boolean deleteFromContactGroupsWhereContactIdEquals(Connection conn, String contactId) {

        boolean success = false;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(" delete from _contact_group where contact_id = ? ");

            stmt.setString(1, contactId);
            stmt.executeUpdate();
            success = true;
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("deleteFromContactGroupsWhereContactIdEquals(): " + ex.getMessage());
        }

        return success;
    }

    boolean insertIntoContactGroups(Connection conn, String contactId, String groupId) {

        boolean success = false;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(" insert into _contact_group (contact_id, group_id) values(?,?) ");

            stmt.setString(1, contactId);
            stmt.setString(2, groupId);
            stmt.executeUpdate();

            stmt.close();
            success = true;
        } catch (SQLException ex) {
            Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("insertIntoContactGroups(): " + ex.getMessage());
        }

        return success;
    }

    Group selectGroup(Connection conn, String id) {

        Group group = null;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(" select g.* from _group g where g.id = ? ");

            stmt.setString(1, id);
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                group = new Group(rs.getString("g.id"));
                group.setName(rs.getString("g.name"));
            }

            rs.close();
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("selectGroup(): " + ex.getMessage());
        }
        return group;
    }

}
