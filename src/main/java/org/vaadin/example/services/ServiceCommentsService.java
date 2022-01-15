package org.vaadin.example.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.example.models.ServiceComment;
import org.vaadin.example.repositories.ServiceCommentsRepository;

import java.util.List;

@Service
public class ServiceCommentsService {

    private final ServiceCommentsRepository serviceCommentsRepository;

    @Autowired
    public ServiceCommentsService(
            ServiceCommentsRepository serviceCommentsRepository
    ) {
        this.serviceCommentsRepository = serviceCommentsRepository;
    }

    public List<ServiceComment> getServiceCommentsByServiceId(Integer serviceId) {
        return serviceCommentsRepository.findAllByServiceIdOrderByCommentDatetime(serviceId);
    }

    public void deleteServiceCommentsByServiceId(Integer serviceId) {
        serviceCommentsRepository.deleteAllByServiceId(serviceId);
    }

    public void saveServiceComments(List<ServiceComment> comments, Integer serviceId) {
        comments.forEach(
                serviceComment -> {
                    serviceComment.setServiceId(serviceId);
                    serviceCommentsRepository.save(serviceComment);
                });
    }
}
