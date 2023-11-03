package domain.organizaciones;


import domain.exceptions.AtributosNulosException;
import domain.exceptions.ExcepcionMiembro;
import domain.mediciones.CriterioReporte;
import domain.mediciones.DetalleReporte;
import domain.mediciones.Medicion;
import domain.mediciones.ReporteOrganizacion;
import domain.organizaciones.enums.ClasificacionOrganizacion;
import domain.organizaciones.enums.RazonSocial;
import domain.organizaciones.enums.TipoOrganizacion;
import domain.organizaciones.repositorios.RepositorioReportes;
import domain.persistencia.EntidadPersistente;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "organizaciones")
public class  Organizacion extends EntidadPersistente {
  @Column
  private String nombre;

  @Enumerated(EnumType.STRING)
  private RazonSocial razonSocial;

  @Enumerated(EnumType.STRING)
  private TipoOrganizacion tipoOrganizacion;

  @Embedded
  //lucas bat:No podria ser Embeddable?, ya que si se borra la organizacion ya se borra la direccion ya que esta
  // embebide en ella, no esta en una tabla, En caso de que no, en realidad no deberia ser OneToOne? ya que solo
  // va a exsitir una organizacion en una ubicacion.
  private Ubicacion ubicacionGeografica;

  @Enumerated(EnumType.STRING)
  private ClasificacionOrganizacion clasificacionOrganizacion;

  @OneToMany(cascade = {CascadeType.ALL})
  @JoinColumn(name = "organizacion_id")
  private List<Area> areas = new ArrayList<>();

  @OneToMany
  @JoinColumn(name = "organizacion_id")
  private List<Medicion> mediciones = new ArrayList<>();

  @OneToMany(cascade = {CascadeType.ALL})
  @JoinColumn(name = "organizacion_id")
  private List<SolicitudDeVinculo> solicitudesPendientes = new ArrayList<SolicitudDeVinculo>();

  @OneToMany
  @JoinColumn(name = "organizacion_id")
  private List<Miembro> subscriptoresARecomendaciones = new ArrayList<Miembro>();

  public Organizacion(String nombre, RazonSocial razonSocial,
                      TipoOrganizacion tipoOrganizacion,
                      Ubicacion ubicacionGeografica,
                      ClasificacionOrganizacion clasificacionOrganizacion
  ) {
    validarAtributosNulos(nombre, razonSocial, tipoOrganizacion,
         ubicacionGeografica, clasificacionOrganizacion);
    this.nombre = nombre;
    this.razonSocial = razonSocial;
    this.tipoOrganizacion = tipoOrganizacion;
    this.ubicacionGeografica = ubicacionGeografica;
    this.clasificacionOrganizacion = clasificacionOrganizacion;
  }

  public Organizacion() {

  }

  public String getNombre() {
    return nombre;
  }

  public List<Area> getAreas(){
    return areas;
  }

  public void agregarSubscriptorARecomendaciones(Miembro miembro){
    subscriptoresARecomendaciones.add(miembro);
  }

  public List<Miembro> getSubscriptoresARecomendaciones(){
    return this.subscriptoresARecomendaciones;
  }

  public void cargarMedicion(Medicion medicion){
    mediciones.add(medicion);
  }

  public void recibirSolicitud(SolicitudDeVinculo solicitud) {
    solicitudesPendientes.add(solicitud);
  }

  public void aceptarSolicitudes() {
    this.solicitudesPendientes.stream().forEach(s -> this.aceptarMiembro(s));
  }

  public void aceptarMiembro(SolicitudDeVinculo solicitud) {
    solicitud.aceptar(this);
    solicitudesPendientes.remove(solicitud);
  }
  //Habria que avisarle al solicitante? Como el enunciado no dice nada supongo que no

  public void rechazarSolicitud(SolicitudDeVinculo solicitud) {
    solicitudesPendientes.remove(solicitud);
  }

  private void validarAtributosNulos(String nombre,
                                     RazonSocial razonSocial,
                                     TipoOrganizacion tipoOrganizacion,
                                     Ubicacion ubicacionGeografica,
                                     ClasificacionOrganizacion clasificacionOrganizacion) {
    if (nombre == null || razonSocial == null || tipoOrganizacion == null
            || ubicacionGeografica == null || clasificacionOrganizacion == null) {
      throw new AtributosNulosException("Tiene atributos nulos");
    }
  }

  public void agregarArea(Area area) {
    this.areas.add(area);
  }

  public List<Miembro> getMiembros() {
    return this.areas.stream()
        .map(Area::getMiembros)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  public List<Trayecto> getTrayectosConSerContrOParticu() {
      return getTrayectosOrganizacion().stream()
          .filter(Trayecto::tieneTodosSusTramosConServicioPartOContr).collect(Collectors.toList());
      //return areas.stream().map(Area::getMiembros).flatMap(Collection::stream)
      //  .map(Miembro::getTrayectoConSerContrOParticu).flatMap(Collection::stream).collect(Collectors.toList());
  }

  public List<Trayecto> getTrayectosOrganizacion() {
    return areas.stream()
        .map(Area::getTrayectosMiembros)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  public List<Trayecto> getTrayectosNoCompartidos() {
    return getTrayectosOrganizacion().stream()
        .filter(trayecto -> !getTrayectosConSerContrOParticu().contains(trayecto))
        .collect(Collectors.toList());
  }

  public List<Trayecto> getTrayectosCompartidos() { //con medios de transporte part o contratado
    return getTrayectosConSerContrOParticu().stream().distinct().collect(Collectors.toList());
  }

  public boolean tieneTrayectosCompartidos(){
    return getTrayectosCompartidos().stream().count() > 0;
  }

  public double obtenerHC() {
    return getTrayectosCompartidos().stream().mapToDouble(Trayecto::obtenerHC).sum()
        + getTrayectosNoCompartidos().stream().mapToDouble(Trayecto::obtenerHC).sum();
  }

  public double obtenerImpactoMiembro(Miembro miembro) {
    if (miembro.perteneceAOrganizacion(this) != true) {
      throw new ExcepcionMiembro("Este miembro no pertenece a la organizacion en cuestion");
    } else {
      return miembro.obtenerHC() / obtenerHC() * 100;
    }
  }

  public void notificarSuscriptores(String guiaRecomendacion) {
    subscriptoresARecomendaciones.stream().forEach(m->m.recibirRecomendacion(guiaRecomendacion));
  }

  public ReporteOrganizacion obtenerReporteHCComposicion() {
    ReporteOrganizacion reporteOrganizacion =
        new ReporteOrganizacion(CriterioReporte.COMPOSICION,
            LocalDate.now(),
            this.obtenerDetalleReporteComposicion(),
            this);
    return reporteOrganizacion;
  }

  private List<DetalleReporte> obtenerDetalleReporteComposicion() {
    List<DetalleReporte> detallesReporte = new ArrayList<>();
    for(Area area : this.getAreas()) {
      DetalleReporte detalle = new DetalleReporte("Area" + area.nombre, area.promedioDeHC());
      detallesReporte.add(detalle);
    }
    return detallesReporte;
  }

  public ReporteOrganizacion obtenerReporteHCTotal() {
    List<DetalleReporte> detalleReportes = new ArrayList<>();
    DetalleReporte detalle = new DetalleReporte("HC Total", this.obtenerHC());
    detalleReportes.add(detalle);
    ReporteOrganizacion alcanceReporte = new ReporteOrganizacion(CriterioReporte.HC_TOTAL,LocalDate.now(), detalleReportes,this);
    return alcanceReporte;
  }

  public List<ReporteOrganizacion> obtenerEvolucionEntreFechas(LocalDate desde, LocalDate hasta) {
    RepositorioReportes repoReporte = RepositorioReportes.instance();
    List<ReporteOrganizacion> reportesOrg = repoReporte.obtenerReporteOrganizacion(this , CriterioReporte.HC_TOTAL);

    if(reportesOrg == null){
      List<ReporteOrganizacion> reportes = new ArrayList<>();
      reportes.add(this.obtenerReporteHCTotal());
      return reportes;
    }

    List<ReporteOrganizacion> reportesFiltrados = reportesOrg.stream().filter(r -> (r.getFecha().isAfter(desde) && r.getFecha().isBefore(hasta)) || (r.getFecha().isEqual(desde) || (r.getFecha().isEqual(hasta)))).collect(Collectors.toList());

    return reportesFiltrados;
  }

  public List<SolicitudDeVinculo> getSolicitudPendientes() {
    return solicitudesPendientes;
  }
}