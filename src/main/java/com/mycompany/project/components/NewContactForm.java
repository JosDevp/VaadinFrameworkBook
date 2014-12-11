/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.project.components;


import com.mycompany.project.MyVaadinUI;
import com.mycompany.project.business.BusinessLogic;
import com.mycompany.project.views.boostrap;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author vikrant.thakur
 */
public class NewContactForm extends Panel{
    
    private final TextField tfName = new TextField("Nombre");
    private final TextField tfPhone = new TextField("Telefono");
    private final TextField tfemail = new TextField("Email");
    
    public NewContactForm(){
        
        VerticalLayout mainVLayout = new VerticalLayout();
        mainVLayout.setMargin(true);
        mainVLayout.setSpacing(true);
        mainVLayout.addStyleName(boostrap.Forms.FORM.styleName());
        setContent(mainVLayout);
        
        // field properties
        tfName.setSizeFull();
        tfPhone.setSizeFull();
        tfemail.setSizeFull();

        tfName.focus();
        
        mainVLayout.addComponent(tfName);
        mainVLayout.addComponent(tfPhone);
        mainVLayout.addComponent(tfemail);
        
        Button btnSave = new Button("Guardar");
        mainVLayout.addComponent(btnSave);
        
        btnSave.addClickListener(new ClickListener(){

            @Override
            public void buttonClick(Button.ClickEvent event) {
                // get hold of the business logic
                
                String name = tfName.getValue();
                String phone = tfPhone.getValue();
                String email=tfemail.getValue();
                
                BusinessLogic bl = ((MyVaadinUI)getUI()).getBusinessLogic();
                String newContactId = bl.saveNewContact(name, phone,email);
                
                if(newContactId!=null){
                    //saved successfully
                    Notification.show("Success", name +" saved", Notification.Type.TRAY_NOTIFICATION);
                    //reset fields
                    resetFields();
                }else{
                    //something bad happened
                    Notification.show("Error", "\nContact could not be saved", Notification.Type.ERROR_MESSAGE);
                }
            }
        });
    }
    
    private void resetFields(){
        tfName.setValue("");
        tfPhone.setValue("");
        tfemail.setValue("");
    }
}
