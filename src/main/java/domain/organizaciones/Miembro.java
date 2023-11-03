package domain.organizaciones;

import domain.persistencia.EntidadPersistente;
import domain.sugerenciasYnotificaciones.Notificacion;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "miembros")
public class Miembro extends EntidadPersistente {
  @Column
  private String nombreCompleto;

  //@OneToOne(cascade=CascadeType.ALL) //creo q no podemos hacerlo como embedded porque es importante su identidad
  //juancito: creo que podemos hacerlo como embedded, es informacion del miembro que decidimos agrupar en una clase
  //(cosificacandola)
  @Embedded
  private Documento documento;

  @Column
  private String numTelefono;

  @Column
  private String email;

  @ManyToMany(
      cascade = CascadeType.ALL,
      mappedBy = "miembrosQueCompartenTrayecto",
      targetEntity = Trayecto.class
  )
  private List<Trayecto> trayectos = new ArrayList<>();

  //@OneToMany //AAAAAAAAAaa ya me maree, no se si esta bien esta relacion
             // lucas: Para mi seria ManyToMany ya que varios miembros pueden tener el mismo puesto
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "miembro_id")
  private List<PuestoDeTrabajo> puestosDeTrabajo = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "miembro_id")
  private List<Notificacion> notificaciones = new ArrayList<>();

  public Miembro(String nombre, Documento doc,
                 PuestoDeTrabajo puestoDeTrabajo
  ) {
    this.nombreCompleto = requireNonNull(nombre,"Falta el Nombre!");
    this.documento = requireNonNull(doc,"Falta el documento");
    this.puestosDeTrabajo.add(puestoDeTrabajo);
  }

  public Miembro(String nombre, Documento doc, String numTelefono, String email
  ) {
    this.email = requireNonNull(email,"Falta el email!");
    this.numTelefono = requireNonNull(numTelefono,"Falta el numero telefono!");
    this.nombreCompleto = requireNonNull(nombre,"Falta el Nombre!");
    this.documento = requireNonNull(doc,"Falta el documento");
  }

  public Miembro() {

  }

  public void agregarNotificacion(Notificacion servicioNotificacion){
    notificaciones.add(servicioNotificacion);
  }

  public void setNumTelefono(String numTelefono) {
     this.numTelefono = numTelefono;
  }

  public void setEmail(String email){
    this.email = email;
  }

  public String getNumTelefono() {
    return this.numTelefono;
  }

  public String getEmail(){
    return this.email;
  }

  public String getNombreCompleto() {
    return nombreCompleto;
  }

  public void solicitarVinculoConOrganizacion(Organizacion org, Area area) {
    SolicitudDeVinculo solicitud = new SolicitudDeVinculo(area,this);
    org.recibirSolicitud(solicitud);
  }

  public double obtenerHC(){
    return trayectos.stream().mapToDouble(trayecto -> trayecto.obtenerHC()).sum();
  }

  public double obtenerHCen(Organizacion org){
    return trayectos.stream().filter(t -> t.perteneceA(org)).mapToDouble(trayecto -> trayecto.obtenerHC()).sum();
  }

  public void compartirTrayectoCon(Trayecto trayecto, Miembro miembro){
    validarTrayecto(trayecto);
    miembro.agregarTrayecto(trayecto);
    trayecto.añadirMiembroALista(miembro);
  }
  public List<Organizacion> getOrganizaciones() {
    return puestosDeTrabajo.stream().map(p->p.getOrganizacion()).collect(Collectors.toList());
  }
  public boolean estaEnLaMismaOrganizacionQue(Miembro miembro){
    List<Organizacion> organizaciones = miembro.getOrganizaciones();
    organizaciones.retainAll(this.getOrganizaciones());
    return organizaciones.stream().count() > 0;
  }
  public void agregarTrayecto(Trayecto trayecto){
    if(!trayectos.contains(trayecto)){
      trayectos.add(trayecto);
      trayecto.agregarMiembro(this);
    }
  }

  public void quitarTrayecto(Trayecto trayecto){
    if(trayectos.contains(trayecto)){
      trayectos.remove(trayecto);
      trayecto.quitarMiembro(this);
    }
  }


  public void validarTrayecto(Trayecto trayecto){
    if(!trayecto.tieneTodosSusTramosConServicioPartOContr()){
      throw new  RuntimeException("Trayecto invalido para compartir, todos sus tramos deben ser con servicio particular o" +
          "contratado");
    }
  }

  //Este metodo lo usPuestoDeTrabajonPuestoDeTrabajo nuevoPuesto
  //puestosDeTrabajo.addinuevoPuestomas de acordarlo entre nos
  public void agregarOrganizacion(PuestoDeTrabajo nuevoPuesto) {
    puestosDeTrabajo.add(nuevoPuesto);
  }

  public boolean perteneceAOrganizacion(Organizacion organizacion) {
    return puestosDeTrabajo.stream().anyMatch(puesto -> puesto.getOrganizacion() == organizacion);
  }

  public boolean tieneTrayectoConSerContrOParticu() {
    return trayectos.stream().anyMatch(Trayecto::tieneTodosSusTramosConServicioPartOContr);
  }

  public List<Trayecto> getTrayectoConSerContrOParticu() {
    return trayectos.stream().filter(Trayecto::tieneTodosSusTramosConServicioPartOContr).collect(Collectors.toList());
  }

  /* public List<Trayecto> getTrayectosConServicioParticularOContratado(){
    return trayectos.stream().filter(trayecto -> trayecto.usaVechiculoParticularOContratado())
        .collect(Collectors.toList());
  }
  */

  public List<Trayecto> getTrayectos(){
    return trayectos;
  }

  public void recibirRecomendacion(String guiaRecomendacion){
    this.notificar(guiaRecomendacion, "Recomendacion");
  }

  public void notificar(String mensaje, String asunto){
    notificaciones.forEach(n-> n.notificar(mensaje, this.numTelefono, this.email, asunto));
  }
}
