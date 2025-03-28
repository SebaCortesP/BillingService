package com.facturacion.facturacionapp.models;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill {
    private Long id;
    private Client cliente;
    private Double total = 0.0;
    private boolean payed = false;
    private List<BillDetail> details = new ArrayList<>();
    
    public Bill(Long id, Client cliente) {
        this.id = id;
        this.cliente = cliente;
        this.details = new ArrayList<>();
    }

    public void addDetail(BillDetail detail) {
        this.details.add(detail);
        this.total += detail.getSubtotal();
    }

    public void pay() { this.payed = true; }
}
