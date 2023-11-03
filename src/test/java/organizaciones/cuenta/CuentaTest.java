package organizaciones.cuenta;

import domain.organizaciones.cuenta.Cuenta;
import domain.organizaciones.cuenta.TipoCuenta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CuentaTest {


  @Test
  void sePuedeAltaCuentaAdmin() {

    Assertions.assertDoesNotThrow(()->crearCuenta("admin","abc2@ADannmf", TipoCuenta.AGENTE_SECTORIAL));
  }

  static Cuenta crearCuenta(String nombre, String password, TipoCuenta tipoCuenta) {
    return new Cuenta(nombre,password, tipoCuenta);
  }
}