package domain.organizaciones.cuenta.validacioncontrasena;

import java.util.ArrayList;
import java.util.List;

public class ValidadorContrasenas {
  private List<ValidarContrasena> validadores = new ArrayList<>();

  public void validar(String contrasenia, String nombreUsuario) {
    getValidaciones().forEach(validador -> validador.validarAtributo(contrasenia));
  }

  public void agregarValidador(ValidarContrasena validador) {
    validadores.add(validador);
  }

  private List<ValidarContrasena> getValidaciones() {
    return validadores;
  }
}
