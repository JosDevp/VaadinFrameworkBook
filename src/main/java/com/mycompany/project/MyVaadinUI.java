package com.mycompany.project;



import com.mycompany.project.business.BusinessLogic;
import com.mycompany.project.views.ContactsView;
import com.mycompany.project.views.GroupsView;
import com.mycompany.project.views.boostrap;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import javax.servlet.annotation.WebServlet;

@Theme("mytheme")
@SuppressWarnings("serial")
@PreserveOnRefresh
public class MyVaadinUI extends UI
{
    private transient BusinessLogic businessLogic;
    private VerticalLayout contentVLayout;//acts the place holder for the various views
    private ContactsView contactsView;
    private GroupsView groupsView;
    private Navigator navigator;

    private final Button btnContactsView = new Button("Contactos");
    private final Button btnGroupsView = new Button("Grupos");

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class, widgetset = "com.mycompany.project.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }
    
    
      

    @Override
    protected void init(VaadinRequest request) {

        //connect to business logic
        connectToBusinessLogic();

        //create the UI
        createApplicationUI();

        //create various views, and add the default view to the content
        contactsView = new ContactsView();
        groupsView = new GroupsView();

        contactsView.setSizeFull();
        groupsView.setSizeFull();

        navigator = new Navigator(this, contentVLayout);
        navigator.setErrorView(contactsView);
        navigator.addView("", contactsView);
        navigator.addView(ContactsView.NAME, contactsView);
        navigator.addView(GroupsView.NAME, groupsView);
        
        navigator.navigateTo("");
        
        btnContactsView.addClickListener(new ClickListener(){

            @Override
            public void buttonClick(Button.ClickEvent event) {
                navigator.navigateTo(ContactsView.NAME);
            }
        });

        btnGroupsView.addClickListener(new ClickListener(){

            @Override
            public void buttonClick(Button.ClickEvent event) {
                navigator.navigateTo(GroupsView.NAME);
            }
        });

    }

    private void connectToBusinessLogic() {
        if (businessLogic == null) {
            try {
                businessLogic = new BusinessLogic();
            } catch (Exception ex) {
                Notification.show("Could not connect to BusinessLogic", "\n"+ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        }

    }

    public BusinessLogic getBusinessLogic() {
        connectToBusinessLogic();
        return businessLogic;
    }

    private void createApplicationUI() {
        VerticalLayout appVLayout = new VerticalLayout();
        appVLayout.setSizeFull();
        setContent(appVLayout);

        Label header = new Label("<div align=\"center\" style=\"font-size:14pt;\">Registro de Contactos</div>");
        header.setContentMode(ContentMode.HTML);
        header.addStyleName(boostrap.Typography.BODYCOPY.styleName());//boostrap
        header.setWidth("100%");

        Label footer = new Label("<div align=\"center\" style=\"font-size:10pt;\">Vaadin.com</div>");
        footer.setContentMode(ContentMode.HTML);
        footer.setWidth("100%");

        contentVLayout = new VerticalLayout();
        contentVLayout.setMargin(true);
        contentVLayout.setSpacing(true);

        // buttons for switching the views
        HorizontalLayout buttonsHLayout = new HorizontalLayout();
        buttonsHLayout.setMargin(true);
        buttonsHLayout.setSpacing(true);
        buttonsHLayout.setHeight("30px");
        
        buttonsHLayout.addComponent(btnContactsView);
        buttonsHLayout.addComponent(btnGroupsView);
        
        //add header, content, footer
        appVLayout.addComponent(header);
        appVLayout.addComponent(buttonsHLayout);
        appVLayout.addComponent(contentVLayout);
        appVLayout.addComponent(footer);

        //let the content occupy the entire screen area
        appVLayout.setExpandRatio(contentVLayout, 1);

        //align header and footer to the center
    }



}
        
    
   


