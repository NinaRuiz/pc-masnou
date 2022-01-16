package org.vaadin.example.views;

import com.flowingcode.vaadin.addons.googlemaps.GoogleMap;
import com.flowingcode.vaadin.addons.googlemaps.LatLon;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.components.EndSessionComponent;
import org.vaadin.example.layouts.MainLayout;
import org.vaadin.example.models.*;
import org.vaadin.example.services.ServicesService;
import org.vaadin.example.services.SessionService;
import org.vaadin.example.services.VehicleService;
import org.vaadin.example.services.VolunteerService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    MessageList commentMessageList;
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
                saveService();
                createService.getUI().ifPresent(click2 -> {
                    click2.navigate(ServicesListView.class);
                });
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

            commentMessageList = new MessageList();
            commentMessageList.setWidthFull();

            MessageInput messageInput = new MessageInput();
            messageInput.setWidthFull();
            messageInput.addSubmitListener(submitEvent -> {
                if (submitEvent.isFromClient()) {
                    List<MessageListItem> messageListItemList = new ArrayList<>(commentMessageList.getItems());
                    Volunteer volunteer = volunteerService.getVolunteerById(SessionService.session.getVolunteerId());
                    MessageListItem messageListItem = new MessageListItem(
                            submitEvent.getValue(),
                            new Date().toInstant(),
                            volunteer.getVolunteerCode() + " - " + volunteer.getFirstname() + " " + volunteer.getFirstlastname());
                    messageListItemList.add(messageListItem);
                    commentMessageList.setItems(messageListItemList);
                    service.setComments(new ArrayList<>());
                    service.getComments().addAll(messageListItemList.stream().map(messageListItem1 -> {
                        ServiceComment serviceComment = new ServiceComment();
                        serviceComment.setCommentDatetime(Date.from(messageListItem1.getTime()));
                        serviceComment.setCommentMessage(messageListItem1.getText());
                        serviceComment.setUserId(SessionService.session.getVolunteerId());
                        return serviceComment;
                    }).collect(Collectors.toList()));
                }
            });

            uploadedFileGrid = new Grid<>();
            uploadedFileGrid.setAllRowsVisible(true);
            uploadedFileGrid.addColumn(UploadedFile::getUploadDate).setHeader("");
            uploadedFileGrid.addColumn(UploadedFile::getFileName).setHeader("Archivos");


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

    private void saveService() {
        if (validateForm()) {
            servicesService.saveService(service);
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
            eventTimeSelect.setValue(service.getEventTime());
            initTimePicker.setValue(service.getInitTime() != null ? LocalTime.parse(service.getInitTime()) : LocalTime.MIN);
            endTimePicker.setValue(service.getEndTime() != null ? LocalTime.parse(service.getEndTime()) : LocalTime.MIN);
            initKmTextField.setValue(service.getInitKm() != null ? service.getInitKm() : "");
            endKmTextField.setValue(service.getEndKm() != null ? service.getEndKm() : "");
            volunteers.forEach(
                    volunteer -> {
                        if (volunteer.getId().intValue() == service.getBoss()) {
                            bossSelect.setValue(volunteer);
                        }
                    }
            );

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

            commentMessageList.setItems(service.getComments().stream().map(
                    serviceComment -> {
                        MessageListItem messageListItem = new MessageListItem();
                        messageListItem.setText(serviceComment.getCommentMessage());
                        messageListItem.setTime(serviceComment.getCommentDatetime().toInstant());
                        Volunteer volunteer = volunteerService.getVolunteerById(serviceComment.getUserId());
                        messageListItem.setUserName(
                                volunteer.getVolunteerCode() +
                                " - " + volunteer.getFirstname() + " " + volunteer.getFirstlastname());
                        return messageListItem;
                    }).collect(Collectors.toList()));

            uploadedFileGrid.setItems(service.getFiles());
        }
    }
}
