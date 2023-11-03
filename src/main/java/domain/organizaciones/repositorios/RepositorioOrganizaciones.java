package domain.organizaciones.repositorios;

import domain.organizaciones.Organizacion;

import java.util.ArrayList;
import java.util.List;

public class RepositorioOrganizaciones extends Repositorio<Organizacion> {
  private static final RepositorioOrganizaciones INSTANCE = new RepositorioOrganizaciones();
  private List<Organizacion> organizacions = new ArrayList<>();

  public static RepositorioOrganizaciones instance() {
    return INSTANCE;
  }

  private RepositorioOrganizaciones() {
    super("Organizacion");
  }

  public void agregarOrganizacion(Organizacion organizacion) {
    entityManager().persist(organizacion);
  }
}