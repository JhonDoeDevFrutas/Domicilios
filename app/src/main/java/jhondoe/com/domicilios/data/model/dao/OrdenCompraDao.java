package jhondoe.com.domicilios.data.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import jhondoe.com.domicilios.data.model.entities.OrdenCompra;
import jhondoe.com.domicilios.data.model.interfaces.IordenCompra;
import jhondoe.com.domicilios.provider.DBhelper;
import jhondoe.com.domicilios.provider.OrdenCompraContract;

public class OrdenCompraDao implements IordenCompra{
    private DBhelper dBhelper;
    private Context ourcontext;
    private SQLiteDatabase database;

    public OrdenCompraDao(Context context){
        ourcontext = context;
    }

    public OrdenCompraDao abrirBaseDatos(){
        dBhelper = new DBhelper(ourcontext);        // Creamos una instancia de la clase DBhelper
        database = dBhelper.getWritableDatabase();  // Abrimos la base de datos para escritura
        return this;
    }

    // Cerrar base de datos
    public void cerrar(){
        dBhelper.close();
    }


    @Override
    public List<OrdenCompra> getCarts() {
        List<OrdenCompra> ordenList = new ArrayList<>();

        Cursor crOrders = database.rawQuery("select _id, id_prdcto, nmbre_prdcto, cntdd, prco, dscnto from tbOrdenCompra", null);

        if (crOrders != null && crOrders.getCount() > 0){
            while (crOrders.moveToNext()){
                OrdenCompra ordenCompraTmp = new OrdenCompra();

                ordenCompraTmp.setProductId(crOrders.getString(1));
                ordenCompraTmp.setProductName(crOrders.getString(2));
                ordenCompraTmp.setQuantity(crOrders.getString(3));
                ordenCompraTmp.setPrice(crOrders.getString(4));
                ordenCompraTmp.setDiscount(crOrders.getString(5));

                ordenList.add(ordenCompraTmp);
            }
        }

        return ordenList;
    }

    @Override
    public boolean addToCard(OrdenCompra ordenCompra) {
        ContentValues newRecord = new ContentValues();

        newRecord.put(OrdenCompraContract.ControladorOrdenCompra.ID_PRODUCTO, ordenCompra.getProductId());
        newRecord.put(OrdenCompraContract.ControladorOrdenCompra.NOMBRE_PRODUCTO, ordenCompra.getProductName());
        newRecord.put(OrdenCompraContract.ControladorOrdenCompra.CANTIDAD, ordenCompra.getQuantity());
        newRecord.put(OrdenCompraContract.ControladorOrdenCompra.PRECIO, ordenCompra.getPrice());
        newRecord.put(OrdenCompraContract.ControladorOrdenCompra.DESCUENTO, ordenCompra.getDiscount());

        boolean insert = (database.insert(OrdenCompraContract.ORDEN_COMPRA, null, newRecord) > 0);
        return insert;

    }

    @Override
    public void cleanCart() {
        database.delete(OrdenCompraContract.ORDEN_COMPRA, null, null);
    }
}
