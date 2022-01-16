package org.vaadin.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.models.UploadedFile;

import java.util.List;

public interface ServiceFilesRepository extends JpaRepository<UploadedFile, Integer> {

    public void deleteAllByServiceId(Integer serviceId);

    public List<UploadedFile> findAllByServiceId(Integer serviceId);
}
