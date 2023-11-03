package organizaciones.mothers;

import domain.organizaciones.*;
import domain.organizaciones.enums.TipoDocumento;
import domain.sugerenciasYnotificaciones.NotificacionTwilio;
import domain.sugerenciasYnotificaciones.NotificacionJavaMail;

public class MotherMiembro {
  Area area1;
  Area area2;
  Organizacion org;
  PuestoDeTrabajo puesto1;
  PuestoDeTrabajo puesto2;
  NotificacionJavaMail notificacionMailService;
  NotificacionTwilio notificacionCelularService;

  public MotherMiembro() {
    this.area1 = new Area("development");
    this.area2 = new Area("qa");
    this.org = new MotherOrganizacion().crearOrganizacionSociedadAnonima();
  }

  public MotherMiembro(NotificacionJavaMail notificacionMailService, NotificacionTwilio notificacionCelularService) {
    this.area1 = new Area("development");
    this.area2 = new Area("qa");
    this.org = new MotherOrganizacion().crearOrganizacionSociedadAnonima();
    this.notificacionMailService = notificacionMailService;
    this.notificacionCelularService = notificacionCelularService;
  }
  public Miembro crearMiembroArea1() {
    this.puesto1 =  new PuestoDeTrabajo(org, area1);
    Miembro miembro = new Miembro("franquito", new Documento(TipoDocumento.DNI, "000000"),this.puesto1);
    miembro.setEmail("franquitom@gmail.com");
    miembro.setNumTelefono("+11323435545");
    miembro.agregarNotificacion(this.notificacionMailService);
    miembro.agregarNotificacion(this.notificacionCelularService);
    this.area1.agregarMiembro(miembro);
    return miembro;
  }

  public Miembro crearMiembroArea2() {
    this.puesto2 =  new PuestoDeTrabajo(this.org, this.area2);
    Miembro miembro = new Miembro("luquita", new Documento(TipoDocumento.DNI, "000001"), this.puesto2);
    miembro.setEmail("luquita@gmail.com");
    miembro.setNumTelefono("+1132343555");
    miembro.agregarNotificacion(this.notificacionMailService);
    miembro.agregarNotificacion(this.notificacionCelularService);
    this.area2.agregarMiembro(miembro);
    return miembro;
  }

  public Miembro crearMiembro(Organizacion org, Area area) {
    PuestoDeTrabajo puesto = new PuestoDeTrabajo(org, area);
    Miembro miembro = new Miembro("pirulo", new Documento(TipoDocumento.DNI, "000002"), puesto);
    miembro.setEmail("pirulo@gmail.com");
    miembro.setNumTelefono("+1132343555");
    miembro.agregarNotificacion(this.notificacionMailService);
    miembro.agregarNotificacion(this.notificacionCelularService);
    area.agregarMiembro(miembro);
    return miembro;
  }


  public Area getArea1() {
    return this.area1;
  }

  public Area getArea2() {
    return this.area2;
  }
}
