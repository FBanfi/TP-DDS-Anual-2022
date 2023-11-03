package domain.organizaciones.repositorios;

import domain.mediciones.TipoConsumo;

public class RepositorioTipoConsumo extends Repositorio<TipoConsumo> {
  private static final RepositorioTipoConsumo INSTANCE = new RepositorioTipoConsumo();

  public static RepositorioTipoConsumo instance(){
    return INSTANCE;
  }

  private RepositorioTipoConsumo(){
    super("TipoConsumo");
  }

}
