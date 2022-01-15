package org.vaadin.example.models;

import javax.persistence.*;

@Entity
@Table(name = "volunteer")
public class Volunteer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "volunteerCode", nullable = false)
    private String volunteerCode;

    private String firstname;

    private String firstlastname;

    private String secondlastname;

    private String userPassword;

    private Integer roleId;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFirstlastname() {
        return firstlastname;
    }

    public void setFirstlastname(String firstlastname) {
        this.firstlastname = firstlastname;
    }

    public String getSecondlastname() {
        return secondlastname;
    }

    public void setSecondlastname(String secondlastname) {
        this.secondlastname = secondlastname;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getVolunteerCode() {
        return volunteerCode;
    }

    public void setVolunteerCode(String volunteerCode) {
        this.volunteerCode = volunteerCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
