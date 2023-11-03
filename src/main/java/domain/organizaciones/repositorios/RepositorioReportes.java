package domain.organizaciones.repositorios;

import domain.mediciones.CriterioReporte;
import domain.mediciones.Reporte;
import domain.mediciones.ReporteOrganizacion;
import domain.mediciones.ReporteSectorTerritorial;
import domain.organizaciones.Organizacion;
import domain.organizaciones.SectorTerritorial;
import domain.organizaciones.enums.ClasificacionOrganizacion;
import domain.organizaciones.enums.TipoOrganizacion;

import java.util.ArrayList;
import java.util.List;

public class RepositorioReportes extends Repositorio<Reporte>{
  private static final RepositorioReportes INSTANCE = new RepositorioReportes();
  private List<Reporte> reportes = new ArrayList<>();

  public static RepositorioReportes instance() {
    return INSTANCE;
  }

  private RepositorioReportes() {
    super("Reportes");
  }

  public void agregarReporte(Reporte reporte) {
    entityManager().persist(reporte);
  }


  public List<ReporteOrganizacion> obtenerReporteOrganizacion(Organizacion organizacion,
                                                              CriterioReporte criterioReporte) {
    String template =
        "from Reporte where tipo='Organizacion' and org='%d' and criterioReporte='%s'";
    String query = String.format(template, organizacion.getId(), criterioReporte.toString());
    return entityManager().createQuery(query).getResultList();
  }

  public List<ReporteOrganizacion> obtenerReporteTipoYClasificacionOrganizacion(TipoOrganizacion tipo,
                                                                                ClasificacionOrganizacion clasificacion,
                                                                                CriterioReporte criterioReporte) {

    String template =
        "select r from Reporte r join r.org o " +
        "where tipo='Organizacion' " +
        "and tipoOrganizacion='%s' " +
        "and clasificacionOrganizacion='%s' " +
        "and criterioReporte='%s'";
    String query = String.format(template, tipo.toString(), clasificacion.toString(), criterioReporte.toString());
    return entityManager().createQuery(query).getResultList();
  }

  public List<ReporteSectorTerritorial> obtenerReporteSectores(SectorTerritorial sectorTerritorial,
                                                               CriterioReporte criterioReporte) {
    String template =
        "from Reporte where tipo='SectorTerritorial' and sector='%d' and criterioReporte='%s'";
    String query = String.format(template,  sectorTerritorial.getId(), criterioReporte.toString());
    List<ReporteSectorTerritorial> reportes = entityManager().createQuery(query).getResultList();
    return reportes;
  }

  public List<ReporteSectorTerritorial> obtenerReportesSectores() {
    String template =
        "from Reporte where tipo='SectorTerritorial'";
    List<ReporteSectorTerritorial> reportes = entityManager().createQuery(template).getResultList();
    return reportes;
  }

  public List<ReporteOrganizacion> obtenerReportesOrganizaciones() {
    String template =
        "from Reporte where tipo='Organizacion'";
    List<ReporteOrganizacion> reportes = entityManager().createQuery(template).getResultList();
    return reportes;
  }

  public List<ReporteOrganizacion> obtenerReportesOrganizaciones(CriterioReporte criterioReporte) {
    String template =
        "from Reporte where tipo='Organizacion' and criterioReporte='%s'";
    String query = String.format(template, criterioReporte.toString());
    List<ReporteOrganizacion> reportes = entityManager().createQuery(query).getResultList();
    return reportes;
  }

  public List<ReporteSectorTerritorial> obtenerReportesSectores(CriterioReporte criterioReporte) {
    String template =
        "from Reporte where tipo='SectorTerritorial' and criterioReporte='%s'";
    String query = String.format(template, criterioReporte.toString());
    List<ReporteSectorTerritorial> reportes = entityManager().createQuery(query).getResultList();
    return reportes;
  }

}
