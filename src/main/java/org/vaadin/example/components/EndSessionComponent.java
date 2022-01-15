package org.vaadin.example.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class EndSessionComponent extends VerticalLayout {

    public EndSessionComponent() {
        add(new Label("Tu session ha caducado, vuelve a iniciar session."));
        Button button = new Button("Iniciar sessión");
        button.addClickListener(clickEvent -> {
            button.getUI().ifPresent(ui -> {
                ui.navigate("/login");
            });
        });
        add(button);
    }
}
