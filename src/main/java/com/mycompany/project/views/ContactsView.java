/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.project.views;


import com.company.project.model.Contact;
import com.mycompany.project.MyVaadinUI;
import com.mycompany.project.business.BusinessLogic;
import com.mycompany.project.components.ContactDetails;
import com.mycompany.project.components.NewContactForm;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.RowHeaderMode;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author askjos
 */
public class ContactsView extends Panel implements View {

    public static final String NAME = "Contactos"; // used by navigator for switching
    private final NewContactForm newContactForm = new NewContactForm();
    private final Window window = new Window();
    private final BeanContainer<String, Contact> beanContainer = new BeanContainer<String, Contact>(Contact.class);
    private final Table table = new Table();
    private final ContactDetails contactDetails = new ContactDetails();

    public ContactsView() {

        VerticalLayout mainVLayout = new VerticalLayout();
        mainVLayout.setMargin(true);
        mainVLayout.setSpacing(true);

        setContent(mainVLayout);

        // view header
        Label header = new Label("<div align=\"center\" style=\"font-size:12pt;\">Contactos</div>");
        header.setContentMode(ContentMode.HTML);

        mainVLayout.addComponent(header);

        // set window properties
        window.setWidth("400px");
        window.setCaption("Nuevo Contacto");
        window.setModal(true);
        window.setContent(newContactForm);

        // add new cotact button
        Button btnNew = new Button("Agregar Nuevo Contacto");
        mainVLayout.addComponent(btnNew);

        // clicking the button should display the NewContactForm
        btnNew.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (window.getParent() == null) {
                    getUI().addWindow(window);
                }

            }
        });

        //add a horozontal layout - left has a table, right has a ContactDetail component
        HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setSizeFull();
        hLayout.setSpacing(true);

        mainVLayout.addComponent(hLayout);

        //add a table
        
        table.setWidth("600px");
        table.setImmediate(true);
        hLayout.addComponent(table);

        // how does table get its data
        beanContainer.setBeanIdProperty("id");
        table.setContainerDataSource(beanContainer);

        //set columns
        final Object[] NATURAL_COL_ORDER = new Object[]{"name", "phone","email"};
        final String[] COL_HEADERS_ENGLISH = new String[]{"Name", "Phone","Email"};

        table.setSelectable(true);
        table.setColumnCollapsingAllowed(true);
        table.setRowHeaderMode(RowHeaderMode.INDEX);
        table.setVisibleColumns(NATURAL_COL_ORDER);
        table.setColumnHeaders(COL_HEADERS_ENGLISH);

        // selecting a table row should enable/disale the ContactDetails component
        table.addValueChangeListener(new ValueChangeListener(){

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                String contactId = (String)table.getValue();
                contactDetails.setContactId(contactId);
            }
        });
        
        //add a ContactDetails component
        
//        contactDetails.setWidth("500px");
        hLayout.addComponent(contactDetails);

        // let the table fill the entire remaining width
        hLayout.setExpandRatio(contactDetails, 1);
        
        
        
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        load();
    }

    public void load() {
        beanContainer.removeAllItems();

        // get hold of the business logic
        BusinessLogic bl = ((MyVaadinUI) getUI()).getBusinessLogic();
        ArrayList<Contact> contacts = bl.getAllContacts();
      //  List<Contact> contac=bl.getAllContacts();
        for (int i = 0; i < contacts.size(); i++) {
            beanContainer.addAll(contacts);
        }
    }

}
