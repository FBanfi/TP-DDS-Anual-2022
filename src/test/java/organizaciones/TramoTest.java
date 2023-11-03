package organizaciones;

import domain.mediciones.*;
import domain.organizaciones.Tramo;
import domain.organizaciones.Ubicacion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.services.distanciaAPI.Distancia;
import domain.services.distanciaAPI.GeoddsService;
import domain.services.distanciaAPI.DistanciaService;
import domain.transporte.MedioDeTransporte;
import domain.transporte.TransporteParticular;
import domain.transporte.TransportePublico;
import domain.transporte.Vehiculo;
import domain.transporte.enums.TipoVehiculo;

import static org.mockito.Mockito.*;


public class TramoTest {

  Vehiculo bondi1 = new Vehiculo(TipoVehiculo.COLECTIVO, crearConsumo(TipoConsumo.GASOIL));
  Vehiculo rayoMCQueen = new Vehiculo(TipoVehiculo.AUTO, crearConsumo(TipoConsumo.NAFTA));
  MedioDeTransporte bondiLinea114 = new TransportePublico(bondi1);
  TransporteParticular transporteParticular = new TransporteParticular(rayoMCQueen);
  DistanciaService servicioDistancia;


  Ubicacion origen = new Ubicacion("Pichincha 123", 3, 4);
  Ubicacion destino1 = new Ubicacion("Chapincha 321", 5, 6);
  Ubicacion destino2 = new Ubicacion("Calle Falsa 1234", 6, 7);

  @BeforeEach
  void initServicioDistancia() {
    servicioDistancia = mock(GeoddsService.class);
  }

  @Test
  public void sePuedeConocerLaDistanciaDeUnTramo() {
    Tramo tramo = new Tramo(transporteParticular, destino1, origen, servicioDistancia);
    tramo.getDistancia();
    when(servicioDistancia.obtenerDistancia(origen, destino1))
        .thenReturn(new Distancia("1", "km"));

    verify(servicioDistancia, times(1))
        .obtenerDistancia(origen, destino1);
  }

  @Test
  public void sePuedeDarAltaANuevoTramoConAtributosNoNulos() {

    Assertions.assertDoesNotThrow(() -> new Tramo(transporteParticular, origen, destino2, servicioDistancia));
  }

  @Test
  public void sePuedeConocerElDatoDeActividadDeUnTramo() {
    Tramo tramo = new Tramo(transporteParticular, destino1, origen, servicioDistancia);
    when(servicioDistancia.obtenerDistancia(origen, destino1)).thenReturn(new Distancia("2", "km"));

    Assertions.assertEquals(20, tramo.obtenerDA());
  }

  @Test
  public void sePuedeConocerLaHuellaDeCarbonoDeUnTramo() {
    Tramo tramo = new Tramo(transporteParticular, destino1, origen, servicioDistancia);
    when(servicioDistancia.obtenerDistancia(origen, destino1)).thenReturn(new Distancia("2", "km"));

    Assertions.assertEquals(20, tramo.obtenerHC());
  }

  private Consumo crearConsumo(TipoConsumo combustible) {
    return new Consumo(10, combustible, new Periodicidad(TipoPeriodicidad.MENSUAL, "01/2022"), new FactorEmision(Unidad.LT));
  }
}
