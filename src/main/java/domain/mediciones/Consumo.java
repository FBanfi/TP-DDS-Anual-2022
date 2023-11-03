package domain.mediciones;

import domain.persistencia.EntidadPersistente;

import javax.persistence.*;

@Entity
@Table(name = "consumos")
public class Consumo extends EntidadPersistente {
  @Column(name = "valor_c")
  private double valor;

  @Enumerated(value = EnumType.STRING)
  private TipoConsumo tipoConsumo;

  @Embedded
  private Periodicidad periodicidad;

  @Embedded
  private FactorEmision factorEmision;

  public Consumo(double valor, TipoConsumo tipoConsumo, Periodicidad periodicidad, FactorEmision factorEmision) {
    this.valor = valor;
    this.tipoConsumo = tipoConsumo;
    this.periodicidad = periodicidad;
    this.factorEmision = factorEmision;
    validarTipoConsumo(tipoConsumo, factorEmision);
  }

  public Consumo() {

  }

  public void validarTipoConsumo(TipoConsumo tipoConsumo, FactorEmision factorEmision) {
    if (!(factorEmision.getUnidad().esIgualA(tipoConsumo.getUnidad()))) {
      throw new RuntimeException("Unidad factor de emision distinta a unidad de consumo");
    }
  }

  public double getValor() {
    return valor;
  }

  public FactorEmision getFactorEmision() {
    return factorEmision;
  }

  public TipoConsumo getTipoConsumo() {
    return this.tipoConsumo;
  }

  public void setValor(double valor) {
    this.valor = valor;
  }

  public Periodicidad getPeriodicidad() {
    return periodicidad;
  }
}