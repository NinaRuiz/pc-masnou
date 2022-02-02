package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.MainView;
import org.vaadin.example.components.EndSessionComponent;
import org.vaadin.example.layouts.MainLayout;
import org.vaadin.example.models.Volunteer;
import org.vaadin.example.services.SessionService;
import org.vaadin.example.services.VolunteerService;


@Route("/profile")
public class ProfileView extends MainLayout {

    @Autowired
    public ProfileView(SessionService sessionService, VolunteerService volunteerService) {
        SessionService.session = sessionService.getSessionByIp(VaadinSession.getCurrent().getBrowser().getAddress());

        if (SessionService.session == null) {
            setContent(new EndSessionComponent());
        } else {

            VerticalLayout verticalLayout = new VerticalLayout();

            PasswordField passwordField = new PasswordField();
            passwordField.setLabel("Nueva contraseña");

            PasswordField repeatPassword = new PasswordField();
            passwordField.setLabel("Repita la nueva contraseña");

            Button savePassword = new Button();
            savePassword.setText("Guardar nueva contraseña");
            savePassword.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            savePassword.addClickListener(buttonClickEvent -> {
                if (passwordField.getValue().equals(repeatPassword.getValue())) {
                    Volunteer volunteer = volunteerService.getVolunteerById(SessionService.session.getVolunteerId());
                    volunteer.setUserPassword(passwordField.getValue());
                    volunteerService.savePassword(volunteer);
                    passwordField.setValue("");
                    repeatPassword.setValue("");
                    Notification.show("Se ha guardado la contraseña.");
                } else {
                    Notification.show("Las contraseñas no coinciden.");
                }
            });

            Button closeSession = new Button();
            closeSession.setText("Tancar la sessió");
            closeSession.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            closeSession.addClickListener(buttonClickEvent -> {
                sessionService.deleteSessionByIp(VaadinSession.getCurrent().getBrowser().getAddress());
                closeSession.getUI().ifPresent(ui -> {
                    ui.navigate(MainView.class);
                });
            });

            verticalLayout.add(passwordField, repeatPassword, savePassword, closeSession);

            setContent(verticalLayout);

        }
    }
}
