package modelos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DetalleCarro {
    private List<ItemCarro> items;
    // Definimos el IVA como una constante
    private final double IVA_RATE = 0.15; // 15% de IVA

    public DetalleCarro() {
        this.items = new ArrayList<>();
    }

    //Implementamos un mpetodo para agregar un producto al carro
    public void addItemCarro(ItemCarro itemCarro) {
        if (items.contains(itemCarro)) {
            Optional<ItemCarro> optionalItemCarro = items.stream()
                    .filter(i -> i.equals(itemCarro))
                    .findAny();
            if (optionalItemCarro.isPresent()) {
                ItemCarro i = optionalItemCarro.get();
                i.setCantidad(i.getCantidad() + 1);
            }
        } else {
            this.items.add(itemCarro);
        }
    }

    public List<ItemCarro> getItem() {
        return items;
    }

    /**
     * Devuelve el total de la compra sin incluir el IVA.
     * Es el total base (subtotales).
     */
    public double getTotal() {
        return items.stream().mapToDouble(ItemCarro::getSubtotal).sum();
    }

    /**
     * Calcula y devuelve el monto del IVA (15% del total base).
     */
    public double getIva() {
        return this.getTotal() * IVA_RATE;
    }

    /**
     * Devuelve el total final de la compra incluyendo el IVA.
     */
    public double getTotalConIva() {
        return this.getTotal() + this.getIva();
    }
}