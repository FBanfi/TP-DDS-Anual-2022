package domain.organizaciones.repositorios;

import domain.organizaciones.SolicitudDeVinculo;

public class RepositorioVinculaciones extends Repositorio<SolicitudDeVinculo> {
  private static final RepositorioVinculaciones INSTANCE = new RepositorioVinculaciones();

  public static RepositorioVinculaciones instance(){
    return INSTANCE;
  }

  private RepositorioVinculaciones(){
    super("SolicitudDeVinculo");
  }
}
