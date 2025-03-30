package com.facturacion.facturacionapp.controllers;
import com.facturacion.facturacionapp.responses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.facturacion.facturacionapp.exceptions.ResourceNotFoundException;
import com.facturacion.facturacionapp.models.*;
import com.facturacion.facturacionapp.services.BillService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/billing")
public class BillingController {
    private final BillService billService;

    public BillingController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping("/bills")
    public List<Bill> getBills() {
        return billService.getAllBills();
    }

    @GetMapping("/clients")
    public List<Client> getClients() {
        return billService.getAllClients();
    }
    @GetMapping("/services")
    public List<VeterinaryService> getServices() {
        return billService.getAllServices();
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<?> getClientById(@PathVariable("id") Long id) {
        try {
            Client client = billService.getClientById(id);
            return new ResponseEntity<>(client, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND); // Devuelve 404 con el mensaje de error
        }
    }
    
    @GetMapping("/services/{id}")
    public ResponseEntity<?> getServiceById(@PathVariable("id") Long id) {
        try {
            VeterinaryService service = billService.getServiceById(id);
            return new ResponseEntity<>(service, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND); // Devuelve 404 con el mensaje de error
        }
    }

    @GetMapping("/bills/{id}")
    public ResponseEntity<?> getBillById(@PathVariable("id") Long id) {
        try {
            Bill bill = billService.getBillById(id);
            return new ResponseEntity<>(bill, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND); // Devuelve 404 con el mensaje de error
        }
    }

    @GetMapping("/pay/{billId}")
    public ResponseEntity<String> payBill(@PathVariable("billId") Long billId) {
        try {
            String response = billService.payBill(billId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/details/{billId}")
    public ResponseEntity<?> getBillDetails(@PathVariable("billId") Long billId) {
        try {
            Map<String, Object> details = billService.getBillDetails(billId);
            return ResponseEntity.ok(details);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
