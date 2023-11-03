package domain.mediciones;

import domain.organizaciones.SectorTerritorial;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.List;

@Entity
@DiscriminatorValue("SectorTerritorial")
public class ReporteSectorTerritorial extends Reporte {
  @ManyToOne
  @JoinColumn(name = "reporte_id")
  private SectorTerritorial sector;

  public ReporteSectorTerritorial(CriterioReporte criterio,
                                  LocalDate fecha,
                                  List<DetalleReporte> detalleReportes,
                                  SectorTerritorial sector) {
    super(fecha, detalleReportes);
    this.sector = sector;
    this.criterioReporte = criterio;
  }

  public ReporteSectorTerritorial() {
  }

  public String getNombreTipoReporte(){
    return sector.getNombre();
  }

  public SectorTerritorial getSector() {
    return sector;
  }
}
