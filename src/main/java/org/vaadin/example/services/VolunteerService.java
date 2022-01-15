package org.vaadin.example.services;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.example.models.ServiceVolunteer;
import org.vaadin.example.models.Volunteer;
import org.vaadin.example.repositories.ServiceVolunteerRepository;
import org.vaadin.example.repositories.VolunteerRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;
    private final ServiceVolunteerRepository serviceVolunteerRepository;

    @Autowired
    public VolunteerService(VolunteerRepository volunteerRepository,
                            ServiceVolunteerRepository serviceVolunteerRepository) {
        this.volunteerRepository = volunteerRepository;
        this.serviceVolunteerRepository = serviceVolunteerRepository;
    }

    public Volunteer saveVolunteer(Volunteer volunteer) throws NoSuchAlgorithmException {
        if (volunteer.getId() == null) {
            StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
            String encryptedPassword = passwordEncryptor.encryptPassword(volunteer.getUserPassword());
            volunteer.setUserPassword(encryptedPassword);
        }
        volunteer.setVolunteerCode(volunteer.getVolunteerCode().toUpperCase(Locale.ROOT));
        return volunteerRepository.save(volunteer);
    }

    public Volunteer getVolunteerByVolunteerCode(String volunteerCode) {
        return volunteerRepository.getVolunteerByVolunteerCode(volunteerCode.toUpperCase(Locale.ROOT));
    }

    public Volunteer getVolunteerById(Integer id) {
        return volunteerRepository
                .findById(Long.parseLong(id.toString()))
                .orElse(null);
    }

    public List<Volunteer> getServiceVolunteers(Integer serviceId) {
        return volunteerRepository.getVolunteersByServiceId(serviceId);
    }

    public List<Volunteer> getVolunteers() {
        return volunteerRepository.findAll();
    }

    public void deleteServiceVolunteers(Integer serviceId) {
        serviceVolunteerRepository.deleteAllByServiceId(serviceId);
    }

    public void saveServiceVolunteers(List<Volunteer> volunteersIds, Integer serviceId) {
        volunteersIds.forEach(
                volunteer -> {
                    ServiceVolunteer serviceVolunteer = new ServiceVolunteer();
                    serviceVolunteer.setVolunteerId(volunteer.getId().intValue());
                    serviceVolunteer.setServiceId(serviceId);
                    serviceVolunteerRepository.save(serviceVolunteer);
                });
    }

}
