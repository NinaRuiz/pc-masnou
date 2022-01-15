package org.vaadin.example;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.layouts.MainLayout;
import org.vaadin.example.models.Session;
import org.vaadin.example.services.SessionService;
import org.vaadin.example.views.HomeView;
import org.vaadin.example.views.LoginView;

import java.util.Date;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and
 * use @Route annotation to announce it in a URL as a Spring managed
 * bean.
 * Use the @PWA annotation make the application installable on phones,
 * tablets and some desktop browsers.
 * <p>
 * A new instance of this class is created for every new user and every
 * browser tab/window.
 */
@Route
@PWA(name = "Vaadin Application",
        shortName = "Vaadin App",
        description = "This is an example Vaadin application.",
        enableInstallPrompt = false)
@Theme(value = Lumo.class, variant = Lumo.DARK)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {

    @Autowired
    public MainView(SessionService sessionService)  {

        SessionService.session = sessionService.getSessionByIp(VaadinSession.getCurrent().getBrowser().getAddress());

        if (SessionService.session != null) {
            System.out.println(new Date().getTime() > SessionService.session.getExpirationDate().getTime());
            if (new Date().getTime() > SessionService.session.getExpirationDate().getTime()) {
                add(new LoginView(sessionService));
            } else {
                add(new HomeView(sessionService));
            }
        } else {
            add(new LoginView(sessionService));
        }

    }

}
