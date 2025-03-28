package com.facturacion.facturacionapp.models;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VeterinaryService {
    private Long id;
    private String name;
    private String description;
    private Integer price;     

}
