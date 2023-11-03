package domain.organizaciones.cuenta.validacioncontrasena;

import domain.exceptions.ExcepcionContrasenia;


public class ValidadorLongitud implements ValidarContrasena {
  @Override
  public void validarAtributo(String contrasena) {
    if (contrasena.length() < 8) {
      throw new ExcepcionContrasenia("La contrasena debe"
              + " tener al menos una longitud de 8 caracteres");
    }
  }
}

