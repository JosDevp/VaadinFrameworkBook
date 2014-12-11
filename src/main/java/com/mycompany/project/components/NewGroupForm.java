/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.project.components;


import com.mycompany.project.MyVaadinUI;
import com.mycompany.project.business.BusinessLogic;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author vikrant.thakur
 */
public class NewGroupForm extends Panel {

    private final TextField tfName = new TextField("Name");

    public NewGroupForm() {

        VerticalLayout mainVLayout = new VerticalLayout();
        mainVLayout.setMargin(true);
        mainVLayout.setSpacing(true);

        setContent(mainVLayout);

        // field properties
        tfName.setSizeFull();

        mainVLayout.addComponent(tfName);

        Button btnSave = new Button("Save");
        mainVLayout.addComponent(btnSave);

        btnSave.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                // get hold of the business logic

                String name = tfName.getValue();

                BusinessLogic bl = ((MyVaadinUI) getUI()).getBusinessLogic();
                String newGroupId = bl.saveNewGroup(name);

                if (newGroupId != null) {
                    //saved successfully
                    Notification.show("Success", name + " saved", Notification.Type.TRAY_NOTIFICATION);
                    resetFields();
                } else {
                    //something bad happened
                    Notification.show("Error", "\nGroup could not be saved", Notification.Type.ERROR_MESSAGE);
                }
            }
        });
    }

    private void resetFields() {
        tfName.setValue("");
    }

}
