package org.vaadin.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.models.ServiceVolunteer;

public interface ServiceVolunteerRepository extends JpaRepository<ServiceVolunteer, Integer> {

    public void deleteAllByServiceId(Integer serviceId);
}
