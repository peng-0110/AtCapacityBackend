package com.example.AtCapacity.controller;

import com.example.AtCapacity.model.Facility;
import com.example.AtCapacity.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/facilities")
public class FacilityController {

    private final FacilityService facilityService;

    @Autowired
    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @GetMapping
    public ResponseEntity<List<Facility>> getAllFacilities() {
        return ResponseEntity.ok(facilityService.getAllFacilities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Facility> getFacilityById(@PathVariable String id) {
        Optional<Facility> facility = facilityService.getFacilityById(id);
        return facility.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Facility> createFacility(@RequestBody Facility facility) {
        Facility savedFacility = facilityService.createFacility(facility);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFacility);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Facility> updateFacility(@PathVariable String id, @RequestBody Facility facility) {
        Optional<Facility> existingFacility = facilityService.getFacilityById(id);
        
        if (existingFacility.isPresent()) {
            facility.setId(id);
            facilityService.updateFacility(facility);
            return ResponseEntity.ok(facility);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/increment")
    public ResponseEntity<String> incrementOccupancy(@PathVariable String id) {
        boolean incremented = facilityService.incrementOccupancy(id);
        
        if (incremented) {
            return ResponseEntity.ok("Occupancy incremented successfully");
        } else {
            return ResponseEntity.badRequest().body("Could not increment occupancy. Facility may be at capacity or not found.");
        }
    }

    @PostMapping("/{id}/decrement")
    public ResponseEntity<String> decrementOccupancy(@PathVariable String id) {
        boolean decremented = facilityService.decrementOccupancy(id);
        
        if (decremented) {
            return ResponseEntity.ok("Occupancy decremented successfully");
        } else {
            return ResponseEntity.badRequest().body("Could not decrement occupancy. Facility may be empty or not found.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacility(@PathVariable String id) {
        facilityService.deleteFacility(id);
        return ResponseEntity.noContent().build();
    }

    // New endpoint to get facilities by owner
    @GetMapping("/owner/{owner}")
    public ResponseEntity<List<Facility>> getFacilitiesByOwner(@PathVariable String owner) {
        List<Facility> facilities = facilityService.getFacilitiesByOwner(owner);
        return ResponseEntity.ok(facilities);
    }

    // New endpoint to get facilities by address
    @GetMapping("/address/{address}")
    public ResponseEntity<List<Facility>> getFacilitiesByAddress(@PathVariable String address) {
        List<Facility> facilities = facilityService.getFacilitiesByAddress(address);
        return ResponseEntity.ok(facilities);
    }
}