package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.components.EndSessionComponent;
import org.vaadin.example.layouts.MainLayout;
import org.vaadin.example.models.Volunteer;
import org.vaadin.example.services.SessionService;
import org.vaadin.example.services.VolunteerService;

import java.security.NoSuchAlgorithmException;


@Route("/volunteer/edit")
public class VolunteerFormView extends MainLayout {

    Volunteer volunteer;

    TextField volunteerCodeTextField;
    TextField firstnameTextField;
    TextField firstLastnameTextField;
    TextField secondLastnameTextField;
    PasswordField passwordField;
    PasswordField repeatPasswordField;

    VolunteerService volunteerService;

    @Autowired
    public VolunteerFormView(SessionService sessionService, VolunteerService volunteerService) {
        SessionService.session = sessionService.getSessionByIp(VaadinSession.getCurrent().getBrowser().getAddress());

        if (SessionService.session == null) {
            setContent(new EndSessionComponent());
        } else {
            this.volunteerService = volunteerService;

            volunteer = new Volunteer();

            VerticalLayout verticalLayout = new VerticalLayout();

            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.setWidthFull();

            Button cancelService = new Button();
            cancelService.setText("Cancelar");
            cancelService.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            cancelService.addClickListener(clickEvent -> {
                cancelService.getUI().ifPresent(click -> {
                    click.navigate(VolunteersListView.class);
                });
            });

            Button createService = new Button();
            createService.setText("Guardar");
            createService.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            createService.addClickListener(click -> {
                try {
                    saveVolunteer();
                    createService.getUI().ifPresent(click2 -> {
                        click2.navigate(VolunteersListView.class);
                    });
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

            });

            horizontalLayout.add(cancelService, createService);

            FormLayout formLayout = new FormLayout();

            volunteerCodeTextField = new TextField("Codi de voluntari: ");
            volunteerCodeTextField.addValueChangeListener(textFieldStringComponentValueChangeEvent -> {
                if (textFieldStringComponentValueChangeEvent.isFromClient()) {
                    volunteer.setVolunteerCode(volunteerCodeTextField.getValue());
                }
            });

            firstnameTextField = new TextField("Nom: ");
            firstnameTextField.addValueChangeListener(textFieldStringComponentValueChangeEvent -> {
                if (textFieldStringComponentValueChangeEvent.isFromClient()) {
                    volunteer.setFirstname(firstnameTextField.getValue());
                }
            });

            firstLastnameTextField = new TextField("Primer cognom: ");
            firstLastnameTextField.addValueChangeListener(textFieldStringComponentValueChangeEvent -> {
                if (textFieldStringComponentValueChangeEvent.isFromClient()) {
                    volunteer.setFirstlastname(firstLastnameTextField.getValue());
                }
            });

            secondLastnameTextField = new TextField("Segon cognom: ");
            secondLastnameTextField.addValueChangeListener(textFieldStringComponentValueChangeEvent -> {
                if (textFieldStringComponentValueChangeEvent.isFromClient()) {
                    volunteer.setSecondlastname(secondLastnameTextField.getValue());
                }
            });

            passwordField = new PasswordField("Contrasenya: ");

            repeatPasswordField = new PasswordField("Repetir contraseña: ");

            formLayout.add(volunteerCodeTextField,
                    firstnameTextField,
                    firstLastnameTextField,
                    secondLastnameTextField,
                    passwordField,
                    repeatPasswordField);

            verticalLayout.add(horizontalLayout, formLayout);

            setContent(verticalLayout);
        }
    }

    public void saveVolunteer() throws NoSuchAlgorithmException {
        if (passwordField.getValue().equals(repeatPasswordField.getValue())) {
            volunteer.setUserPassword(passwordField.getValue());
            volunteerService.saveVolunteer(volunteer);
        } else {
            Notification.show("Las contrasenyas no coincideixen.");
        }
    }
}
