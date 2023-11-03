package organizaciones;

import domain.mediciones.*;
import domain.organizaciones.Tramo;
import domain.organizaciones.Trayecto;
import domain.organizaciones.Ubicacion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.services.distanciaAPI.Distancia;
import domain.services.distanciaAPI.DistanciaService;
import domain.services.distanciaAPI.GeoddsService;
import domain.transporte.MedioDeTransporte;
import domain.transporte.TransporteParticular;
import domain.transporte.TransportePublico;
import domain.transporte.Vehiculo;
import domain.transporte.enums.TipoVehiculo;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrayectoTest {

  Vehiculo bondi1 = new Vehiculo(TipoVehiculo.COLECTIVO, crearConsumo(TipoConsumo.NAFTA));
  Vehiculo rayoMCQueen = new Vehiculo(TipoVehiculo.AUTO, crearConsumo(TipoConsumo.GASOIL));
  MedioDeTransporte bondiLinea114 = new TransportePublico(bondi1);
  TransporteParticular transporteParticular = new TransporteParticular(rayoMCQueen);

  Ubicacion origen = new Ubicacion("Pichincha 123", 5, 8);
  Ubicacion destino1 = new Ubicacion("Chapincha 321", 6, 8);
  Ubicacion destino2 = new Ubicacion("Calle Falsa 1234", 8, 9);
  Tramo tramo1 = new Tramo(transporteParticular, origen, destino1, new GeoddsService());
  Tramo tramo2 = new Tramo(transporteParticular, origen, destino2, new GeoddsService() {
  });
  DistanciaService servicioDistancia;

  @BeforeEach
  void initServicioDistancia() {
    servicioDistancia = mock(GeoddsService.class);
  }

  @Test
  public void sePuedenAgregarTramosAUnTrayecto(){

    List<Tramo> listaDeDosTramos = new ArrayList<>();
    listaDeDosTramos.add(tramo1);
    listaDeDosTramos.add(tramo2);
    Trayecto trayecto = new Trayecto(listaDeDosTramos);

    Assertions.assertEquals(trayecto.getTramos(), listaDeDosTramos);

  }
 @Test
 public void sePuedeConocerLaHuellaDeCarbonoDeUnTrayecto(){
   List<Tramo> listaDeDosTramos = new ArrayList<>();

   Tramo tramo = new Tramo(transporteParticular, destino1, origen, servicioDistancia);
   Tramo tramo1 = new Tramo(transporteParticular, destino1, origen, servicioDistancia);
   listaDeDosTramos.add(tramo);
   listaDeDosTramos.add(tramo1);

   Trayecto trayecto = new Trayecto(listaDeDosTramos);

   when(servicioDistancia.obtenerDistancia(origen, destino1)).thenReturn(new Distancia("2", "km"));

   Assertions.assertEquals(40, trayecto.obtenerHC());
 }

  private Consumo crearConsumo(TipoConsumo combustible){
    return new Consumo(10, combustible, new Periodicidad(TipoPeriodicidad.MENSUAL, "01/2022"), new FactorEmision(Unidad.LT));
  }
}