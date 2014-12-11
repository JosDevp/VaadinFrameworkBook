package com.mycompany.project.business;

import com.company.project.model.Contact;
import com.company.project.model.Group;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author vikrant.thakur
 */
public class BusinessLogic {

    private final DataSource dataSource;
    private final Sql sql;
    private static int connCount = 0;

    //constructor
    public BusinessLogic() throws Exception {
        //whenever a new instance is created, get a reference to the connection pool
        // Create a JNDI Initial context to be able to lookup the DataSource
        InitialContext ctx = new InitialContext();
        // Lookup the DataSource, which will be backed by a pool
        //   that the application server provides.
        dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/abookPool");
        dataSource.getConnection();
        if (dataSource == null) {
            throw new Exception("Unknown DataSource 'jdbc/abookPool'");
        } else {
            sql = new Sql();
            System.out.println("BusinessLogic(): Connected to Database.");
        }
    }

    public Connection getConnection() {
        Connection conn = null;
        try {
            if (conn == null || conn.isClosed()) {
                conn = dataSource.getConnection();
                connCount++;
                System.out.println("Connection count: " + connCount);
            }
        } catch (SQLException ex) {

            System.out.println("DB: getConnection(): " + ex.getMessage());
        }
        return conn;
    }

    public boolean closeConnection(Connection conn) {
        boolean success = false;
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                    connCount--;
                    System.out.println("Connection count: " + connCount);
                }
                success = true;
            } catch (SQLException ex) {
                System.out.println("DB: closeConnection(): " + ex.getMessage());
            }
        }

        return success;
    }

    boolean beginTransaction(Connection conn, String callingMethod) {
        boolean success = false;
        try {
            conn.setAutoCommit(false);
            success = true;
            System.out.println("INFO: Transaction started by: " + callingMethod);
        } catch (SQLException ex) {
            System.out.println("ERROR: beginTransaction(): " + ex.getMessage());
        }
        return success;
    }

    boolean commitTransaction(Connection conn, String callingMethod) {
        boolean success = false;
        try {
            conn.commit();
            conn.setAutoCommit(true);
            success = true;
            System.out.println("INFO: Transaction committed by: " + callingMethod);
        } catch (SQLException ex) {
            System.out
                    .println("ERROR: commitTransaction(): " + ex.getMessage());
        }
        return success;
    }

    boolean rollbackTransaction(Connection conn, String callingMethod) {
        boolean success = false;
        try {
            conn.rollback();
            conn.setAutoCommit(true);
            success = true;
            System.out.println("INFO: Transaction rolled back by: " + callingMethod);
        } catch (SQLException ex) {
            System.out.println("ERROR: rollbackTransaction(): "
                    + ex.getMessage());
        }
        return success;
    }

    // Tasks related to contacts
    //1. Add a new contact,
    //3. Modify the details of the selected contact.
    //4. Fetch a list of all contacts

    // Tasks related to groups
    //1. Add a new group,
    //3. Modify the details of the selected group.
    //4. Fetch a list of all groups
    
    
// business logic for generating primary keys

    private String generatePrimaryKey(Connection conn, String tableName) {
        String returnValue = null;
        String existingPkValue = null;
        String nextPkValue = null;

        try {
            existingPkValue = sql.selectKeyValueFromLastUsedKey(conn, tableName);

            if (existingPkValue == null) {
                System.out.println("Last used key value is NULL");
                return null;
            }
            // remove first 1 alphabetic characters from this
            String prefix = existingPkValue.substring(0, 1);
            String existingPkValueWithPrefixStripped = existingPkValue.substring(1);

            // get number of characters in lastUsedId
            int completeLength = existingPkValueWithPrefixStripped.length();

            // convert lastUsedId into number
            int existingIdNum = 0;
            existingIdNum = Integer.parseInt(existingPkValueWithPrefixStripped.trim());

            // add 1 to this number
            int nextIdNum = existingIdNum + 1;

            // convert this number back to String
            String nextId = String.valueOf(nextIdNum);
            int newLength = nextId.length();
            // depending on the number of characters calculated initially,
            int zeroes = completeLength - newLength;
            // prefix Zeros accordingly to this String
            for (int i = 1; i <= zeroes; i++) {
                nextId = "0" + nextId;
            }

            // add the 4 character alphabetic prefix to this string
            nextPkValue = prefix + nextId;

            // update the system_last_used_id table
            sql.updateLastUsedPrimaryKeyValue(conn, tableName, nextPkValue);
            returnValue = nextPkValue;
        } catch (NumberFormatException ex) {
            System.out.println("generatePrimaryKey: " + ex.getMessage());
        }
        return returnValue;

    }

    
    //1. Add a new contact,
    public String saveNewContact(
            String name,
            String phone,String email) {

        String newContactId = null;
        boolean success = false;
        Connection conn = getConnection();
        if (conn != null) {
            success = beginTransaction(conn, "Guardar Contacto");
            if (success) {
                String newId = this.generatePrimaryKey(conn, "_contact");
                if (newId == null || newId.length() == 0) {
                    success = false;
                }

                if (success) {
                    success = sql.insertIntoContact(conn, newId, name, phone,email);
                }
                if (success) {
                    newContactId = newId;
                }
                if (success) {
                    commitTransaction(conn, "saveNewContact");
                } else {
                    rollbackTransaction(conn, "saveNewContact");
                }
            }
            closeConnection(conn);
        }
        return newContactId;
    }

    public boolean updateContact(
            String contactId,
            String name,
            String address,
            String phone,
            String email,
            ArrayList<String> groupIds) {

        boolean success = false;
        Connection conn = getConnection();
        if (conn != null) {
            success = beginTransaction(conn, "updateContact");
            if (success) {

                success = sql.updateContactWhereIdEquals(conn, name, address, phone, email, contactId);

                if (success) {
                    success = sql.deleteFromContactGroupsWhereContactIdEquals(conn, contactId);
                }
                if (success) {
                    for (int i = 0; i < groupIds.size(); i++) {
                        success = sql.insertIntoContactGroups(conn, contactId, groupIds.get(i));
                        if (!success) {
                            break;
                        }
                    }
                }
                if (success) {
                    commitTransaction(conn, "updateContact");
                } else {
                    rollbackTransaction(conn, "updateContact");
                }
            }

            closeConnection(conn);
        }
        return success;
    }

    public Contact getContact(String contactId) {
        Contact contact = null;
        Connection conn = getConnection();
        if (conn != null) {
            contact = sql.selectContact(conn, contactId);

            //which groups does it belong to
            ArrayList<String> groupIds = sql.selectGroupIdsWhereContactIdEquals(conn, contactId);
            contact.setGroupIds(groupIds);
            closeConnection(conn);
        }
        return contact;
    }

    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> contacts = null;
        Connection conn = getConnection();
        if (conn != null) {
            contacts = sql.selectAllContacts(conn);
            closeConnection(conn);
        }
        return contacts;
    }

    // groups
    public String saveNewGroup(
            String name) {

        String newGroupId = null;
        boolean success = false;
        Connection conn = getConnection();
        if (conn != null) {
            success = beginTransaction(conn, "saveNewGroup");
            if (success) {
                String newId = this.generatePrimaryKey(conn, "_group");
                if (newId == null || newId.length() == 0) {
                    success = false;
                }

                if (success) {
                    success = sql.insertIntoGroup(conn, newId, name);
                }
                if (success) {
                    newGroupId = newId;
                }
                if (success) {
                    commitTransaction(conn, "saveNewGroup");
                } else {
                    rollbackTransaction(conn, "saveNewGroup");
                }
            }
            closeConnection(conn);
        }
        return newGroupId;
    }

    public boolean updateGroup(
            String groupId,
            String name) {

        boolean success = false;
        Connection conn = getConnection();
        if (conn != null) {
            success = beginTransaction(conn, "updateGroup");
            if (success) {

                success = sql.updateGroupWhereIdEquals(conn, name, groupId);

                if (success) {
                    commitTransaction(conn, "updateContact");
                } else {
                    rollbackTransaction(conn, "updateContact");
                }
            }

            closeConnection(conn);
        }
        return success;
    }

    public ArrayList<Group> getAllGroups() {
        ArrayList<Group> groups = null;
        Connection conn = getConnection();
        if (conn != null) {
            groups = sql.selectAllGroups(conn);
            closeConnection(conn);
        }
        return groups;
    }

    public Group getGroup(String groupId) {
        Group group = null;
        Connection conn = getConnection();
        if (conn != null) {
            group = sql.selectGroup(conn, groupId);
            closeConnection(conn);
        }
        return group;
    }

}
