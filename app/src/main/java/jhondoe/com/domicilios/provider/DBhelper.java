package jhondoe.com.domicilios.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {
    // Informacion de la Base de Datos
    static final String DB_NAME = "dbDomicilios";
    static final int DB_VERSION = 1; // Iniciar Version en 1

    public DBhelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // Estructura de Tablas
    private static final String CREAR_ORDEN = "create table " + OrdenCompraContract.ORDEN_COMPRA + " (" +
            OrdenCompraContract.ControladorOrdenCompra._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            OrdenCompraContract.ControladorOrdenCompra.ID_PRODUCTO + " TEXT," +
            OrdenCompraContract.ControladorOrdenCompra.NOMBRE_PRODUCTO + " TEXT," +
            OrdenCompraContract.ControladorOrdenCompra.CANTIDAD + " TEXT," +
            OrdenCompraContract.ControladorOrdenCompra.PRECIO + " TEXT," +
            OrdenCompraContract.ControladorOrdenCompra.DESCUENTO + " TEXT)";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_ORDEN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
