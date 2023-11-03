package domain.organizaciones;

import domain.exceptions.AtributosNulosException;
import domain.persistencia.EntidadPersistente;
import domain.services.distanciaAPI.Distancia;
import domain.services.distanciaAPI.DistanciaService;
import domain.transporte.MedioDeTransporte;

import javax.persistence.*;

@Entity
@Table(name = "tramos")
public class Tramo extends EntidadPersistente {
  @ManyToOne(cascade = CascadeType.ALL) //pensar que tipo de mapeo de herencia usar
  private MedioDeTransporte medioDeTransporte;

  @AttributeOverrides({
      @AttributeOverride(name = "calle", column = @Column(name = "calle1")),
      @AttributeOverride(name = "local", column = @Column(name = "local1")),
      @AttributeOverride(name = "altura", column = @Column(name = "altura1"))

  })
  @Embedded
  private Ubicacion destino;

  @AttributeOverrides({
      @AttributeOverride(name = "calle", column = @Column(name = "calle2")),
      @AttributeOverride(name = "local", column = @Column(name = "local2")),
      @AttributeOverride(name = "altura", column = @Column(name = "altura2"))

  })
  @Embedded
  private Ubicacion origen;

  @OneToOne
  private DistanciaService servicioDistancia;

  public Tramo(MedioDeTransporte medioDeTransporte, Ubicacion destino, Ubicacion origen, DistanciaService servicioDistancia) {
    this.destino = destino;
    this.origen = origen;
    this.servicioDistancia = servicioDistancia;
    this.medioDeTransporte = medioDeTransporte;
    validarAtributosNulos(medioDeTransporte, destino, origen);
  }

  public Tramo(MedioDeTransporte medioDeTransporte) {
    this.medioDeTransporte = medioDeTransporte;
  }

  public Tramo() {

  }

  private void validarAtributosNulos(MedioDeTransporte medioDeTransporte, Ubicacion destino, Ubicacion origen) {
    if (medioDeTransporte == null || destino == null || origen == null || servicioDistancia == null) {
      throw new AtributosNulosException("No puede ingresar "
          + "atributos nulos medioDeTransporte, destino, origen");
    }
  }

  public Distancia getDistancia() {
    return servicioDistancia.obtenerDistancia(origen, destino);
  }

  public boolean esParticularOContratado() {
    return medioDeTransporte.esIgualAParticualOContratado();
  }

  public double obtenerDA(){
    return medioDeTransporte.getConsumo().getValor() * getDistancia().getValor();
  }

  public double obtenerHC(){
    return this.obtenerDA() * medioDeTransporte.getFactorEmision();
  }

  public MedioDeTransporte getMedioDeTransporte() {
    return medioDeTransporte;
  }

  public String getDireccionDestino(){
    return destino.getDireccion();
  }

  public String getDireccionOrigen(){
    return origen.getDireccion();
  }

  public String getNombreMedioTransporte(){
    return medioDeTransporte.getNombre();
  }

  public Ubicacion getDestino() {
    return destino;
  }

  public Ubicacion getOrigen() {
    return origen;
  }
}

