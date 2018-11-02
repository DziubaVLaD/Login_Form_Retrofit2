package com.sourcey.registration_form;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RequestInterface {

    @Headers("Accept: application/json")
    @POST("api/account/signup")
    Call<SignupResponseDTO> operation(@Body User request);

}
