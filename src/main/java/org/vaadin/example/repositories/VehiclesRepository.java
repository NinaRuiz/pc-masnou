package org.vaadin.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.vaadin.example.models.Vehicle;

import java.util.List;

public interface VehiclesRepository extends JpaRepository<Vehicle, Integer> {

    @Query(
            value = "SELECT v.* FROM pcMasnouProd.vehicles v " +
                    "LEFT JOIN pcMasnouProd.service_vehicles sv ON v.id = sv.vehicleId " +
                    "WHERE sv.serviceId = ?1",
            nativeQuery = true)
    public List<Vehicle> findAllByServiceId(Integer serviceId);
}
