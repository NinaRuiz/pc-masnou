package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.components.EndSessionComponent;
import org.vaadin.example.layouts.MainLayout;
import org.vaadin.example.models.Service;
import org.vaadin.example.services.ServicesService;
import org.vaadin.example.services.SessionService;

import java.io.*;
import java.util.List;

@Route(value = "/services")
@PageTitle("Serveis")
public class ServicesListView extends MainLayout {

    List<Service> services;
    HorizontalLayout anchorLayout;

    @Autowired
    public ServicesListView(SessionService sessionService, ServicesService servicesService) {
        SessionService.session = sessionService.getSessionByIp(VaadinSession.getCurrent().getBrowser().getAddress());
        if (SessionService.session == null) {
            setContent(new EndSessionComponent());
        } else {
            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.setWidthFull();

            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.setWidthFull();

            Button createService = new Button();
            createService.setText("Nou servei");
            createService.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            createService.addClickListener(buttonClickEvent -> {
                createService.getUI().ifPresent(click -> {
                    click.navigate(ServicesFormView.class, "new");
                });
            });

            Button exportToExcel = new Button();
            exportToExcel.setText("Exportar a Excel");
            exportToExcel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            exportToExcel.addClickListener(buttonClickEvent -> {
                exportToExcel();
            });

            horizontalLayout.add(createService, exportToExcel);

            anchorLayout = new HorizontalLayout();

            verticalLayout.add(horizontalLayout, anchorLayout);


            Grid<Service> grid = new Grid<>(Service.class, false);
            grid.addColumn(Service::getSubject).setHeader("Nombre");
            grid.addItemClickListener(serviceItemClickEvent -> {
                grid.getUI().ifPresent(ui -> {
                    ui.navigate("/services/edit/" + serviceItemClickEvent.getItem().getId());
                });
            });

            services = servicesService.getServices();
            grid.setItems(services);
            grid.setHeightFull();

            verticalLayout.add(grid);
            verticalLayout.setHeightFull();

            setContent(verticalLayout);
        }
    }

    public void download(File file) {
        Anchor anchor = new Anchor(getStreamResource(file.getName(), file), file.getName());
        anchor.getElement().setAttribute("download", true);
        anchor.setHref(getStreamResource(file.getName(), file));
        anchorLayout.add(anchor);
    }

    public StreamResource getStreamResource(String filename, File content) {
        return new StreamResource(filename, () -> {
            try {
                return new ByteArrayInputStream(FileUtils.readFileToByteArray(content));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    private void exportToExcel() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Serveis");

        Row firstRow = sheet.createRow(0);

        Cell subjectTitleCell = firstRow.createCell(0);
        subjectTitleCell.setCellValue("Assumpte");

        for (int i = 1; (i-1) < services.size(); i++) {
            Row row = sheet.createRow(i);

            Cell subjectCell = row.createCell(0);
            subjectCell.setCellValue(services.get(i - 1).getSubject());

        }

        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream("test.xlsx");
            workbook.write(fileOut);
            download(new File("test.xlsx"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert fileOut != null;
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
