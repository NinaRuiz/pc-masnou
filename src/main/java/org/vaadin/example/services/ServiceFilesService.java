package org.vaadin.example.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.example.models.UploadedFile;
import org.vaadin.example.repositories.ServiceFilesRepository;

import java.util.List;

@Service
public class ServiceFilesService {

    private final ServiceFilesRepository serviceFilesRepository;

    @Autowired
    public ServiceFilesService(ServiceFilesRepository serviceFilesRepository) {
        this.serviceFilesRepository = serviceFilesRepository;
    }

    public void deleteServiceFiles(Integer serviceId) {
        serviceFilesRepository.deleteAllByServiceId(serviceId);
    }

    public void saveServiceUploadedFiles(List<UploadedFile> uploadedFiles, Integer serviceId) {
        uploadedFiles.forEach(
                uploadedFile -> {
                    uploadedFile.setServiceId(serviceId);
                    serviceFilesRepository.save(uploadedFile);
                });
    }

    public List<UploadedFile> getServiceUploadedFiles(Integer serviceId) {
        return serviceFilesRepository.findAllByServiceId(serviceId);
    }
}
