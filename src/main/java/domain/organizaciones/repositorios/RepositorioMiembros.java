package domain.organizaciones.repositorios;

import domain.organizaciones.Miembro;

import java.util.ArrayList;
import java.util.List;

public class RepositorioMiembros extends Repositorio<Miembro> {
  private static final RepositorioMiembros INSTANCE = new RepositorioMiembros();
  private List<Miembro> miembros = new ArrayList<>();

  public static RepositorioMiembros instance() {
    return INSTANCE;
  }

  private RepositorioMiembros() {
    super("Miembro");
  }

  public void agregarMiembro(Miembro miembro) {
    entityManager().persist(miembro);
  }

  public List<Miembro> obtenerMiembro(String nombre) {

    String query = String.format("from Miembro where nombreCompleto='%s'", nombre);
    return entityManager().createQuery(query).getResultList();
  }

  public Miembro obtenerMiembro(int id) {

    String query = String.format("from Miembro where id='%s'", id);
    return (Miembro) entityManager().createQuery(query).getResultList().get(0);
  }


}
