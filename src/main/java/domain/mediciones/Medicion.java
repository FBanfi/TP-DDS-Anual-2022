package domain.mediciones;

import domain.persistencia.EntidadPersistente;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "mediciones")
public class Medicion extends EntidadPersistente {

  @OneToOne
  private Consumo consumo;

  public Medicion(Consumo consumo) {
    this.consumo = consumo;
  }
  public Medicion() {
  }
  public TipoPeriodicidad getPeriodoImputacion(){
    return consumo.getPeriodicidad().getTipoPeriodicidad();
  }
}