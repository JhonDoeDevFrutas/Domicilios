package jhondoe.com.domicilios.common;

import jhondoe.com.domicilios.remote.APIService;
import jhondoe.com.domicilios.remote.RetrofitClient;

public class Common {

    public static final String UPDATE = "Actualizar";
    public static final String DELETE = "Eliminar";


    public static final String PHONE = "3136410475";

    public static String convertCodeToStatus(String code){
        if (code.equals("0"))
            return "Pedido Realizado";
        else if (code.equals("1"))
            return "En camino";
        else
            return "Enviado";
    }

    public static String NOTIFICATION;

    private static final String BASE_URL = "https://fcm.googleapis.com";

    public static APIService getFCMService(){
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
