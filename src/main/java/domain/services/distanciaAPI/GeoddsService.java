package domain.services.distanciaAPI;

import domain.exceptions.ExcepcionRegistro;
import domain.organizaciones.Ubicacion;
import domain.utils.TokenPropertiesUtil;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.IOException;

@Entity
@DiscriminatorValue("Geodds")
public class GeoddsService extends DistanciaService{
  //private static GeoddsService instancia = null;
  @Column
  private static final String urlApi = "https://ddstpa.com.ar/api/";

  @Transient
  private final Retrofit retrofit;

  public GeoddsService() {
    this.retrofit = new Retrofit.Builder()
        .baseUrl(urlApi)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }
  @Override
  public Distancia obtenerDistancia(Ubicacion origen, Ubicacion destino) {

    try{
      Geodds distanciaService = this.retrofit.create(Geodds.class);
      Call<Distancia> requestDistancia = distanciaService.distancia(origen.getLocal(), origen.getCalle(),
          origen.getAltura(), destino.getLocal(), destino.getCalle(), destino.getAltura(),
          TokenPropertiesUtil.getString("t.distancia")
      );
      Response<Distancia> responseDistnacia = requestDistancia.execute();
      System.out.println("RESPONSE: " + responseDistnacia.code());
      return responseDistnacia.body();
    }catch (IOException e){
      throw new ExcepcionRegistro("Hubo un problema de conexion al servidor" + e);
    }

  }

  public TokenAPI obtenerToken(Email email) {

    try{
      Geodds apiDistanciaService = this.retrofit.create(Geodds.class);
      Call<TokenAPI> requestToken = apiDistanciaService.usuarios(email);
      Response<TokenAPI> responseToken = requestToken.execute();
      return responseToken.body();
    } catch (IOException e){
      throw new ExcepcionRegistro("Hubo un problema de conexion al servidor" );
    }
  }
}
