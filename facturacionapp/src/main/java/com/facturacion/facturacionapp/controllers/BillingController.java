package com.facturacion.facturacionapp.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.facturacion.facturacionapp.exceptions.ResourceNotFoundException;
import com.facturacion.facturacionapp.models.*;
import com.facturacion.facturacionapp.services.BillService;

import java.util.List;

@RestController
@RequestMapping("/billing")
public class BillingController {
    private final BillService billService;

    public BillingController(BillService billService) {
        this.billService = billService;
    }

    // Endpoint para listar todas las facturas
    @GetMapping("/bills")
    public List<Bill> getBills() {
        return billService.getAllBills();
    }

    // Nuevo endpoint para obtener todos los clientes
    @GetMapping("/clients")
    public List<Client> getClients() {
        return billService.getAllClients();
    }
    @GetMapping("/services")
    public List<VeterinaryService> getServices() {
        return billService.getAllServices();
    }

    @GetMapping("/clients/{id}")
    public Client getClientById(@PathVariable("id") Long id) {
        return billService.getClientById(id);
    }
    @GetMapping("/services/{id}")
    public VeterinaryService getServiceById(@PathVariable("id") Long id) {
        return billService.getServiceById(id);
    }

    // Endpoint para obtener una factura por ID
    @GetMapping("/bills/{id}")
    public Bill getBillById(@PathVariable("id") Long id) {
        return billService.getBillById(id);
    }

    @GetMapping("/pay/{billId}")
    public ResponseEntity<String> payBill(@PathVariable("billId") Long billId) {
        try {
            // Llamamos al servicio para pagar la factura
            String response = billService.payBill(billId);
            return new ResponseEntity<>(response, HttpStatus.OK); // 200 OK si la factura fue pagada
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404 si la factura no se encuentra
        }
    }
    // getDetails
    @GetMapping("/details/{billId}")
    public ResponseEntity<?> getBillDetails(@PathVariable("billId") Long billId) {
        try {
            ResponseEntity details = billService.getBillDetails(billId);
            return ResponseEntity.ok(details);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
