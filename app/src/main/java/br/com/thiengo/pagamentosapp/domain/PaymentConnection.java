package br.com.thiengo.pagamentosapp.domain;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by viniciusthiengo on 9/11/16.
 */

public interface PaymentConnection {
    @FormUrlEncoded
    @POST("package/ctrl/CtrlPayment.php")
    public Call<String> sendPayment(
            @Field("product_id") String id,
            @Field("value") double value,
            @Field("token") String token,
            @Field("parcels") int parcels
    );
}
