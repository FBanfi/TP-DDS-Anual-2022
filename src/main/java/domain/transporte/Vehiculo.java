package domain.transporte;

import domain.mediciones.Consumo;
import domain.persistencia.EntidadPersistente;
import domain.transporte.enums.TipoVehiculo;

import javax.persistence.*;

@Entity
@Table(name = "vehiculos")
public class Vehiculo extends EntidadPersistente {
  @Enumerated(EnumType.STRING)
  private TipoVehiculo vehiculo;

  @OneToOne(cascade = {CascadeType.ALL})
  private Consumo consumo;

  public Vehiculo(TipoVehiculo vehiculo, Consumo consumo) {
    this.vehiculo = vehiculo;
    this.consumo = consumo;
  }

  public Vehiculo() {

  }

  public Consumo getConsumo(){
    return consumo;
  }
}


