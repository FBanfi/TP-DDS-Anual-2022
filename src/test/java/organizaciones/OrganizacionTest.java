package organizaciones;

import domain.organizaciones.Area;
import domain.organizaciones.Miembro;
import domain.organizaciones.Organizacion;
import domain.sugerenciasYnotificaciones.NotificacionTwilio;
import domain.sugerenciasYnotificaciones.NotificacionJavaMail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.exceptions.AtributosNulosException;
import domain.services.distanciaAPI.DistanciaService;
import domain.services.distanciaAPI.GeoddsService;
import organizaciones.mothers.MotherMiembro;
import organizaciones.mothers.MotherOrganizacion;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.mock;

class OrganizacionTest {
  NotificacionJavaMail mailService;
  NotificacionTwilio whatsappService;
  DistanciaService distanciaService;

  @BeforeEach
  void init() {
    mailService = mock(NotificacionJavaMail.class);
    whatsappService = mock(NotificacionTwilio.class);
    distanciaService = mock(GeoddsService.class);
  }

  @Test
  void sePuededarDeAltaOrganizacion() {
    Assertions.assertDoesNotThrow(() -> new MotherOrganizacion().crearOrganizacionSociedadAnonima());
  }

  @Test
  void noSePuedeDarDeAltaOrganizacionSiFaltanAtributos() {
    Assertions.assertThrows(AtributosNulosException.class, () -> new MotherOrganizacion().crearOrganizacionIncompleta());
  }

  @Test
  void unaOrganizacionPuedeConocerSusMiembros() {
    Organizacion org = new MotherOrganizacion().crearOrganizacionSociedadAnonima();

    MotherMiembro builderMiembro = new MotherMiembro();

    org.agregarArea(builderMiembro.getArea1());
    org.agregarArea(builderMiembro.getArea2());

    List<Miembro> miembros = new ArrayList<>();
    miembros.add(builderMiembro.crearMiembroArea1());
    miembros.add(builderMiembro.crearMiembroArea1());
    miembros.add(builderMiembro.crearMiembroArea1());

    Assertions.assertTrue(org.getMiembros().equals(miembros));
  }

  @Test
  void unaOrganizacionPuedeSaberSiTieneTrayectosCompartidos() {
    Assertions.assertTrue(new MotherOrganizacion().crearOrganizacionConMiembros(whatsappService, mailService, distanciaService).tieneTrayectosCompartidos());
  }

  @Test
  void unaOrganizacionPuedeSaberCualesSonLosTrayectosCompartidos() {
    Assertions.assertTrue(new MotherOrganizacion().crearOrganizacionConMiembros(whatsappService, mailService, distanciaService).getTrayectosCompartidos().stream().count() > 0);
  }
  @Test
  void unaOrganizacionPuedeCalcularSuHuellaDeCarbono() {
    Organizacion organizacion = new MotherOrganizacion().crearOrganizacionConMiembros(whatsappService, mailService,distanciaService);
    Assertions.assertEquals(20, organizacion.obtenerHC());
  }

  @Test
  void unaOrganizacionPuedeAlmacenarContactos() {
    Organizacion organizacion = new MotherOrganizacion().crearOrganizacionConMiembros(whatsappService, mailService, distanciaService);
    MotherMiembro motherMiembro = new MotherMiembro();
    organizacion.agregarSubscriptorARecomendaciones(motherMiembro.crearMiembroArea1());

    Assertions.assertEquals(1, organizacion.getSubscriptoresARecomendaciones().stream().count());
  }

  @Test
  void unaOrganizacionPuedeRecibirRecomendacion() {
    Organizacion organizacion = new MotherOrganizacion().crearOrganizacionConMiembros(whatsappService, mailService, distanciaService);
    MotherMiembro motherMiembro = new MotherMiembro(mailService, whatsappService);
    organizacion.agregarSubscriptorARecomendaciones(motherMiembro.crearMiembroArea1());
    Assertions.assertDoesNotThrow(() -> organizacion.notificarSuscriptores("https://aulasvirtuales.frba.utn.edu.ar/"));
  }
  @Test
  void unaOrganizacionPuedeConocerElImpactoDeUnMiembro() {
    Organizacion organizacion = new MotherOrganizacion().crearOrganizacionConMiembros(whatsappService, mailService,distanciaService);
    Area area1 = organizacion.getAreas().get(0);
    Miembro miembroArea1 = area1.getMiembros().get(0);
    Assertions.assertEquals(50, organizacion.obtenerImpactoMiembro(miembroArea1));
  }


}

