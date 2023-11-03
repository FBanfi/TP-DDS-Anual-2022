package domain.organizaciones;

import domain.persistencia.EntidadPersistente;
import domain.services.distanciaAPI.DistanciaService;

import javax.persistence.*;

@Entity
@Table(name = "paradas")
public class Parada extends EntidadPersistente {
  @Column
  private String nombre;

  @Embedded
  private Ubicacion origen;

  @OneToOne(cascade = {CascadeType.ALL})
  private Parada destino;

  @ManyToOne(cascade = {CascadeType.ALL})
  private DistanciaService servicioDistancia;

  public Parada(DistanciaService servicioDistancia, String nombre, Ubicacion origen, Parada destino) {
    this.servicioDistancia = servicioDistancia;
    this.nombre = nombre;
    this.origen = origen;
    this.destino = destino;
  }

  public Parada() {
  }

  public Parada(DistanciaService servicioDistancia, String nombre, Ubicacion origen) {
    this.servicioDistancia = servicioDistancia;
    this.nombre = nombre;
    this.origen = origen;
  }

  public Ubicacion getUbicacion(){
    return origen;
  }

  public int getAltura(){
    return origen.getAltura();
  }

  public int getLocal(){
    return origen.getLocal();
  }

  public double getDistancia() {
    return servicioDistancia.obtenerDistancia(origen, destino.getUbicacion()).getValor();
  }

  public void setParada(Parada destino) {
    this.destino = destino;
  }

  public String getNombre() {
    return nombre;
  }

  public long getIdSentido() {
    return super.getId();
  }

  public long idValorOpcionSentido() {
    return super.getId();
  }
  public String valorOpcionSentido() {
    return getNombre();
  }
}
