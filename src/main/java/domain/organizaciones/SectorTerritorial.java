package domain.organizaciones;

import domain.mediciones.CriterioReporte;
import domain.mediciones.DetalleReporte;
import domain.mediciones.ReporteSectorTerritorial;
import domain.organizaciones.repositorios.RepositorioReportes;
import domain.persistencia.EntidadPersistente;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "sectores_territoriales")
public class SectorTerritorial extends EntidadPersistente {
  @Column
  private String nombre;

  @OneToMany(cascade = {CascadeType.ALL})
  @JoinColumn(name = "sector_id")
  private List<Organizacion> organizaciones = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  private TipoDeSector tipoDeSector;

  public SectorTerritorial(TipoDeSector tipoDeSector) {
    this.tipoDeSector = tipoDeSector;
  }

  public SectorTerritorial(String nombre, TipoDeSector tipoDeSector) {
    this.tipoDeSector = tipoDeSector;
    this.nombre = nombre;
  }
  public SectorTerritorial() {
  }

  public List<Organizacion> getOrganizaciones() {
    return organizaciones;
  }

  public double obtenerHCTotal() {
    return organizaciones.stream().mapToDouble(o->o.obtenerHC()).sum();
  }

  public ReporteSectorTerritorial obtenerReporteHCComposicion() {
    ReporteSectorTerritorial reporte = new ReporteSectorTerritorial(
        CriterioReporte.COMPOSICION,LocalDate.now(),
        this.obtenerDetalleReporteComposicion(),
        this);
    return reporte;
  }

  public ReporteSectorTerritorial obtenerReporteHCTotal() {
    List<DetalleReporte> detalleReportes = new ArrayList<>();
    DetalleReporte detalle = new DetalleReporte("HC Total", this.obtenerHCTotal());
    detalleReportes.add(detalle);
    ReporteSectorTerritorial alcanceReporte = new ReporteSectorTerritorial(CriterioReporte.HC_TOTAL, LocalDate.now(), detalleReportes,this);
    return alcanceReporte;
  }

  public List<DetalleReporte> obtenerDetalleReporteComposicion() {
    List<DetalleReporte> detallesReporte = new ArrayList<>();
    for(Organizacion org : this.getOrganizaciones()) {
      DetalleReporte detalle = new DetalleReporte("Organizacion" + org.getId(), org.obtenerHC());
      detallesReporte.add(detalle);
    }
    return detallesReporte;
  }

  public void agregarOrganizacion(Organizacion organizacion) {
    organizaciones.add(organizacion);
  }

  public List<ReporteSectorTerritorial> obtenerEvolucionEntreFechas(LocalDate desde, LocalDate hasta) {
    RepositorioReportes repoReporte = RepositorioReportes.instance();
    List<ReporteSectorTerritorial> reportesSectores = repoReporte.obtenerReporteSectores(this , CriterioReporte.HC_TOTAL);

    if(reportesSectores == null){
      List<ReporteSectorTerritorial> reportes = new ArrayList<>();
      reportes.add(this.obtenerReporteHCTotal());
      return reportes;
    }

    List<ReporteSectorTerritorial> reportesFiltrados = reportesSectores.stream().filter(r -> (r.getFecha().isAfter(desde) && r.getFecha().isBefore(hasta)) || (r.getFecha().isEqual(desde) || (r.getFecha().isEqual(hasta)))).collect(Collectors.toList());

    return reportesFiltrados;
  }

  public String getNombre() {
    return nombre;
  }

}