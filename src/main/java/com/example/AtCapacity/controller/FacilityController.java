package com.example.AtCapacity.controller;

import com.example.AtCapacity.model.Facility;
import com.example.AtCapacity.model.FacilityType;
import com.example.AtCapacity.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing facilities.
 * Provides endpoints for CRUD operations and specialized facility queries.
 */
@RestController
@RequestMapping("/api/facilities")
public class FacilityController {

    private final FacilityService facilityService;

    @Autowired
    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    /**
     * Retrieves all facilities.
     * @return list of all facilities in the system
     */
    @GetMapping
    public ResponseEntity<List<Facility>> getAllFacilities() {
        return ResponseEntity.ok(facilityService.getAllFacilities());
    }

    /**
     * Retrieves a facility by its unique identifier.
     * @param id the facility's MongoDB ID
     * @return the facility if found, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Facility> getFacilityById(@PathVariable String id) {
        Optional<Facility> facility = facilityService.getFacilityById(id);
        return facility.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves a facility by its name.
     * @param name unique name of the facility
     * @return the facility if found, or 404 if not found
     */
    @GetMapping
    public ResponseEntity<Facility> getFacilityByName(@RequestParam String name) {
        Optional<Facility> facility = facilityService.getFacilityByName(name);
    return facility.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }


    /**
     * Creates a new facility.
     * @param facility facility details in request body
     * @return created facility with generated ID
     */
    @PostMapping
    public ResponseEntity<Facility> createFacility(@RequestBody Facility facility) {
        Facility savedFacility = facilityService.createFacility(facility);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFacility);
    }

    /**
     * Updates an existing facility by ID.
     * @param id facility ID to update
     * @param facility updated facility details
     * @return updated facility or 404 if not found
     */
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

    /**
     * Updates an existing facility by name.
     * @param name facility name to update
     * @param facility updated facility details
     * @return updated facility or 404 if not found
     */
    @PutMapping("/by-name/{name}")
    public ResponseEntity<Facility> updateFacilityByName(@PathVariable String name, @RequestBody Facility facility) {
        Optional<Facility> existingFacility = facilityService.getFacilityByName(name);
        
        if (existingFacility.isPresent()) {
            facility.setId(existingFacility.get().getId()); // Preserve the original ID
            facilityService.updateFacility(facility);
            return ResponseEntity.ok(facility);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Increments facility occupancy by ID.
     * @param id facility ID
     * @return success message or error if at capacity/not found
     */
    @PostMapping("/{id}/increment")
    public ResponseEntity<String> incrementOccupancy(@PathVariable String id) {
        boolean incremented = facilityService.incrementOccupancy(id);
        
        if (incremented) {
            return ResponseEntity.ok("Occupancy incremented successfully");
        } else {
            return ResponseEntity.badRequest().body("Could not increment occupancy. Facility may be at capacity or not found.");
        }
    }

    /**
     * Increments facility occupancy by name.
     * @param name facility name
     * @return success message or error if at capacity/not found
     */
    @PostMapping("/{name}/increment")
    public ResponseEntity<String> incrementOccupancyByName(@PathVariable String name) {
        boolean incremented = facilityService.incrementOccupancyByName(name);
        
        if (incremented) {
            return ResponseEntity.ok("Occupancy incremented successfully");
        } else {
            return ResponseEntity.badRequest().body("Could not increment occupancy. Facility may be at capacity or not found.");
        }
    }

    /**
     * Decrements facility occupancy by ID.
     * @param id facility ID
     * @return success message or error if empty/not found
     */
    @PostMapping("/{id}/decrement")
    public ResponseEntity<String> decrementOccupancy(@PathVariable String id) {
        boolean decremented = facilityService.decrementOccupancy(id);
        
        if (decremented) {
            return ResponseEntity.ok("Occupancy decremented successfully");
        } else {
            return ResponseEntity.badRequest().body("Could not decrement occupancy. Facility may be empty or not found.");
        }
    }

    /**
     * Decrements facility occupancy by name.
     * @param name facility name
     * @return success message or error if empty/not found
     */
    @PostMapping("/{name}/decrement")
    public ResponseEntity<String> decrementOccupancyByName(@PathVariable String name) {
        boolean decremented = facilityService.decrementOccupancyByName(name);
        
        if (decremented) {
            return ResponseEntity.ok("Occupancy decremented successfully");
        } else {
            return ResponseEntity.badRequest().body("Could not decrement occupancy. Facility may be empty or not found.");
        }
    }

    /**
     * Deletes a facility by ID.
     * @param id facility ID to delete
     * @return no content on success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacility(@PathVariable String id) {
        facilityService.deleteFacility(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes a facility by name.
     * @param name facility name to delete
     * @return no content on success
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteFacilityByName(@PathVariable String name) {
        facilityService.deleteFacilityByName(name);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all facilities owned by a specific owner.
     * @param owner owner's identifier
     * @return list of facilities owned by the specified owner
     */
    @GetMapping("/owner/{owner}")
    public ResponseEntity<List<Facility>> getFacilitiesByOwner(@PathVariable String owner) {
        List<Facility> facilities = facilityService.getFacilitiesByOwner(owner);
        return ResponseEntity.ok(facilities);
    }

    /**
     * Finds the 4 nearest facilities to given coordinates.
     * @param latitude location latitude
     * @param longitude location longitude
     * @param type optional facility type filter
     * @return list of up to 4 nearest facilities, optionally filtered by type
     */
    @GetMapping("/nearest")
    public ResponseEntity<List<Facility>> getNearestFacilities(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(required = false) FacilityType type) {
        List<Facility> nearestFacilities = facilityService.findNearestFacilities(latitude, longitude, 4, type);
        return ResponseEntity.ok(nearestFacilities);
    }
}