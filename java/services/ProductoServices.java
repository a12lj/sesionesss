package services;
/*
    * Interfaz ProductoServices
 * */

import modelos.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoServices {
    List<Producto> listar();

    Optional<Producto> porId(Long id);
}
