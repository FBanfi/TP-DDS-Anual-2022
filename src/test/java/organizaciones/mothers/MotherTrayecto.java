package organizaciones.mothers;

import domain.mediciones.TipoConsumo;
import domain.organizaciones.Tramo;
import domain.organizaciones.Trayecto;
import domain.organizaciones.Ubicacion;
import domain.services.distanciaAPI.Distancia;
import domain.services.distanciaAPI.DistanciaService;
import domain.transporte.MedioDeTransporte;
import domain.transporte.TransportePublico;
import domain.transporte.TransporteServicioContratado;
import domain.transporte.Vehiculo;
import domain.transporte.enums.TipoVehiculo;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class MotherTrayecto {

  public Trayecto crearTrayectoCompartido(DistanciaService distanciaService) {
    List<Tramo> tramos = new ArrayList<>();
    Vehiculo vehiculo = new Vehiculo(TipoVehiculo.AUTO, new MotherConsumo().crearConsumo(TipoConsumo.NAFTA));
    MedioDeTransporte transportePublico = new TransportePublico(vehiculo);
    tramos.add(new Tramo(new TransporteServicioContratado(vehiculo),new Ubicacion("calle",1,2),new Ubicacion("calle",2,3), distanciaService));

    Mockito.when(distanciaService.obtenerDistancia(tramos.get(0).getOrigen(), tramos.get(0).getDestino()))
        .thenReturn(new Distancia("1", "km"));
    Trayecto trayecto = new Trayecto(tramos);
    return trayecto;
  }

  public Trayecto crearTrayectoNoCompartido(DistanciaService distanciaService) {
    List<Tramo> tramos1 = new ArrayList<>();
    Trayecto trayecto;


    Vehiculo vehiculo = new Vehiculo(TipoVehiculo.AUTO, new MotherConsumo().crearConsumo(TipoConsumo.NAFTA));
    MedioDeTransporte transportePublico = new TransportePublico(vehiculo);
    tramos1.add(new Tramo(new TransportePublico(vehiculo), new Ubicacion("calle",1,2), new Ubicacion("calle",2,3), distanciaService));

    Mockito.when(distanciaService.obtenerDistancia(tramos1.get(0).getOrigen(), tramos1.get(0).getDestino()))
        .thenReturn(new Distancia("1", "km"));

    Trayecto trayectoNoCompartido = new Trayecto(tramos1);

    return trayectoNoCompartido;
  }

  public Trayecto crearTrayectoConNombre(DistanciaService distanciaService, String nombre) {
    List<Tramo> tramos1 = new ArrayList<>();

    Vehiculo vehiculo = new Vehiculo(TipoVehiculo.AUTO, new MotherConsumo().crearConsumo(TipoConsumo.NAFTA));
    MedioDeTransporte transportePublico = new TransportePublico(vehiculo, "Auteto de Juanceto");
    tramos1.add(new Tramo(transportePublico, new Ubicacion("calle",1,2), new Ubicacion("calle",2,3), distanciaService));

    Trayecto trayectoNoCompartido = new Trayecto(tramos1, nombre);

    return trayectoNoCompartido;
  }

}
