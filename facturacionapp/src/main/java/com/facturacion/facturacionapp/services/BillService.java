package com.facturacion.facturacionapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.facturacion.facturacionapp.exceptions.ResourceNotFoundException;
import com.facturacion.facturacionapp.models.*;
import com.facturacion.facturacionapp.repositories.BillingRepository;

import java.util.*;

@Service
public class BillService {

    private final BillingRepository billingRepository;

    @Autowired
    public BillService(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    public List<Bill> getAllBills() {
        return billingRepository.listBills();
    }

    public List<Client> getAllClients() {
        return billingRepository.listClients();
    }

    public List<VeterinaryService> getAllServices() {
        return billingRepository.listServices();
    }

    public Bill getBillById(Long id) {
        return billingRepository.getBillById(id);
    }

    public Client getClientById(Long id) {
        return billingRepository.getClientById(id);
    }
    
    public VeterinaryService getServiceById(Long id) {
        return billingRepository.getServiceById(id);
    }

    public String payBill(Long billId) {
        Bill bill = getBillById(billId);

        if (bill.isPayed()) {
            return "La factura ya está pagada.";
        }

        bill.pay();
        return "Factura pagada con éxito.";
    }

    public Map<String, Object> getBillDetails(Long billId) {
        Bill bill = billingRepository.getBillById(billId);

        Map<String, Object> response = new HashMap<>();
        response.put("billId", bill.getId());

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

        return response;
    }


    

}


