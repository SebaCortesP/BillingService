package com.facturacion.facturacionapp.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.facturacion.facturacionapp.models.*;
import com.facturacion.facturacionapp.repositories.BillingRepository;
import com.facturacion.facturacionapp.exceptions.ResourceNotFoundException;

import java.util.*;

@Service
public class BillService {
    private final BillingRepository billingRepository;
    private final Map<Long, Bill> bills = new HashMap<>(); // Asegurar que exista
    public BillService(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    // getBills
    public List<Bill> getAllBills() {
        return billingRepository.listBills();
    }

    public List<Client> getAllClients() {
        return billingRepository.listClients();
    }
    public List<VeterinaryService> getAllServices() {
        return billingRepository.listServices();
    }
    
    // Obtener cliente por ID
    public Client getClientById(Long id) {
        Optional<Client> clientOptional = billingRepository.getClientById(id);
        return clientOptional.orElseThrow(() -> new RuntimeException("Client not found"));
    }
    
    public VeterinaryService getServiceById(Long id) {
        Optional<VeterinaryService> serviceOptional = billingRepository.getServiceById(id);
        return serviceOptional.orElseThrow(() -> new RuntimeException("Service not found"));
    }    

    // Obtener factura por ID
    public Bill getBillById(Long id) {
        Optional<Bill> billOptional = billingRepository.getBillById(id);
        return billOptional.orElseThrow(() -> new RuntimeException("Bill not found"));
    }
    
    public String payBill(Long billId) {
        Optional<Bill> billOptional = billingRepository.getBillById(billId);
        if (!billOptional.isPresent()) {
            throw new ResourceNotFoundException("Bill with ID " + billId + " not found");
        }
        Bill bill = billOptional.get();
        if (bill.isPayed()) {
            return "La factura ya está pagada.";
        }
        bill.pay(); // Cambiar el estado de la factura a "pagada"
        // billingRepository.save(bill); // Guardar la factura actualizada en la base de datos
        return "Factura pagada con éxito";
    }

    // public String getBillDetails(Long billId) {
    //     Optional<Bill> billOptional = billingRepository.getBillById(billId);
    
    //     // Verificar si la factura existe
    //     Bill bill = billOptional.orElseThrow(() -> new ResourceNotFoundException("Bill with ID " + billId + " not found."));
    
    //     StringBuilder details = new StringBuilder();
    //     double subtotal = 0.0;
    
    //     details.append("Factura ID: ").append(bill.getId()).append("\n");
    //     // details.append("Cliente: ").append(bill.getClient().getName()).append("\n");
    //     details.append("Detalles:\n");
    
    //     for (BillDetail detail : bill.getDetails()) {
    //         double value = detail.getService().getPrice() * detail.getQuantity();
    //         subtotal += value;
    
    //         details.append("- ").append(detail.getService().getName())
    //                 .append(": $").append(detail.getService().getPrice())
    //                 .append(" x ").append(detail.getQuantity())
    //                 .append(" = $").append(value).append("\n");
    //     }
    
    //     double iva = subtotal * 0.19;
    //     double total = subtotal + iva;
    
    //     details.append("Subtotal: $").append(subtotal).append("\n");
    //     details.append("IVA (19%): $").append(iva).append("\n");
    //     details.append("Total: $").append(total).append("\n");
    
    //     return details.toString();
    // }

    @GetMapping("/bill/{billId}")
    public ResponseEntity<Map<String, Object>> getBillDetails(@PathVariable Long billId) {
        Optional<Bill> billOptional = billingRepository.getBillById(billId);

        // Verificar si la factura existe
        Bill bill = billOptional.orElseThrow(() -> new ResourceNotFoundException("Bill with ID " + billId + " not found."));

        // Crear el JSON de respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("billId", bill.getId());
        // response.put("client", bill.getClient() != null ? bill.getClient().getName() : "No asignado");

        List<Map<String, Object>> detailsList = new ArrayList<>();
        double subtotal = 0.0;

        for (BillDetail detail : bill.getDetails()) {
            double value = detail.getService().getPrice() * detail.getQuantity();
            subtotal += value;

            Map<String, Object> detailMap = new HashMap<>();
            detailMap.put("service", detail.getService().getName());
            detailMap.put("price", detail.getService().getPrice());
            detailMap.put("quantity", detail.getQuantity());
            detailMap.put("value", value);

            detailsList.add(detailMap);
        }

        double iva = subtotal * 0.19;
        double total = subtotal + iva;

        response.put("details", detailsList);
        response.put("subtotal", subtotal);
        response.put("IVA_19%", iva);
        response.put("total", total);

        return ResponseEntity.ok(response);
    }

    

}


