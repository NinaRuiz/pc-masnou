package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.components.EndSessionComponent;
import org.vaadin.example.layouts.MainLayout;
import org.vaadin.example.models.Service;
import org.vaadin.example.models.Volunteer;
import org.vaadin.example.services.SessionService;
import org.vaadin.example.services.VolunteerService;

import java.util.ArrayList;

@Route(value = "/volunteer")
@PageTitle("Voluntaris")
public class VolunteersListView extends MainLayout {

    @Autowired
    public VolunteersListView(VolunteerService volunteerService, SessionService sessionService) {

        SessionService.session = sessionService.getSessionByIp(VaadinSession.getCurrent().getBrowser().getAddress());

        if (SessionService.session == null) {
            setContent(new EndSessionComponent());
        } else {
            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.setWidthFull();

            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.setWidthFull();

            Button createService = new Button();
            createService.setText("Nou voluntari");
            createService.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            createService.addClickListener(buttonClickEvent -> {
                createService.getUI().ifPresent(ui -> {
                    ui.navigate("/volunteer/edit/new");
                });
            });

            horizontalLayout.add(createService);

            Grid<Volunteer> grid = new Grid<>(Volunteer.class, false);
            grid.addColumn(Volunteer::getVolunteerCode).setHeader("Codi de voluntari");
            grid.addItemClickListener(serviceItemClickEvent -> {
                grid.getUI().ifPresent(ui -> {
                    ui.navigate("/volunteer/edit/" + serviceItemClickEvent.getItem().getId());
                });
            });

            grid.setItems(volunteerService.getVolunteers());
            grid.setHeightFull();

            verticalLayout.add(horizontalLayout, grid);
            verticalLayout.setHeightFull();

            setContent(verticalLayout);
        }
    }

}
