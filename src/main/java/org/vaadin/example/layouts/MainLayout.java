package org.vaadin.example.layouts;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.RouterLink;
import org.vaadin.example.MainView;
import org.vaadin.example.views.HomeView;
import org.vaadin.example.views.ServicesListView;
import org.vaadin.example.views.VolunteersListView;

public class MainLayout extends AppLayout {

    public MainLayout() {

        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("Protecció Civil");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-1)")
                .set("margin", "0");

        Tabs tabs = new Tabs();

        tabs.add(createTab("Home", HomeView.class));
        tabs.add(createTab("Serveis", ServicesListView.class));
        tabs.add(createTab("Voluntaris", VolunteersListView.class));
        tabs.add(createTab("Noticies", HomeView.class));
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);

        addToDrawer(tabs);
        addToNavbar(toggle, title);
    }

    private static Tab createTab(String title, Class<? extends Component> viewClass) {
        return createTab(populateLink(new RouterLink(null, viewClass), title));
    }

    private static Tab createTab(Component content) {
        final Tab tab = new Tab();
        tab.add(content);
        return tab;
    }

    private static <T extends HasComponents> T populateLink(T a, String title) {
        a.add(title);
        return a;
    }

}
