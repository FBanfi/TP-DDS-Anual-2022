package organizaciones.mothers;

import domain.organizaciones.Parada;
import domain.organizaciones.Ubicacion;
import domain.services.distanciaAPI.DistanciaService;
import domain.services.distanciaAPI.GeoddsService;
import org.mockito.Mockito;

public class MotherParada {
  private String nombre;
  private Ubicacion origen;
  private Parada destino;
  private DistanciaService distanciaService = Mockito.mock(GeoddsService.class);

  public MotherParada(String nombre, Ubicacion origen, Parada destino) {
    this.nombre = nombre;
    this.origen = origen;
    this.destino = destino;
  }

  public MotherParada() {
  }

  public Parada crearParadaSinDestino(){
    Parada parada = new Parada(distanciaService, "villa lugano", new Ubicacion("mozart", 0 , 3100));

    return parada;
  }

  public Parada crearParadaConUnaParadaAdentro(){
    Parada parada2 = new Parada(distanciaService, "villa lugano", new Ubicacion("mozart", 0 , 3100));

    return new Parada(distanciaService, "utn", new Ubicacion("mozart", 0 , 2300), parada2);
  }

  public Parada crearParadaCon3ParadasAdentro(){
    Parada parada3 = new Parada(distanciaService, "villa lugano", new Ubicacion("mozart", 0 , 3100));
    Parada parada2 = new Parada(distanciaService, "lugano",new Ubicacion("mozart", 0 , 2700), parada3);
    Parada parada1 = new Parada(distanciaService, "utn", new Ubicacion("mozart", 0 , 2300), parada2);

    return parada1;
  }

  public Parada crearParadaCon3ParadasAdentroQueTerminaDondeEmpieza(){
    Parada parada3 = new Parada(distanciaService, "villa lugano", new Ubicacion("mozart", 0 , 3100));
    Parada parada2 = new Parada(distanciaService, "lugano",new Ubicacion("mozart", 0 , 2700), parada3);
    Parada parada1 = new Parada(distanciaService, "utn", new Ubicacion("mozart", 0 , 2300), parada2);
    parada3.setParada(parada1);

    return parada1;
  }
}
