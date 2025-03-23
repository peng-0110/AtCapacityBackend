package com.example.AtCapacity.repository;

import com.example.AtCapacity.model.Facility;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // Add this import

@Repository
public interface FacilityRepository extends MongoRepository<Facility, String> {
    Optional<Facility> findByName(String name); // Existing method
    void deleteByName(String name);

    // Methods for querying by owner and address
    List<Facility> findByOwner(String owner);
}