package jhondoe.com.domicilios.provider;

import android.provider.BaseColumns;

import java.util.UUID;

public class OrdenCompraContract {
    /**
     * Representación de la tabla a consultar
     */
    public static final String ORDEN_COMPRA = "tbOrdenCompra";

    interface Columnas{
        String ID_PRODUCTO = "id_prdcto";
        String NOMBRE_PRODUCTO = "nmbre_prdcto";
        String CANTIDAD = "cntdd";
        String PRECIO = "prco";
        String DESCUENTO = "dscnto";
    }

    /**
     * Controlador de la tabla
     * */
    public static class ControladorOrdenCompra implements BaseColumns, Columnas{

        public static String generarId(){
            return "C-" + UUID.randomUUID().toString();
        }
    }

}
