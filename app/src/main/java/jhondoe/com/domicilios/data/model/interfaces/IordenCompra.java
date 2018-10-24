package jhondoe.com.domicilios.data.model.interfaces;

import java.util.List;

import jhondoe.com.domicilios.data.model.entities.OrdenCompra;

public interface IordenCompra {
    // Funciones Basicas
    public List<OrdenCompra> getCarts();
    public boolean addToCard(OrdenCompra ordenCompra);
    public void cleanCart();

}
