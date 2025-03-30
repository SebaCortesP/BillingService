package com.facturacion.facturacionapp.repositories;

import org.springframework.stereotype.Repository;

import com.facturacion.facturacionapp.exceptions.ResourceNotFoundException;
import com.facturacion.facturacionapp.models.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;


@Repository
public class BillingRepository {
    private static final Logger logger = Logger.getLogger(BillingRepository.class.getName());

    private final List<Client> clients = new ArrayList<>();
    private final List<VeterinaryService> services = new ArrayList<>();
    private final List<Bill> bills = new ArrayList<>();

    private long clientIdCounter = 1;
    private long serviceIdCounter = 1;
    private long billIdCounter = 1;

    public BillingRepository() {
        try {
            // Poblar services
            VeterinaryService consulta = new VeterinaryService(serviceIdCounter++, "Consulta", "Consulta médica con especialista", 15000);
            VeterinaryService examen = new VeterinaryService(serviceIdCounter++, "Examen", "Exámenes médicos", 25000);
            VeterinaryService cirugia = new VeterinaryService(serviceIdCounter++, "Cirugía", "Intervención quirúrgica", 40000);
            services.addAll(List.of(consulta, examen, cirugia));

            // Poblar clients
            Client seba = new Client(clientIdCounter++, "Seba Cortés", "Barrancas", "123-456-7890");
            Client nata = new Client(clientIdCounter++, "Nata Soto", "Llo Lleo", "987-654-3210");
            Client chris = new Client(clientIdCounter++, "Chris Gamboa", "Independencia", "555-666-7777");
            Client pipe = new Client(clientIdCounter++, "Pipe Guerrero", "Las Dunas", "111-222-3333");
            clients.addAll(List.of(seba, nata, chris, pipe));

            // Generar bills
            Bill bill1 = new Bill(billIdCounter++, seba);
            bill1.addDetail(new BillDetail(consulta, 1));
            
            Bill bill2 = new Bill(billIdCounter++, nata);
            bill2.addDetail(new BillDetail(consulta, 1));
            bill2.addDetail(new BillDetail(examen, 1));
            
            Bill bill3 = new Bill(billIdCounter++, chris);
            bill3.addDetail(new BillDetail(examen, 1));
            bill3.addDetail(new BillDetail(cirugia, 1));
            
            Bill bill4 = new Bill(billIdCounter++, pipe);
            bill4.addDetail(new BillDetail(consulta, 1));
            bill4.addDetail(new BillDetail(examen, 1));
            bill4.addDetail(new BillDetail(cirugia, 1));
            
            bills.addAll(List.of(bill1, bill2, bill3, bill4));

            logger.info("Clientes registrados: " + clients.size());
            logger.info("Facturas generadas: " + bills.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Client> listClients() {
        return new ArrayList<>(clients);
    }

    public List<VeterinaryService> listServices() {
        return new ArrayList<>(services);
    }

    public List<Bill> listBills() {
        return new ArrayList<>(bills);
    }

    public Client getClientById(Long id) {
        return clients.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Client with ID " + id + " not found"));
    }

    public VeterinaryService getServiceById(Long id) {
        return services.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("VeterinaryService with ID " + id + " not found"));
    }

    public Bill getBillById(Long id) {
        return bills.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Bill with ID " + id + " not found"));
    }

}

