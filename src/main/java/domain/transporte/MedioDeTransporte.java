package domain.transporte;

import domain.mediciones.Consumo;
import domain.persistencia.EntidadPersistente;

import javax.persistence.*;


@Entity
@Table(name = "medios_de_transporte")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
public abstract class MedioDeTransporte extends EntidadPersistente {
  @OneToOne(cascade = {CascadeType.ALL})
  private Vehiculo vehiculo;

  @Column
  private String nombre;

  public MedioDeTransporte(Vehiculo vehiculo) {
    this.vehiculo = vehiculo;
  }

  public MedioDeTransporte(Vehiculo vehiculo, String nombre) {
    this.vehiculo = vehiculo;
    this.nombre = nombre;
  }

  public MedioDeTransporte() {

  }

  public Consumo getConsumo(){
    return vehiculo.getConsumo();
  }

  public double getFactorEmision(){
    return getConsumo().getFactorEmision().getValor();
  }

  public abstract double getEmision();



  public Vehiculo getVehiculo() {
    return this.vehiculo;
    
  }
  public String getNombre() {
    return nombre;
  }

  public boolean esIgualAParticualOContratado() {
    return false;
  }


}