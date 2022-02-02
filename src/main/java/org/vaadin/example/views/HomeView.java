package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
            VerticalLayout vl = new VerticalLayout();
            Label title = new Label("(05-02-2022) Novedades de la aplicación: ");
            title.getStyle().set("font-weight", "bold");
            title.getStyle().set("font-size", "36px");
            vl.add(title);

            vl.add(new Label("- Puedes cambiar tu contraseña desde tu perfil."));
            vl.add(new Label("- Al escribir las novedades de un servicio, se permite escribir más de 750 carácteres. \n" +
                    "Cuando esto ocurra la aplicación dividirá las novedades en varios párrafos."));
            vl.add(new Label("- Si todas las novedades son escritas por la misma persona no se repite el nombre y la" +
                    " abreviación."));
            vl.add(new Label("- En la abreviación del voluntario en las novedades ahora aparecerá el numero de voluntario."));
            vl.add(new Label("- Las novedades en la aplicación aparecerán en la pantalla Home"));
            setContent(vl);
        }

    }
}
