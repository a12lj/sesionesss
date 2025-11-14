package services;
/*
    * Implementación de la interfaz ProductoServices
 * */
import modelos.Producto;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ProductosServicesImplement implements ProductoServices {

    @Override
    public List<Producto> listar() {
        // Implementación con datos estáticos
        return Arrays.asList(new Producto(1L, "Laptop ASUS", "computacion", 1500.0),
                new Producto(2L, "Smartphone Samsung", "telefonia", 800.0),
                new Producto(3L, "Tablet Apple", "tablet", 1200.0),
                new Producto(4L, "Monitor LG", "computacion", 300.0));
    }
    @Override
    public Optional<Producto> porId(Long id) {
        return listar().stream().filter(p -> p.getId().equals(id)).findAny();
    }
}
