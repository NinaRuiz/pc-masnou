package org.vaadin.example.services;

import com.vaadin.flow.server.VaadinSession;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.example.models.Session;
import org.vaadin.example.models.Volunteer;
import org.vaadin.example.repositories.SessionRepository;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@Service
public class SessionService {

    public static Session session = null;

    private final SessionRepository sessionRepository;

    private final VolunteerService volunteerService;

    @Autowired
    public SessionService(SessionRepository sessionRepository,
                          VolunteerService volunteerService) {
        this.sessionRepository = sessionRepository;
        this.volunteerService = volunteerService;
    }

    @Transactional
    public Session login(String user, String password) throws NoSuchAlgorithmException {
        Volunteer volunteer = volunteerService.getVolunteerByVolunteerCode(user);
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        System.out.println(password + " - " + volunteer.getUserPassword());
        if (passwordEncryptor.checkPassword(password, volunteer.getUserPassword())) {
            Date currentDate = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(currentDate);
            c.add(Calendar.MONTH, 1);

            deleteExpiredSession(VaadinSession.getCurrent().getBrowser().getAddress());

            Session session = new Session();
            session.setLastConnection(new Date());
            session.setExpirationDate(c.getTime());
            session.setIpAddress(VaadinSession.getCurrent().getBrowser().getAddress());
            session.setVolunteerId(volunteer.getId().intValue());
            System.out.println("INIT SESSION");
            return sessionRepository.save(session);
        } else {
            System.out.println("NULL");
            return null;
        }
    }

    public Session getSessionByIp(String ipAddress) {
        return sessionRepository.findByIpAddress(ipAddress);
    }

    private void deleteExpiredSession(String ipAddress) {
        sessionRepository.deleteByExpirationDateBeforeAndIpAddress(new Date(), ipAddress);
    }
}
