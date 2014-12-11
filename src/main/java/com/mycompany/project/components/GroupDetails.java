/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.project.components;
import com.company.project.model.Group;
import com.mycompany.project.MyVaadinUI;
import com.mycompany.project.business.BusinessLogic;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author vikrant.thakur
 */
public class GroupDetails extends Panel {

    private String selectedGroupId;
    private final TextField tfName = new TextField("Name");

    public GroupDetails() {
//        setCaption("Contact Details");

        VerticalLayout mainVLayout = new VerticalLayout();
        mainVLayout.setMargin(true);
        mainVLayout.setSpacing(true);

        setContent(mainVLayout);
        tfName.setSizeFull();
        mainVLayout.addComponent(tfName);

        Button btnUpdate = new Button("Update");
        mainVLayout.addComponent(btnUpdate);

        btnUpdate.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                //invoke business logic
                BusinessLogic bl = ((MyVaadinUI) getUI()).getBusinessLogic();

                String name = tfName.getValue();

                boolean success = bl.updateGroup(selectedGroupId, name);
                if (success) {
                    load(selectedGroupId);
                    Notification.show("Success", "Group updated", Notification.Type.TRAY_NOTIFICATION);
                } else {
                    Notification.show("Error", "\nSomething bad happened", Notification.Type.ERROR_MESSAGE);
                }
            }
        });

        GroupDetails.this.setVisible(false);
    }

    public void setGroupId(String groupId) {
        selectedGroupId = groupId;
        if (groupId == null) {
            GroupDetails.this.setVisible(false);
        } else {
            GroupDetails.this.setVisible(true);
            load(groupId);
        }
    }

    private void load(String groupId) {
        // get hold of the business logic
        BusinessLogic bl = ((MyVaadinUI) getUI()).getBusinessLogic();
        Group group = bl.getGroup(groupId);
        if (group != null) {
            tfName.setValue(group.getName());
        } else {
            GroupDetails.this.setVisible(false);
            Notification.show("Error", "\nGroup could not be loaded", Notification.Type.ERROR_MESSAGE);
        }
    }
}
