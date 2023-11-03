package domain.organizaciones.repositorios;

import domain.mediciones.FactorEmision;

import java.util.ArrayList;
import java.util.List;

public class RepositorioFactoresEmision {
  private static final RepositorioFactoresEmision INSTANCE = new RepositorioFactoresEmision();
  private List<FactorEmision> factoresEmision = new ArrayList<>();

  public static RepositorioFactoresEmision instance(){
    return INSTANCE;
  }

  private RepositorioFactoresEmision() {}

  public void agregarFactorEmision(FactorEmision factorEmision) {
    factoresEmision.add(factorEmision);
  }

  public List<FactorEmision> getFactoresEmision() {
    return factoresEmision;
  }
}
