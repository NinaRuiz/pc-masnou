package org.vaadin.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.models.ServiceVehicle;

public interface ServiceVehicleRepository extends JpaRepository<ServiceVehicle, Integer> {

    public void deleteAllByServiceId(Integer serviceId);
}
