package com.rmeunier.servicepoller.rest;

import com.rmeunier.servicepoller.model.Service;
import com.rmeunier.servicepoller.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
/**
 * REST API endpoints for sending Service requests.
 */
public class ServiceRestController {

    @Autowired
    private ServiceService serviceService;

    @GetMapping("/users/services")
    public ResponseEntity<List<Service>> getAllServices() {
        List<Service> services = serviceService.getAllServices();

        if (services != null) {
            return ResponseEntity.accepted().body(services);
        }
        return ResponseEntity.unprocessableEntity().body(null);
    }

    @GetMapping("/users/{userId}/services")
    public ResponseEntity<List<Service>> getAllServicesByUserId(@PathVariable("userId") Long userId) {
        List<Service> services = serviceService.getAllServicesByUserId(userId);

        if (services != null) {
            return ResponseEntity.accepted().body(services);
        }
        return ResponseEntity.unprocessableEntity().body(null);
    }

    @PostMapping(value ="/users/{userId}/services/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Service> addService(@PathVariable("userId") Long userId, @RequestBody Service service) {
        Service s = serviceService.addService(userId, service);

        if (s != null) {
            return ResponseEntity.accepted().body(s);
        }
        return ResponseEntity.unprocessableEntity().body(null);
    }

    @PutMapping(value = "/users/services/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Service> updateService(@PathVariable("id") Long id, @RequestBody Service updatedService) {
        Service service = serviceService.updateService(id, updatedService);
        if (service != null) {
            return ResponseEntity.accepted().body(service);
        }
        return ResponseEntity.unprocessableEntity().body(null);
    }

    @DeleteMapping(value = "/users/services/{id}")
    public boolean deleteService(@PathVariable("id") Long id) {
        return serviceService.deleteService(id);
    }
}
