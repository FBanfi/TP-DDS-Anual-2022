
package organizaciones;

import domain.mediciones.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import domain.transporte.TransportePublico;
import domain.transporte.Vehiculo;
import domain.transporte.enums.TipoVehiculo;

public class TransportePublicoTest {

  @Test
  private void sePuedeDarDeAltaTransportePublico() {
    Assertions.assertDoesNotThrow(()-> obtenerTransporte());
  }

  @Test
  private void sePuedeDarDeAltaPardasAlTransportePublico() {}

  private TransportePublico obtenerTransporte() {
    Vehiculo vehiculo = new Vehiculo(TipoVehiculo.COLECTIVO, crearConsumo(TipoConsumo.NAFTA));
    return new TransportePublico(vehiculo);
  }


  private Consumo crearConsumo(TipoConsumo combustible){
    return new Consumo(10, combustible, new Periodicidad(TipoPeriodicidad.MENSUAL, "01/2022"), new FactorEmision(Unidad.M3));
  }
}
