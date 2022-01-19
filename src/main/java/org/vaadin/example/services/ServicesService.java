package org.vaadin.example.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.example.repositories.ServicesRepository;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ServicesService {

    private final ServicesRepository servicesRepository;
    private final VolunteerService volunteerService;
    private final VehicleService vehicleService;
    private final ServiceCommentsService serviceCommentsService;
    private final ServiceFilesService serviceFilesService;

    @Autowired
    public ServicesService(ServicesRepository servicesRepository,
                           VolunteerService volunteerService,
                           VehicleService vehicleService,
                           ServiceCommentsService serviceCommentsService,
                           ServiceFilesService serviceFilesService) {
        this.servicesRepository = servicesRepository;
        this.volunteerService = volunteerService;
        this.vehicleService = vehicleService;
        this.serviceCommentsService = serviceCommentsService;
        this.serviceFilesService = serviceFilesService;
    }

    public org.vaadin.example.models.Service getServiceById(Long id){
        Optional<org.vaadin.example.models.Service> service = servicesRepository.findById(id);
        if (service.isPresent()) {
            service.get().setVolunteers(volunteerService.getServiceVolunteers(service.get().getId().intValue()));
            service.get().setVehicles(vehicleService.getVehiclesByServiceId(service.get().getId().intValue()));
            service.get().setComments(serviceCommentsService.getServiceCommentsByServiceId(service.get().getId().intValue()));
            if (service.get().getBoss() != null) {
                service.get().setBossVolunteer(volunteerService.getVolunteerById(service.get().getBoss()));
            }
            service.get().setFiles(serviceFilesService.getServiceUploadedFiles(service.get().getId().intValue()));
            return service.get();
        } else {
            return null;
        }
    }

    public List<org.vaadin.example.models.Service> getServices() {
        return servicesRepository.findAll();
    }

    @Transactional
    public void saveService(org.vaadin.example.models.Service service) {
        org.vaadin.example.models.Service newService = servicesRepository.save(service);

        if (service.getVolunteers() != null) {
            volunteerService.deleteServiceVolunteers(newService.getId().intValue());
            volunteerService.saveServiceVolunteers(service.getVolunteers(), newService.getId().intValue());
        }

        if (service.getVehicles() != null) {
            vehicleService.deleteServiceVehiclesByServiceId(newService.getId().intValue());
            vehicleService.saveServiceVehicles(service.getVehicles(), newService.getId().intValue());
        }

        if (service.getComments() != null) {
            serviceCommentsService.deleteServiceCommentsByServiceId(newService.getId().intValue());
            serviceCommentsService.saveServiceComments(service.getComments(), newService.getId().intValue());
        }

        if (service.getFiles() != null) {
            serviceFilesService.deleteServiceFiles(service.getId().intValue());
            serviceFilesService.saveServiceUploadedFiles(service.getFiles(), newService.getId().intValue());
        }

    }

}
