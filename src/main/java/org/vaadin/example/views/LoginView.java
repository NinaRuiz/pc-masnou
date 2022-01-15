package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.vaadin.example.services.SessionService;

import java.security.NoSuchAlgorithmException;

@Route("/login")
public class LoginView extends VerticalLayout {

    public LoginView(SessionService sessionService) {
        setAlignItems(Alignment.CENTER);
        TextField userName = new TextField("Usuario: ");

        PasswordField passwordField = new PasswordField();
        passwordField.setLabel("Password");

        Button enter = new Button();
        enter.setText("Entrar");
        enter.addClickListener(clickEvent -> {
            try {
                SessionService.session = sessionService.login(userName.getValue(), passwordField.getValue());
                enter.getUI().ifPresent(ui -> {
                    ui.navigate("/home");
                });
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        });

        add(userName, passwordField, enter);
    }
}
