package domain.organizaciones.repositorios;

import domain.mediciones.DetalleReporte;

public class RepositorioDetalleReporte extends Repositorio<DetalleReporte> {
  private static final RepositorioDetalleReporte INSTANCE = new RepositorioDetalleReporte();

  public static RepositorioDetalleReporte instance(){
    return INSTANCE;
  }

  private RepositorioDetalleReporte(){
    super("DetalleReporte");
  }
}
