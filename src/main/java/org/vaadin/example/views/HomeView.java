package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.components.EndSessionComponent;
import org.vaadin.example.layouts.MainLayout;
import org.vaadin.example.services.SessionService;

@Route("/home")
public class HomeView extends MainLayout {

    @Autowired
    public HomeView(SessionService sessionService) {

        SessionService.session = sessionService.getSessionByIp(VaadinSession.getCurrent().getBrowser().getAddress());

        if (SessionService.session == null) {
            setContent(new EndSessionComponent());
        } else {

            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.setWidthFull();

            Button createService = new Button();
            createService.setText("Noticias");
            createService.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

            horizontalLayout.add(createService);

            setContent(horizontalLayout);
        }

    }
}
