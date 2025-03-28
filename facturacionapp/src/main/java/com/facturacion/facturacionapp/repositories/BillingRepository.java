package com.facturacion.facturacionapp.repositories;

import org.springframework.stereotype.Repository;
import com.facturacion.facturacionapp.models.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;


@Repository
public class BillingRepository {
    private static final Logger logger = Logger.getLogger(BillingRepository.class.getName());

    private final Map<Long, Client> clients = new ConcurrentHashMap<>();
    private final Map<Long, VeterinaryService> services = new ConcurrentHashMap<>();
    private final Map<Long, Bill> bills = new ConcurrentHashMap<>();

    private long clientIdCounter = 1;
    private long serviceIdCounter = 1;
    private long billIdCounter = 1;

    public BillingRepository() {
        // Inicializa los datos
        populateData();
    }

    // Método para poblar datos de prueba
    public void populateData() {
        logger.info("Ejecutando populateData...");

        // Poblar servicios
        populateServices();

        // Poblar clientes
        populateClients();

        // Generar facturas para cada cliente
        generateBills();

        logger.info("Clientes registrados: " + clients.size());
        logger.info("Facturas generadas: " + bills.size());
    }

    private void populateServices() {
        addService("Consulta", "Consulta médica con especialista", 15000);
        addService("Examen", "Exámenes médicos", 25000);
        addService("Cirugía", "Intervención quirúrgica", 40000);
    }

    private void addService(String name, String description, int price) {
        VeterinaryService service = new VeterinaryService(serviceIdCounter++, name, description, price);
        services.put(service.getId(), service);
    }

    private void populateClients() {
        addClient("Seba Cortés", "Barrancas", "123-456-7890");
        addClient("Nata Soto", "Llo Lleo", "987-654-3210");
        addClient("Chris Gamboa", "Independencia", "555-666-7777");
        addClient("Pipe Guerrero", "Las Dunas", "111-222-3333");
    }

    private void addClient(String name, String address, String phone) {
        Client client = new Client(clientIdCounter++, name, address, phone);
        clients.put(client.getId(), client);
    }

    private void generateBills() {
        associateBillsToClient(1L, List.of(1L));
        associateBillsToClient(2L, List.of(1L, 2L));
        associateBillsToClient(3L, List.of(2L, 3L));
        associateBillsToClient(4L, List.of(1L, 2L, 3L));
    }

    // private void associateBillsToClient(Long clientId, List<Long> serviceIds) {
    //     Client client = clients.get(clientId);
    //     if (client == null) {
    //         logger.warning("Client with ID " + clientId + " not found.");
    //         return;
    //     }

    //     for (Long serviceId : serviceIds) {
    //         VeterinaryService service = services.get(serviceId);
    //         if (service == null) {
    //             logger.warning("Service with id " + serviceId + " not found.");
    //             continue;
    //         }

    //         Bill bill = new Bill(billIdCounter++, client);
    //         bill.addDetail(new BillDetail(service, 1));
    //         bills.put(bill.getId(), bill);
    //     }
    // }
    private void associateBillsToClient(Long clientId, List<Long> serviceIds) {
        Client client = clients.get(clientId);
        if (client == null) {
            logger.warning("Client with ID " + clientId + " not found.");
            return;
        }
    
        // Crear una única factura para el cliente
        Bill bill = new Bill(billIdCounter++, client);
    
        for (Long serviceId : serviceIds) {
            VeterinaryService service = services.get(serviceId);
            if (service == null) {
                logger.warning("Service with ID " + serviceId + " not found.");
                continue;
            }
    
            // Agregar cada servicio a los detalles de la factura
            bill.addDetail(new BillDetail(service, 1));
        }
    
        // Guardar la factura con todos los servicios agregados
        bills.put(bill.getId(), bill);
    }
    

    // Listar clients
    public List<Client> listClients() {
        return new ArrayList<>(clients.values());
    }
    // Listar Services
    public List<VeterinaryService> listServices() {
        return new ArrayList<>(services.values());
    }

    // Listar bills
    public List<Bill> listBills() {
        return new ArrayList<>(bills.values());
    }

    // Obtener client por ID
    public Optional<Client> getClientById(Long id) {
        return Optional.ofNullable(clients.get(id));
    }
    // Obtener service por ID
    public Optional<VeterinaryService> getServiceById(Long id) {
        return Optional.ofNullable(services.get(id));
    }

    // Obtener bill por ID
    public Optional<Bill> getBillById(Long id) {
        return Optional.ofNullable(bills.get(id));
    }
}
