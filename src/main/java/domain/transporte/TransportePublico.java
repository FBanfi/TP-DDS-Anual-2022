package domain.transporte;

import domain.organizaciones.Parada;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@DiscriminatorValue("publico")
public class TransportePublico extends MedioDeTransporte {
  @OneToMany(cascade = {CascadeType.ALL})
  @JoinColumn(name = "transporte_publico_id")
  private List<Parada> tramoIda = new ArrayList<>();

  @OneToMany(cascade = {CascadeType.ALL})
  @JoinColumn(name = "transporte_publico_id")
  private List<Parada> tramoVuelta = new ArrayList<>();


  public TransportePublico(Vehiculo vehiculo, LinkedList<Parada> tramoIda, List<Parada> tramoVuelta) {
    super(vehiculo);
    this.tramoIda = tramoIda;
    this.tramoVuelta = tramoVuelta;
    validarTramos();
  }

  public TransportePublico(Vehiculo vehiculo, String nombre ,List<Parada> tramoIda, List<Parada> tramoVuelta) {
    super(vehiculo, nombre);
    this.tramoIda = tramoIda;
    this.tramoVuelta = tramoVuelta;
    validarTramos();
  }
  public TransportePublico(Vehiculo vehiculo, String nombre) {
    super(vehiculo, nombre);
  }

  public TransportePublico(Vehiculo vehiculo) {
    super(vehiculo);
  }


  public TransportePublico() {

  }

  public void validarTramos(){
    if(tramoIda.get(0) != tramoVuelta.get(tramoVuelta.size()-1)){
      throw new RuntimeException("La parada inicial del tramo de ida y la parada " +
          "final del tramo de vuelta tienen que ser las mismas");
    }
  }

  @Override
  public double getEmision() {
    return 1;
  }

  public List<Parada> getTramoIda() {
    return tramoIda;
  }

  public List<Parada> getTramoVuelta() {
    return tramoVuelta.stream().filter(tramoVuelta -> !tramoIda.stream().anyMatch(tramoida -> tramoida.equals(tramoVuelta))).collect(Collectors.toList());
  }

  public long getIdTransporte() {return super.getId();}

  public long idValorOpcionTransporte() {return super.getId();}
  public String ValorOpcionTransporte() {return super.getNombre();}
}
