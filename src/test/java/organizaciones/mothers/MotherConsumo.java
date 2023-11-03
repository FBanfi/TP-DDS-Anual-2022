package organizaciones.mothers;

import domain.mediciones.*;

public class MotherConsumo {
  public Consumo crearConsumo(TipoConsumo tipoConsumo) {
    return new Consumo(
        10,
        tipoConsumo,
        new Periodicidad(TipoPeriodicidad.MENSUAL, "01/2022"),
        new FactorEmision(Unidad.LT)
    );
  }

}
