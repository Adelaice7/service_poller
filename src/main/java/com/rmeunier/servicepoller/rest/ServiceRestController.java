package com.rmeunier.servicepoller.rest;

import com.rmeunier.servicepoller.model.Service;
import com.rmeunier.servicepoller.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/users/{userId}/services/add")
    public ResponseEntity<Service> addService(@PathVariable("userId") Long userId, @RequestBody Service service) {
        Service s = serviceService.addService(service, userId);

        if (s != null) {
            return ResponseEntity.accepted().body(s);
        }
        return ResponseEntity.unprocessableEntity().body(null);
    }

    @PutMapping("/users/services/{id}")
    public ResponseEntity<Service> updateService(@PathVariable("id") Long id, @RequestBody Service updatedService) {
        Service service = serviceService.updateService(id, updatedService);
        if (service != null) {
            return ResponseEntity.accepted().body(service);
        }
        return ResponseEntity.unprocessableEntity().body(null);
    }

    @DeleteMapping("/users/services/{id}")
    public boolean deleteService(@PathVariable("id") Long id) {
        return serviceService.deleteService(id);
    }
}
