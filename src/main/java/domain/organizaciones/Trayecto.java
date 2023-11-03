package domain.organizaciones;

import domain.exceptions.AtributosNulosException;
import domain.persistencia.EntidadPersistente;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trayectos")
public class Trayecto extends EntidadPersistente {
 
  @Column
  private String nombre;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "trayecto_id")
  private List<Tramo> tramos = new ArrayList<>();

  @ManyToMany(
      targetEntity=Miembro.class,
      cascade=CascadeType.ALL
  )
  @JoinTable(
      name="trayectos_miembros",
      joinColumns=@JoinColumn(name="trayecto_id"),
      inverseJoinColumns=@JoinColumn(name="miembro_id")
  )
  private List<Miembro> miembrosQueCompartenTrayecto = new ArrayList<>();

  @ManyToOne
  private Organizacion organizacion;

  public Trayecto() {
  }

  public Trayecto(List<Tramo> tramos) {
    this.tramos = tramos;
  }

  public Trayecto(List<Tramo> tramos, String nombre) {
    this.tramos = tramos;
    this.nombre = nombre;
  }

  public Trayecto(String nombreTrayecto) {
    this.nombre = nombreTrayecto;
  }

  public Trayecto(List<Tramo> tramos, String nombre, Organizacion organizacion) {
    this.tramos = tramos;
    this.nombre = nombre;
    this.organizacion = organizacion;
  }

  public void añadirMiembroALista(Miembro miembro) {
    miembrosQueCompartenTrayecto.add(miembro);
  }

  public void anadirTramo(Tramo tramo) {
    if (tramo == null) {
      throw new AtributosNulosException("no se puede añadir tramo nulo");
    }

    tramos.add(tramo);
  }

  public String getNombre() {
    return nombre;
  }

  public boolean tieneTodosSusTramosConServicioPartOContr() {
    return tramos.stream().allMatch(Tramo::esParticularOContratado);
  }

  public double getDistanciaTotal() {
    return tramos.stream().mapToDouble(tramo -> tramo.getDistancia().getValor()).sum();
  }

  public List<Tramo> getTramos() {
    return tramos;
  }

  public double obtenerHC() {
    return this.tramos.stream().mapToDouble(Tramo::obtenerHC).sum();
  }

  public void eliminarTramo(Tramo tramo) {
      this.tramos.remove(tramo);
    }

  public boolean perteneceA(Organizacion org) {
    return this.organizacion.equals(org);
  }

  public void agregarMiembro(Miembro miembro) {
    this.miembrosQueCompartenTrayecto.add(miembro);
  }

  public boolean esCompartido() {
    return this.miembrosQueCompartenTrayecto.size()>1;
  }

  public void quitarMiembro(Miembro miembro) {
    miembrosQueCompartenTrayecto.remove(miembro);
  }

  public void eliminar() {
    tramos.clear();
  }
}
