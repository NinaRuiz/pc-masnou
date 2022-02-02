package org.vaadin.example.views;

import com.flowingcode.vaadin.addons.googlemaps.GoogleMap;
import com.flowingcode.vaadin.addons.googlemaps.LatLon;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.components.EndSessionComponent;
import org.vaadin.example.layouts.MainLayout;
import org.vaadin.example.models.*;
import org.vaadin.example.services.ServicesService;
import org.vaadin.example.services.SessionService;
import org.vaadin.example.services.VehicleService;
import org.vaadin.example.services.VolunteerService;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Route("/services/edit")
public class ServicesFormView extends MainLayout implements HasUrlParameter<String> {

    Service service;
    ServicesService servicesService;
    VolunteerService volunteerService;
    VehicleService vehicleService;

    List<Volunteer> volunteers;
    List<Vehicle> vehicles;

    TextField subjectTextField;
    DatePicker dateDatePicker;
    Select<String> eventTimeSelect;
    TimePicker initTimePicker;
    TimePicker endTimePicker;
    TextField initKmTextField;
    TextField endKmTextField;
    Select<Volunteer> bossSelect;
    MultiSelectListBox<Volunteer> volunteerListBox;
    MultiSelectListBox<Vehicle> vehicleListBox;
    VerticalLayout commentMessageList;
    Grid<UploadedFile> uploadedFileGrid;



    @Autowired
    public ServicesFormView(SessionService sessionService, ServicesService servicesService, VolunteerService volunteerService,
                            VehicleService vehicleService) {

        SessionService.session = sessionService.getSessionByIp(VaadinSession.getCurrent().getBrowser().getAddress());

        if (SessionService.session == null) {
            setContent(new EndSessionComponent());
        } else {

            this.servicesService = servicesService;
            this.volunteerService = volunteerService;
            this.vehicleService = vehicleService;

            service = new Service();

            VerticalLayout verticalLayout = new VerticalLayout();

            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.setWidthFull();

            Button cancelService = new Button();
            cancelService.setText("Cancelar");
            cancelService.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            cancelService.addClickListener(clickEvent -> {
                cancelService.getUI().ifPresent(click -> {
                    click.navigate(ServicesListView.class);
                });
            });

            Button createService = new Button();
            createService.setText("Guardar");
            createService.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            createService.addClickListener(click -> {
                if (saveService()) {
                    createService.getUI().ifPresent(click2 -> {
                        click2.navigate(ServicesListView.class);
                    });
                }

            });

            horizontalLayout.add(cancelService, createService);

            subjectTextField = new TextField();
            subjectTextField.setLabel("Assumpte: ");
            subjectTextField.addValueChangeListener(change -> {
                if (change.isFromClient()) {
                    service.setSubject(subjectTextField.getValue());
                }
            });
            subjectTextField.setWidthFull();

            FormLayout formLayout = new FormLayout();

            dateDatePicker = new DatePicker();
            dateDatePicker.setLabel("Data:");
            dateDatePicker.addValueChangeListener(change -> {
                if (change.isFromClient()) {
                    service.setDate(dateDatePicker.getValue());
                }
            });

            eventTimeSelect = new Select<>();
            eventTimeSelect.setLabel("Torn: ");
            eventTimeSelect.setItems("Matí", "Tarda", "Nit");
            eventTimeSelect.addValueChangeListener(change -> {
                if (change.isFromClient()) {
                    service.setEventTime(eventTimeSelect.getValue());
                }
            });

            initTimePicker = new TimePicker();
            initTimePicker.setLabel("Hora d'inici: ");
            initTimePicker.addValueChangeListener(change -> {
                if (change.isFromClient()) {
                    service.setInitTime(initTimePicker.getValue().toString());
                }
            });

            endTimePicker = new TimePicker();
            endTimePicker.setLabel("Hora final: ");
            endTimePicker.addValueChangeListener(change -> {
                if (change.isFromClient()) {
                    service.setEndTime(endTimePicker.getValue().toString());
                }
            });

            initKmTextField = new TextField();
            initKmTextField.setLabel("Km inici: ");
            initKmTextField.addValueChangeListener(change -> {
                if (change.isFromClient()) {
                    service.setInitKm(initKmTextField.getValue());
                }
            });

            endKmTextField = new TextField();
            endKmTextField.setLabel("Km final: ");
            endKmTextField.addValueChangeListener(change -> {
                if (change.isFromClient()) {
                    service.setEndKm(endKmTextField.getValue());
                }
            });

            bossSelect = new Select<>();
            bossSelect.setItemLabelGenerator(Volunteer::getVolunteerCode);
            bossSelect.setLabel("Cap de torn: ");
            volunteers = volunteerService.getVolunteers();
            bossSelect.setItems(volunteers);
            bossSelect.addValueChangeListener(change -> {
                if (change.isFromClient()) {
                    service.setBoss(bossSelect.getValue().getId().intValue());
                }
            });

            Label label = new Label("Voluntaris: ");

            volunteerListBox = new MultiSelectListBox<>();
            volunteerListBox.setRenderer(new ComponentRenderer<>(volunteer -> {
                HorizontalLayout row = new HorizontalLayout();
                Label label1 = new Label(volunteer.getVolunteerCode());
                row.add(label1);
                return row;
            }));
            volunteerListBox.setItems(volunteers);
            volunteerListBox.addValueChangeListener(change -> {
                if (change.isFromClient()) {
                    service.setVolunteers(new ArrayList<>(volunteerListBox.getSelectedItems()));
                }
            });

            Label label1 = new Label("Vehicle: ");

            vehicleListBox = new MultiSelectListBox<>();
            vehicles = vehicleService.getVehicles();
            vehicleListBox.setItems(vehicles);
            vehicleListBox.setRenderer(new ComponentRenderer<>(vehicle -> {
                HorizontalLayout row = new HorizontalLayout();
                Label label2 = new Label(vehicle.getVehicleCode());
                row.add(label2);
                return row;
            }));
            vehicleListBox.addValueChangeListener(change -> {
                if (change.isFromClient()) {
                    service.setVehicles(new ArrayList<>(vehicleListBox.getSelectedItems()));
                }
            });

            commentMessageList = new VerticalLayout();
            commentMessageList.setWidthFull();


            MessageInput messageInput = new MessageInput();
            messageInput.setWidthFull();
            messageInput.addSubmitListener(submitEvent -> {
                if (submitEvent.isFromClient()) {
                    System.out.println(submitEvent.getValue().length());
                    if (submitEvent.getValue().length() > 765) {
                        String[] messageWords = submitEvent.getValue().split("\\.");
                        List<String> comments = new ArrayList<>();
                        StringBuilder comment = new StringBuilder();
                        for (int i = 0; i < messageWords.length; i++) {
                            System.out.println(messageWords[i]);
                            if ((comment.length() + messageWords[i].length() + 1) > 750) {
                                System.out.println(comment);
                                comments.add(comment.toString());
                                comment = new StringBuilder();
                                comment.append(messageWords[i] + ".");
                            } else {
                                comment.append(messageWords[i] + ".");
                            }

                            if ((i + 1) == messageWords.length) {
                                comments.add(comment.toString());
                            }
                        }
                        comments.forEach(s -> {
                            ServiceComment serviceComment = new ServiceComment();
                            serviceComment.setCommentDatetime(new Date());
                            serviceComment.setCommentMessage(s);
                            serviceComment.setUserId(SessionService.session.getVolunteerId());
                            service.getComments().add(serviceComment);
                            System.out.println(s);
                            addCommentToMessageList(serviceComment);
                        });
                    } else {
                        ServiceComment serviceComment = new ServiceComment();
                        serviceComment.setCommentDatetime(new Date());
                        serviceComment.setCommentMessage(submitEvent.getValue());
                        serviceComment.setUserId(SessionService.session.getVolunteerId());
                        service.getComments().add(serviceComment);
                        addCommentToMessageList(serviceComment);
                    }
                }
            });

            uploadedFileGrid = new Grid<>();
            uploadedFileGrid.setAllRowsVisible(true);
            uploadedFileGrid.addColumn(UploadedFile::getUploadDate).setHeader("");
            uploadedFileGrid.addColumn(UploadedFile::getFileName).setHeader("Archivos");
            uploadedFileGrid.addComponentColumn(item -> new Anchor(getStreamResource(item.getFileName(), new File(item.getFilePath())), "descargar"));


            MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
            Upload upload = new Upload(buffer);
            upload.setWidthFull();
            upload.setMaxFileSize(5242880);

        /*
        GoogleMap gmaps = new GoogleMap("AIzaSyByJG0JeS-wcNN5r8WIAOLZ2g00WIHl0r4", null, null);
        gmaps.setMapType(GoogleMap.MapType.SATELLITE);
        gmaps.setSizeFull();
        gmaps.setCenter(new LatLon(-31.636036, -60.7055271));
        gmaps.setVisible(true);*/

            upload.addSucceededListener(event -> {
                UploadedFile uploadedFile = new UploadedFile();
                uploadedFile.setUploadDate(new Date());
                uploadedFile.setFileName(event.getFileName());
                uploadedFile.setFilePath("./src/main/resources/uploadFiles/" + event.getFileName());
                InputStream inputStream = buffer.getInputStream(uploadedFile.getFileName());
                File file = new File(uploadedFile.getFilePath());
                try {
                    copyInputStreamToFile(inputStream, file);
                    if (service.getFiles() == null) {
                        service.setFiles(new ArrayList<>());
                    }
                    service.getFiles().add(uploadedFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            verticalLayout.add(horizontalLayout, subjectTextField);

            formLayout.add(
                    dateDatePicker,
                    eventTimeSelect,
                    initTimePicker,
                    endTimePicker,
                    initKmTextField,
                    endKmTextField,
                    bossSelect);

            verticalLayout.add(formLayout, label, volunteerListBox, label1, vehicleListBox, commentMessageList,
                    uploadedFileGrid, upload, messageInput);

            setContent(verticalLayout);
        }
    }

    private static void copyInputStreamToFile(InputStream inputStream, File file)
            throws IOException {

        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[8192];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }

    }

    private boolean saveService() {
        if (validateForm()) {
            servicesService.saveService(service);
            return true;
        } else {
            Notification.show("Tienes que indicar como minimo el titulo y la fecha del servicio.");
            return false;
        }
    }

    private boolean validateForm() {
        return service.getDate() != null
                && service.getSubject() != null;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        if (!"new".equals(s) && SessionService.session != null) {
            service = servicesService.getServiceById(Long.parseLong(s));
            subjectTextField.setValue(service.getSubject());
            dateDatePicker.setValue(service.getDate());
            eventTimeSelect.setValue(service.getEventTime() != null ? service.getEventTime() : "");
            initTimePicker.setValue(service.getInitTime() != null ? LocalTime.parse(service.getInitTime()) : LocalTime.MIN);
            endTimePicker.setValue(service.getEndTime() != null ? LocalTime.parse(service.getEndTime()) : LocalTime.MIN);
            initKmTextField.setValue(service.getInitKm() != null ? service.getInitKm() : "");
            endKmTextField.setValue(service.getEndKm() != null ? service.getEndKm() : "");

            if (service.getBoss() != null) {
                volunteers.forEach(
                        volunteer -> {
                            if (volunteer.getId().intValue() == service.getBoss()) {
                                bossSelect.setValue(volunteer);
                            }
                        }
                );
            }

            if (service.getVolunteers() != null) {
                volunteerListBox.select(volunteers.stream().filter(volunteer -> {
                    AtomicBoolean out = new AtomicBoolean(false);
                    service.getVolunteers().forEach(
                            volunteer1 -> {
                                if(volunteer.getId().equals(volunteer1.getId())) {
                                    out.set(true);
                                }
                            });
                    return out.get();
                }).collect(Collectors.toList()));
            }

            if (service.getVehicles() != null) {
                vehicleListBox.select(vehicles.stream().filter(vehicle -> {
                    AtomicBoolean out = new AtomicBoolean(false);
                    service.getVehicles().forEach(
                            vehicle1 -> {
                                if(vehicle.getId().equals(vehicle1.getId())) {
                                    out.set(true);
                                }
                            });
                    return out.get();
                }).collect(Collectors.toList()));
            }

            if (service.getComments() != null) {
                service.getComments().forEach(this::addCommentToMessageList);
            }

            if (service.getFiles() != null) {
                uploadedFileGrid.setItems(service.getFiles());
            }

        }
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

    private void addCommentToMessageList(ServiceComment serviceComment) {
        String lastCommentUserId = null;
        if (commentMessageList.getChildren().findAny().isPresent()) {
            Component component = commentMessageList.getChildren().collect(Collectors.toList())
                    .get((int) commentMessageList.getChildren().count() - 1);
            lastCommentUserId = component.getId().orElse(null);
        }
        boolean showAvatarAndName = true;
        if (lastCommentUserId != null && lastCommentUserId.equals(serviceComment.getUserId().toString())) {
            showAvatarAndName = false;
        }
        HorizontalLayout hl1 = new HorizontalLayout();
        HorizontalLayout hl2 = new HorizontalLayout();
        VerticalLayout vl = new VerticalLayout();
        vl.setPadding(false);
        vl.setMargin(false);
        if (showAvatarAndName) {
            Volunteer volunteer = volunteerService.getVolunteerById(serviceComment.getUserId());
            Avatar avatar = new Avatar(volunteer.getVolunteerCode());
            avatar.setAbbreviation(volunteer.getVolunteerCode().substring(2));
            hl1.add(avatar);
            Label volunteerLabel = new Label(volunteer.getVolunteerCode() +
                    " - " + volunteer.getFirstname() + " " + volunteer.getFirstlastname());
            volunteerLabel.getStyle().set("font-weight", "bold");
            hl2.add(volunteerLabel);
        } else {
            Label timeComment = new Label(serviceComment.getCommentDatetime().toString());
            timeComment.getStyle().set("font-size", "10px");
            hl2.add(timeComment);
            vl.setMargin(true);
            vl.getStyle().set("margin-left", "70px");
        }
        vl.add(hl2);
        vl.add(new Label(serviceComment.getCommentMessage()));
        hl1.add(vl);
        hl1.setId(serviceComment.getUserId().toString());
        commentMessageList.add(hl1);
    }
}
