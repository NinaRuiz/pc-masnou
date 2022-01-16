package org.vaadin.example.models;

import com.vaadin.flow.component.messages.MessageListItem;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "service")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "eventTime")
    private String eventTime;

    @Column(name = "initTime")
    private String initTime;

    @Column(name = "endTime")
    private String endTime;

    @Column(name = "initKm")
    private String initKm;

    @Column(name = "endKm")
    private String endKm;

    @Column(name = "boss")
    private Integer boss;

    @Transient
    private Volunteer bossVolunteer;

    @Transient
    private List<Volunteer> volunteers;

    @Transient
    private List<Vehicle> vehicles;

    @Transient
    private List<ServiceComment> comments;

    @Transient
    private List<UploadedFile> files;

    public List<UploadedFile> getFiles() {
        return files;
    }

    public void setFiles(List<UploadedFile> files) {
        this.files = files;
    }

    public List<Volunteer> getVolunteers() {
        return volunteers;
    }

    public void setVolunteers(List<Volunteer> volunteers) {
        this.volunteers = volunteers;
    }

    public Integer getBoss() {
        return boss;
    }

    public void setBoss(Integer boss) {
        this.boss = boss;
    }

    public Volunteer getBossVolunteer() {
        return bossVolunteer;
    }

    public void setBossVolunteer(Volunteer bossVolunteer) {
        this.bossVolunteer = bossVolunteer;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getInitTime() {
        return initTime;
    }

    public void setInitTime(String initTime) {
        this.initTime = initTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getInitKm() {
        return initKm;
    }

    public void setInitKm(String initKm) {
        this.initKm = initKm;
    }

    public String getEndKm() {
        return endKm;
    }

    public void setEndKm(String endKm) {
        this.endKm = endKm;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public List<ServiceComment> getComments() {
        return comments;
    }

    public void setComments(List<ServiceComment> comments) {
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
