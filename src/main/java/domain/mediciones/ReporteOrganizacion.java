package domain.mediciones;


import domain.organizaciones.Organizacion;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.List;

@Entity
@DiscriminatorValue("Organizacion")
public class ReporteOrganizacion extends Reporte {

  @ManyToOne
  private Organizacion org;

  public ReporteOrganizacion(CriterioReporte criterio, LocalDate fecha, List<DetalleReporte> detalleReportes, Organizacion org) {
    super(fecha, detalleReportes);
    this.org = org;
    this.criterioReporte = criterio;
  }

  public ReporteOrganizacion() {
  }

  public Organizacion getOrg() {
    return org;
  }

  public String getNombreTipoReporte() {
    return org.getNombre();
  }
}
