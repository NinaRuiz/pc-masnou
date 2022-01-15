package org.vaadin.example.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.example.models.ServiceVehicle;
import org.vaadin.example.models.Vehicle;
import org.vaadin.example.repositories.ServiceVehicleRepository;
import org.vaadin.example.repositories.VehiclesRepository;

import java.util.List;

@Service
public class VehicleService {

    private final VehiclesRepository vehiclesRepository;
    private final ServiceVehicleRepository serviceVehicleRepository;

    @Autowired
    public VehicleService(
            VehiclesRepository vehiclesRepository,
            ServiceVehicleRepository serviceVehicleRepository
    ) {
        this.serviceVehicleRepository = serviceVehicleRepository;
        this.vehiclesRepository = vehiclesRepository;
    }

    public List<Vehicle> getVehicles() {
        return vehiclesRepository.findAll();
    }

    public List<Vehicle> getVehiclesByServiceId(Integer serviceId) {
        return vehiclesRepository.findAllByServiceId(serviceId);
    }

    public void deleteServiceVehiclesByServiceId(Integer serviceId) {
        serviceVehicleRepository.deleteAllByServiceId(serviceId);
    }

    public void saveServiceVehicles(List<Vehicle> vehicles, Integer serviceId) {
        vehicles.forEach(
                vehicle -> {
                    ServiceVehicle serviceVehicle = new ServiceVehicle();
                    serviceVehicle.setServiceId(serviceId);
                    serviceVehicle.setVehicleId(vehicle.getId().intValue());
                    serviceVehicleRepository.save(serviceVehicle);
                });
    }

}
