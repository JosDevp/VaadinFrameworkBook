/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.project.components;


import com.company.project.model.Contact;
import com.company.project.model.Group;
import com.mycompany.project.MyVaadinUI;
import com.mycompany.project.business.BusinessLogic;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author vikrant.thakur
 */
public class ContactDetails extends Panel {

    private String selectedContactId;
    private final TextField tfName = new TextField("Nombre");
    private final TextField tfAddress = new TextField("Direccion");
    private final TextField tfPhone = new TextField("Telefono");
    private final TextField tfEmail = new TextField("Email");

    private final HorizontalLayout groupsHLayout = new HorizontalLayout();

    private final HashMap<String, CheckBox> groupsMap = new HashMap<String, CheckBox>();

    public ContactDetails() {
//        setCaption("Contact Details");

        VerticalLayout mainVLayout = new VerticalLayout();
        mainVLayout.setMargin(true);
        mainVLayout.setSpacing(true);

        setContent(mainVLayout);

        tfName.setSizeFull();
        tfAddress.setSizeFull();
        tfPhone.setSizeFull();
        tfEmail.setSizeFull();

        Panel panel = new Panel("Grupos");
        panel.setWidth("100%");
        panel.setHeight("50px");
        panel.setContent(groupsHLayout);

        mainVLayout.addComponent(tfName);
        mainVLayout.addComponent(tfAddress);
        mainVLayout.addComponent(tfPhone);
        mainVLayout.addComponent(tfEmail);
        mainVLayout.addComponent(groupsHLayout);

        Button btnUpdate = new Button("Actualizar");
        mainVLayout.addComponent(btnUpdate);

        btnUpdate.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                //invoke business logic
                BusinessLogic bl = ((MyVaadinUI) getUI()).getBusinessLogic();

                String name = tfName.getValue();
                String address = tfAddress.getValue();
                String phone = tfPhone.getValue();
                String email = tfEmail.getValue();

                ArrayList<String> selectedGroupIds = new ArrayList<String>();
                Set<String> groupIdSet = groupsMap.keySet();
                for (String groupId : groupIdSet) {
                    CheckBox cb = groupsMap.get(groupId);
                    if(cb.getValue()){
                        selectedGroupIds.add(groupId);
                    }
                }
                
                boolean success = bl.updateContact(selectedContactId, name, address, phone, email, selectedGroupIds);
                if(success){
                    load(selectedContactId);
                    Notification.show("Informacion", "Contacto Actualizado", Notification.Type.TRAY_NOTIFICATION);
                }else{
                    Notification.show("Error", "\nSomething bad happened", Notification.Type.ERROR_MESSAGE);
                }
            }
        });

        ContactDetails.this.setVisible(false);
    }

    public void setContactId(String contactId) {
        this.selectedContactId = contactId;
        if (contactId == null) {
            ContactDetails.this.setVisible(false);
        } else {
            ContactDetails.this.setVisible(true);

            load(contactId);
        }
    }

    private void load(String contactId) {
        // get hold of the business logic

        BusinessLogic bl = ((MyVaadinUI) getUI()).getBusinessLogic();
        Contact contact = bl.getContact(contactId);
        if (contact != null) {
            tfName.setValue(contact.getName());
            tfAddress.setValue(contact.getAddress());
            tfPhone.setValue(contact.getPhone());
            tfEmail.setValue(contact.getEmail());

            loadAllGroups();
            //set groups
            for (int i = 0; i < contact.getGroupIds().size(); i++) {
                CheckBox cb = groupsMap.get(contact.getGroupIds().get(i));
                cb.setValue(true);
            }
        } else {
            ContactDetails.this.setVisible(false);
            Notification.show("Error", "\nContact could not be loaded", Notification.Type.ERROR_MESSAGE);
        }
    }

    public void loadAllGroups() {
        groupsHLayout.removeAllComponents();
        BusinessLogic bl = ((MyVaadinUI) getUI()).getBusinessLogic();
        ArrayList<Group> allGroups = bl.getAllGroups();
        for (int i = 0; i < allGroups.size(); i++) {
            Group group = allGroups.get(i);
            CheckBox cb = new CheckBox(group.getName());
            cb.setImmediate(true);
            groupsMap.put(group.getId(), cb);
            groupsHLayout.addComponent(cb);
        }
    }
}
