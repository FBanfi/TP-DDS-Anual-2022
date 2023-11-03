package domain.mediciones;

import domain.persistencia.EntidadPersistente;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class DetalleReporte extends EntidadPersistente {
  @Column
  String tipo; // Podria indicar un sector o area de organizacion o una fecha, o directamente el total de HC
  @Column
  double valorDetalle; // Podria ser el valor de la composicion o el porcentaje de evolucion

  public DetalleReporte() {
  }

  //Ver ejemplo de uso en Org y Sector
  public DetalleReporte(String tipo, double valorDetalle) {
    this.tipo = tipo;
    this.valorDetalle = valorDetalle;
  }

  public double getValorDetalle(){
    return valorDetalle;
  }
}