package com.facturacion.facturacionapp.models;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillDetail {
    private VeterinaryService service;
    private int quantity;
    private Integer subtotal;

    public BillDetail(VeterinaryService service, int quantity) {
        this.service = service;
        this.quantity = quantity;
        this.subtotal = service.getPrice() * quantity;
    }



    
}
