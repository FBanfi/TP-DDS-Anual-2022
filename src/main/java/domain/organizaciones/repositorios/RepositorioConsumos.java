package domain.organizaciones.repositorios;

import domain.mediciones.Consumo;

public class RepositorioConsumos extends Repositorio<Consumo> {
  private static final RepositorioConsumos INSTANCE = new RepositorioConsumos();

  public static RepositorioConsumos instance() {
    return INSTANCE;
  }

  private RepositorioConsumos() {
    super("Consumo");
  }

}