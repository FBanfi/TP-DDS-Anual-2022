package domain.organizaciones.cuenta.validacioncontrasena;

import domain.exceptions.ExcepcionContrasenia;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ValidadorListaContrasenas implements ValidarContrasena {
  private List<String> listaContrasenas = leerArchivo();

  public List<String> leerArchivo() {
    Path pathArchivo = Paths.get(
        "src/main/java/domain/organizaciones/cuenta/validacioncontrasena/passwordlist.txt");

    try {
      return Files.readAllLines(pathArchivo, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new ExcepcionContrasenia("No se pudo abrir el archivo de contrasenas faciles");
    }

  }

  @Override
  public void validarAtributo(String contrasena) {
    if (this.listaContrasenas.contains(contrasena)) {
      throw new ExcepcionContrasenia("La contrasena no es segura, "
              + "esta entre las 10.000 mas usadas.");
    }
  }
}
