package domain.organizaciones.repositorios;

import domain.mediciones.Medicion;

public class RepositorioMediciones extends Repositorio<Medicion> {
  private static final RepositorioMediciones INSTANCE = new RepositorioMediciones();

  public static RepositorioMediciones instance(){
    return INSTANCE;
  }

  private RepositorioMediciones(){
    super("Medicion");
  }
}
