package domain.organizaciones.repositorios;

import domain.organizaciones.SectorTerritorial;
import domain.organizaciones.TipoDeSector;

import java.util.ArrayList;
import java.util.List;

public class RepositorioSectoresTerritoriales extends Repositorio<SectorTerritorial> {
  private static final RepositorioSectoresTerritoriales INSTANCE = new RepositorioSectoresTerritoriales();
  private List<SectorTerritorial> sectores = new ArrayList<>();

  public static RepositorioSectoresTerritoriales instance() {
    return INSTANCE;
  }

  private RepositorioSectoresTerritoriales() {
    super("SectorTerritorial");
  }

  public void agregarSectorTerritorial(SectorTerritorial sectorTerritorial) {
    entityManager().persist(sectorTerritorial);
  }

  public List<SectorTerritorial> obtenerSectorSegun(TipoDeSector tipoDeSector) {
    String param = tipoDeSector.toString();
    String query = String.format("from SectorTerritorial where tipoDeSector='%s'", param);
    return entityManager().createQuery(query).getResultList();
  }
  public List<SectorTerritorial> obtenerSector(String nombre) {

    String query = String.format("from SectorTerritorial where nombre='%s'", nombre);
    return entityManager().createQuery(query).getResultList();
  }
}
