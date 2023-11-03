package domain.mediciones;

import domain.persistencia.EntidadPersistente;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
public abstract class Reporte extends EntidadPersistente {

  @Column
  private LocalDate fecha;
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "reporte_id")
  List<DetalleReporte> detalleReporteComposicion;

  // Podria indicar un sector o area de organizacion o una fecha, o directamente el total de HC
  @Enumerated(EnumType.STRING)
  CriterioReporte criterioReporte;

  public Reporte(LocalDate fecha, List<DetalleReporte> detalleReporteComposicion) {
    this.fecha = fecha;
    this.detalleReporteComposicion = detalleReporteComposicion;
  }

  public Reporte() {
  }

  public LocalDate getFecha(){
    return fecha;
  }

  public List<DetalleReporte> getDetalles(){
    return detalleReporteComposicion;
  }

  public CriterioReporte getCriterioReporte() {
    return criterioReporte;
  }

  public void setFecha(LocalDate fecha){ // SOLO PARA EL TEST
    this.fecha = fecha;
  }

  public String getHc(){
    return String.valueOf(detalleReporteComposicion.stream().mapToDouble(d -> d.valorDetalle).sum());
  }
}
