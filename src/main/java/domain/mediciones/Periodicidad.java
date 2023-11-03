package domain.mediciones;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class Periodicidad {
  @Column
  private String periodoImputacion;

  @Enumerated(EnumType.STRING)
  private TipoPeriodicidad tipo;


  public Periodicidad(TipoPeriodicidad tipo, String periodoImputacion) {
    this.tipo = tipo;
    this.periodoImputacion = periodoImputacion;
  }

  public Periodicidad(TipoPeriodicidad tipo) {
    this.tipo = tipo;

  }

  public Periodicidad() {

  }

  public TipoPeriodicidad getTipoPeriodicidad() {
    return tipo;
  }

  public String getPeriodoImputacion() {
    return periodoImputacion;
  }
}
