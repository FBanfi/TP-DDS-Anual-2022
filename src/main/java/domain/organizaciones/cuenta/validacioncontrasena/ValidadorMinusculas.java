package domain.organizaciones.cuenta.validacioncontrasena;

import domain.exceptions.ExcepcionContrasenia;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidadorMinusculas implements ValidarContrasena {
  @Override
  public void validarAtributo(String contrasena) {
    Pattern pattern = Pattern.compile("^(?=.*[a-z])", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(contrasena);
    boolean matchFound = matcher.find();
    if (!matchFound) {
      throw new ExcepcionContrasenia("La contrasena debe tener al menos un caracter en minusculas");
    }
  }
}
