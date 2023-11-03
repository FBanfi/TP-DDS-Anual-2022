package domain.mediciones;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class FactorEmision {
  @Enumerated(EnumType.STRING)
  private Unidad unidad;

  @Column
  private int valor;

  public FactorEmision(Unidad unidad) {
    this.unidad = unidad;
    this.valor = 1;//todo: revisar esta cosa
  }

  public FactorEmision() {

  }

  public Unidad getUnidad() {
    return this.unidad;
  }

  public int getValor() {
    return valor;
  }

  public void setValor(int valor) {
    this.valor = valor;
  }
}
