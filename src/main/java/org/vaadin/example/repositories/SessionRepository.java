package org.vaadin.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.models.Session;

import java.util.Date;

public interface SessionRepository extends JpaRepository<Session, Long> {

    public Session findByIpAddress(String ipAddress);

    public void deleteByExpirationDateBeforeAndIpAddress(Date expirationDate, String ipAddress);

    public void deleteByIpAddress(String ipAddress);
}
