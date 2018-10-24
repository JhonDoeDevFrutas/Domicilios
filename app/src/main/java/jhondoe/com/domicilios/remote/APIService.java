package jhondoe.com.domicilios.remote;

import jhondoe.com.domicilios.data.model.entities.MyResponse;
import jhondoe.com.domicilios.data.model.entities.Sender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type: Application/json",
                    "Authorization:key=AAAAcSEqyE8:APA91bEZq9Zxdp-5KVQqYpgycLRmudRqVmkahQdBgck92QrF-KbAZNt18THvDdK7agwJAd2kaj4yo_O7KnnouIRQU4vjoF9blBD6F4cM3APYTwi0Xoio8KFwvpbHKTL9RbQeTCf4qoyOxzTpksZfdrAB6Q898H2ApA"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);


}
