package domain.organizaciones.repositorios;

import domain.organizaciones.Parada;

import java.util.ArrayList;
import java.util.List;

public class RepositorioParadas extends Repositorio<Parada> {
  private static final RepositorioParadas INSTANCE = new RepositorioParadas();
  private List<Parada> paradas = new ArrayList<>();

  public static RepositorioParadas instance(){
    return INSTANCE;
  }

  private RepositorioParadas(){
    super("Parada");
  }

  public void agregarParada(Parada parada) {
    paradas.add(parada);
  }

  public List<Parada> getParadas() {
    return paradas;
  }
}
