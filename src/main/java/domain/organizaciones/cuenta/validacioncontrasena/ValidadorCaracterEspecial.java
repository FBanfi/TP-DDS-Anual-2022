package domain.organizaciones.cuenta.validacioncontrasena;

import domain.exceptions.ExcepcionContrasenia;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidadorCaracterEspecial implements ValidarContrasena {
  @Override
  public void validarAtributo(String contrasena) {
    Pattern pattern = Pattern.compile("^(?=.*[^A-Za-z0-9])", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(contrasena);
    boolean matchFound = matcher.find();
    if (!matchFound) {
      throw new ExcepcionContrasenia("La contrasena debe contener al menos un caracter especial");
    }
  }
}
