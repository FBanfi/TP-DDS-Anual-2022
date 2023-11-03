package organizaciones.cuenta.validacioncontrasena;

import domain.organizaciones.cuenta.validacioncontrasena.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import domain.exceptions.ExcepcionContrasenia;

class ValidadorContrasenasTest {

  @Test
  void ContrasenaNoPasaValidadorNumero() {
    String contrasena = "abcd";
    ValidarContrasena validador = new ValidadorNumero();
    Assertions.assertThrows(ExcepcionContrasenia.class,() -> {validador.validarAtributo(contrasena);});
  }

  @Test
  void ContrasenaPasaValidadorNumero() {
    String contrasena = "ab2cd";
    ValidarContrasena validador = new ValidadorNumero();
    Assertions.assertDoesNotThrow(() -> {validador.validarAtributo(contrasena);});
  }

  @Test
  void ContrasenaEstaEnTop10000MasUsadas() {
    String contrasena = "password";
    ValidarContrasena validador = new ValidadorListaContrasenas();
    Assertions.assertThrows(ExcepcionContrasenia.class,() -> {validador.validarAtributo(contrasena);});
  }

  @Test
  void ContrasenaNoEstaEnTop10000MasUsadas() {
    String contrasena = "abcd2";
    ValidarContrasena validador = new ValidadorNumero();
    Assertions.assertDoesNotThrow(() -> {validador.validarAtributo(contrasena);});
  }

  @Test
  void ContraSenaPasaValidaciones() {
    ValidadorContrasenas validadorContrasenas = new ValidadorContrasenas();
    validadorContrasenas.agregarValidador(new ValidadorCaracterEspecial());
    validadorContrasenas.agregarValidador(new ValidadorLongitud());
    validadorContrasenas.agregarValidador(new ValidadorMayusculas());
    validadorContrasenas.agregarValidador(new ValidadorMinusculas());
    validadorContrasenas.agregarValidador(new ValidadorNumero());
    validadorContrasenas.agregarValidador(new ValidadorListaContrasenas());
    Assertions.assertDoesNotThrow(() -> {validadorContrasenas.validar("abc2@ADannmf", "");});
  }
  @Test
  void agregarValidador() {

  }
}