package domain.services.distanciaAPI;

import retrofit2.Call;
import retrofit2.http.*;

public interface Geodds {

  @GET("distancia")
  Call<Distancia> distancia(@Query("localidadOrigenId") int localOrig, @Query("calleOrigen") String calleOrig, @Query("alturaOrigen") int alturaOrig,
                            @Query("localidadDestinoId") int localDest, @Query("calleDestino") String calleDest, @Query("alturaDestino") int alturaDest,
                            @Header("Authorization") String token);

  @POST("user")
  Call<TokenAPI> usuarios(@Body Email email);

}

// Token que genere: "Bearer /KDXFCtRb+vel5alndLjLqg3svzGdyFQUEx6CuMAHZw="