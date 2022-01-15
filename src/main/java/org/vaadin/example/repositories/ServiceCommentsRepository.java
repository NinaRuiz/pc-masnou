package org.vaadin.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.models.ServiceComment;

import java.util.List;

public interface ServiceCommentsRepository extends JpaRepository<ServiceComment, Integer> {

    public List<ServiceComment> findAllByServiceIdOrderByCommentDatetime(Integer serviceId);

    public void deleteAllByServiceId(Integer serviceId);
}
