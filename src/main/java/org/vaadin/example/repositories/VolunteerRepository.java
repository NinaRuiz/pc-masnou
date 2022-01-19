package org.vaadin.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.vaadin.example.models.Volunteer;

import java.util.List;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

    @Query(
            value = "SELECT v.* FROM pcMasnouProd.volunteer v " +
                    "LEFT JOIN pcMasnouProd.service_volunteer sv ON v.id = sv.volunteerId " +
                    "WHERE sv.serviceId = ?1",
            nativeQuery = true)
    public List<Volunteer> getVolunteersByServiceId(Integer serviceId);

    public Volunteer getVolunteerByVolunteerCode(String volunteerCode);

}
