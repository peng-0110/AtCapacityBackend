package com.example.AtCapacity.service;

import com.example.AtCapacity.model.Facility;
import com.example.AtCapacity.model.FacilityType;
import com.example.AtCapacity.repository.FacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacilityService {

    private final FacilityRepository facilityRepository;

    @Autowired
    public FacilityService(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }

    public Optional<Facility> getFacilityById(String id) {
        return facilityRepository.findById(id);
    }

    public Optional<Facility> getFacilityByName(String name) {
        return facilityRepository.findByName(name);
    }

    public Facility createFacility(Facility facility) {
        return facilityRepository.save(facility);
    }

    public boolean incrementOccupancy(String facilityId) {
        Optional<Facility> facilityOpt = facilityRepository.findById(facilityId);
        
        if (facilityOpt.isPresent()) {
            Facility facility = facilityOpt.get();
            boolean incremented = facility.incrementOccupancy();
            
            if (incremented) {
                facilityRepository.save(facility);
                return true;
            }
        }
        
        return false;
    }

    public boolean decrementOccupancy(String facilityId) {
        Optional<Facility> facilityOpt = facilityRepository.findById(facilityId);
        
        if (facilityOpt.isPresent()) {
            Facility facility = facilityOpt.get();
            boolean decremented = facility.decrementOccupancy();
            
            if (decremented) {
                facilityRepository.save(facility);
                return true;
            }
        }
        
        return false;
    }

    public boolean incrementOccupancyByName(String name) {
        Optional<Facility> facilityOpt = facilityRepository.findByName(name);
        
        if (facilityOpt.isPresent()) {
            Facility facility = facilityOpt.get();
            boolean incremented = facility.incrementOccupancy();
            
            if (incremented) {
                facilityRepository.save(facility);
                return true;
            }
        }
        return false;
    }

    public boolean decrementOccupancyByName(String name) {
        Optional<Facility> facilityOpt = facilityRepository.findByName(name);
        
        if (facilityOpt.isPresent()) {
            Facility facility = facilityOpt.get();
            boolean decremented = facility.decrementOccupancy();
            
            if (decremented) {
                facilityRepository.save(facility);
                return true;
            }
        }
        return false;
    }

    public void updateFacility(Facility facility) {
        facilityRepository.save(facility);
    }

    public void deleteFacility(String id) {
        facilityRepository.deleteById(id);
    }

    public void deleteFacilityByName(String name) {
        facilityRepository.deleteByName(name);
    }

    // New methods for querying by address or owner
    public List<Facility> getFacilitiesByOwner(String owner) {
        return facilityRepository.findByOwner(owner);
    }

    public List<Facility> findNearestFacilities(double latitude, double longitude, int limit) {
        List<Facility> allFacilities = getAllFacilities();
        
        return allFacilities.stream()
                .sorted((f1, f2) -> {
                    double dist1 = f1.calculateDistance(latitude, longitude);
                    double dist2 = f2.calculateDistance(latitude, longitude);
                    return Double.compare(dist1, dist2);
                })
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Facility> findNearestFacilities(double latitude, double longitude, int limit, FacilityType type) {
        List<Facility> allFacilities = getAllFacilities();
        
        return allFacilities.stream()
                .filter(facility -> type == null || facility.getType() == type)
                .sorted((f1, f2) -> {
                    double dist1 = f1.calculateDistance(latitude, longitude);
                    double dist2 = f2.calculateDistance(latitude, longitude);
                    return Double.compare(dist1, dist2);
                })
                .limit(limit)
                .collect(Collectors.toList());
    }
}