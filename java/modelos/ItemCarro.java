package modelos;

import java.util.Objects;

public class ItemCarro {
    private int cantidad;
    private Producto producto;

    public ItemCarro(int cantidad, Producto producto) {
        this.cantidad = cantidad;
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    /* Creamos un metodo para comparar si ya un producto está en la lista del carrito
     * de compras y no repetirlo */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemCarro itemCarro = (ItemCarro) o;

        return Objects.equals(producto.getId(), itemCarro.producto.getId())
                && Objects.equals(cantidad, itemCarro.cantidad);
    }

    // Calculamos el subtotal sin IVA
    public double getSubtotal() {
        return cantidad * producto.getPrecio();
    }

    /* Método para calcular el IVA de este producto
    public double getIva() {
        return getSubtotal() * 0.15; // IVA del 15%
    }*/

    /* Método para calcular el subtotal con IVA
    public double getSubtotalConIva() {
        return getSubtotal() + getIva();
    }*/
}

