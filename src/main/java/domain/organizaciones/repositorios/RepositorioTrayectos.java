package domain.organizaciones.repositorios;

import domain.organizaciones.Trayecto;

public class RepositorioTrayectos extends Repositorio<Trayecto> {
  private static final RepositorioTrayectos INSTANCE = new RepositorioTrayectos();

  public static RepositorioTrayectos instance(){
    return INSTANCE;
  }

  private RepositorioTrayectos(){
    super("Trayecto");
  }
}
