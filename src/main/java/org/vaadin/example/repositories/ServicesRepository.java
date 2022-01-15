package org.vaadin.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.models.Service;

public interface ServicesRepository extends JpaRepository<Service, Long> {

}
