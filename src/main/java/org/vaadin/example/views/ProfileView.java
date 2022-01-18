package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.MainView;
import org.vaadin.example.components.EndSessionComponent;
import org.vaadin.example.layouts.MainLayout;
import org.vaadin.example.services.SessionService;


@Route("/profile")
public class ProfileView extends MainLayout {

    @Autowired
    public ProfileView(SessionService sessionService) {
        SessionService.session = sessionService.getSessionByIp(VaadinSession.getCurrent().getBrowser().getAddress());

        if (SessionService.session == null) {
            setContent(new EndSessionComponent());
        } else {

            VerticalLayout verticalLayout = new VerticalLayout();

            Label info = new Label("Proximamente más opciones");

            Button closeSession = new Button();
            closeSession.setText("Tancar la sessió");
            closeSession.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            closeSession.addClickListener(buttonClickEvent -> {
                sessionService.deleteSessionByIp(VaadinSession.getCurrent().getBrowser().getAddress());
                closeSession.getUI().ifPresent(ui -> {
                    ui.navigate(MainView.class);
                });
            });

            verticalLayout.add(info, closeSession);

            setContent(verticalLayout);

        }
    }
}
