package organizaciones.mothers;

import domain.organizaciones.*;
import domain.organizaciones.enums.ClasificacionOrganizacion;
import domain.organizaciones.enums.RazonSocial;
import domain.organizaciones.enums.TipoDocumento;
import domain.organizaciones.enums.TipoOrganizacion;
import domain.services.distanciaAPI.DistanciaService;
import domain.sugerenciasYnotificaciones.NotificacionTwilio;
import domain.sugerenciasYnotificaciones.NotificacionJavaMail;

import java.util.ArrayList;
import java.util.List;

public class MotherOrganizacion {

  private String nombre;
  private RazonSocial razonSocial;
  private TipoOrganizacion tipoOrganizacion;
  private Ubicacion ubicacionGeografica;
  private ClasificacionOrganizacion clasificacionOrganizacion;
  private List<Area> areas = new ArrayList<>();
  private List<SolicitudDeVinculo> solicitudesPendientes = new ArrayList<SolicitudDeVinculo>();

  public Organizacion crearOrganizacion() {
    return new Organizacion(nombre, razonSocial, tipoOrganizacion,
            ubicacionGeografica, clasificacionOrganizacion);
  }
  public Organizacion crearOrganizacionIncompleta(){
    return new Organizacion(null,
        null, TipoOrganizacion.EMPRESA,
        new Ubicacion("Jonte y Chimborazo",2, 4),
        ClasificacionOrganizacion.EMPRESA_DEL_SECTOR_PRIMARIO);
  }
  public Organizacion crearOrganizacionSociedadAnonima(){
    return new Organizacion("UNIT TESTING",
        RazonSocial.SOCIEDAD_ANONIMA, TipoOrganizacion.EMPRESA,
        new Ubicacion("Av Cochabamba", 1, 3),
        ClasificacionOrganizacion.EMPRESA_DEL_SECTOR_PRIMARIO);
  }


  public Organizacion crearOrganizacionSociedadAccionesSimplificadas(){
    return  new Organizacion("ORG TEST",
        RazonSocial.SOCIEDAD_ACCIONES_SIMPLIFICADAS, TipoOrganizacion.EMPRESA,
        new Ubicacion("Av Rivadavia", 1, 3),
        ClasificacionOrganizacion.EMPRESA_DEL_SECTOR_SECUNDARIO);
  }

  public Organizacion crearOrganizacionConMiembros(NotificacionTwilio whatsappService, NotificacionJavaMail servicioMail, DistanciaService distanciaService){
    MotherTrayecto motherTrayecto = new MotherTrayecto();


    Organizacion org = this.crearOrganizacionSociedadAnonima();
    Area area1 = new Area("development");
    Area area2 = new Area("qa");
    Miembro miembro1 = new Miembro("franquito", new Documento(TipoDocumento.DNI, "000000"), new PuestoDeTrabajo(org, area1));
    miembro1.agregarNotificacion(whatsappService);
    miembro1.agregarNotificacion(servicioMail);
    Miembro miembro2 = new Miembro("luquita", new Documento(TipoDocumento.DNI, "000001"), new PuestoDeTrabajo(org, area2));
    miembro2.agregarNotificacion(whatsappService);
    miembro2.agregarNotificacion(servicioMail);
    Miembro miembro3 = new Miembro("juanceto", new Documento(TipoDocumento.DNI, "000003"), new PuestoDeTrabajo(org, area2));
    miembro3.agregarNotificacion(whatsappService);
    miembro3.agregarNotificacion(servicioMail);
    Miembro miembro4 = new Miembro("luqui", new Documento(TipoDocumento.DNI, "000003"), new PuestoDeTrabajo(org, area2));
    miembro4.agregarNotificacion(whatsappService);
    miembro4.agregarNotificacion(servicioMail);

    Trayecto trayecto = motherTrayecto.crearTrayectoCompartido(distanciaService);
    Trayecto trayectoNoCompartido = motherTrayecto.crearTrayectoNoCompartido(distanciaService);
    miembro1.agregarTrayecto(trayecto);
    miembro2.agregarTrayecto(trayecto);
    miembro3.agregarTrayecto(trayecto);
    miembro4.agregarTrayecto(trayectoNoCompartido);


    area1.agregarMiembro(miembro1);
    area2.agregarMiembro(miembro2);
    area2.agregarMiembro(miembro3);
    area2.agregarMiembro(miembro4);

    org.agregarArea(area1);
    org.agregarArea(area2);

    List<Miembro> miembros = new ArrayList<>();
    miembros.add(miembro1);
    miembros.add(miembro2);
    miembros.add(miembro3);
    miembros.add(miembro4);

    return org;
  }




}
