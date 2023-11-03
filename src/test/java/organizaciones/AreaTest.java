
package organizaciones;

import domain.organizaciones.*;
import domain.sugerenciasYnotificaciones.NotificacionTwilio;
import domain.sugerenciasYnotificaciones.NotificacionJavaMail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import organizaciones.mothers.MotherOrganizacion;
import domain.organizaciones.enums.TipoDocumento;
import domain.services.distanciaAPI.DistanciaService;
import domain.services.distanciaAPI.GeoddsService;

import static org.mockito.Mockito.mock;

class AreaTest {
  Area areaPrueba;
  PuestoDeTrabajo puestoDeTrabajoPrueba;
  Organizacion orga;
  Miembro miembro1;
  NotificacionJavaMail servicioMail = mock(NotificacionJavaMail.class);
  NotificacionTwilio whatsappService = mock(NotificacionTwilio.class);
  DistanciaService distanciaService = mock(GeoddsService.class);

  @BeforeEach
  void init() {
    areaPrueba = new Area("areaPrueba");
    puestoDeTrabajoPrueba = new PuestoDeTrabajo(new MotherOrganizacion().crearOrganizacionSociedadAnonima(), areaPrueba);
    miembro1 = new Miembro("John Doe", new Documento(TipoDocumento.DNI, "25000000"), puestoDeTrabajoPrueba);
  }

  @Test
  void sePuedeDarDeAltaUnArea() {
    Assertions.assertDoesNotThrow(() -> this.crearArea("areaPrueba2"));
  }

  @Test
  void sepuedeAgregarMiembroAUnArea() {
    areaPrueba.agregarMiembro(miembro1);
    Assertions.assertTrue(areaPrueba.getMiembros().contains(miembro1));
  }
  @Test
  void unaAreaPuedeConocerSuPromedioDeHC() {
    Organizacion organizacion = new MotherOrganizacion().crearOrganizacionConMiembros(whatsappService, servicioMail,distanciaService);
    Area area1 = organizacion.getAreas().get(0);
    Assertions.assertEquals(10, area1.promedioDeHC());
  }

  static Area crearArea(String nombre) {
    return new Area(nombre);
  }
}
