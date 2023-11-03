package organizaciones;

import domain.organizaciones.Organizacion;
import domain.organizaciones.SectorTerritorial;
import domain.organizaciones.TipoDeSector;
import domain.sugerenciasYnotificaciones.NotificacionTwilio;
import domain.sugerenciasYnotificaciones.NotificacionJavaMail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import organizaciones.mothers.MotherOrganizacion;
import domain.services.distanciaAPI.DistanciaService;
import domain.services.distanciaAPI.GeoddsService;

import static org.mockito.Mockito.mock;

class SectorTerritorialTest {
  Organizacion organizacion;
  SectorTerritorial sectorTerritorial;
  TipoDeSector tipoDeSector;
  NotificacionJavaMail mailService = mock(NotificacionJavaMail.class);
  NotificacionTwilio whatsappService = mock(NotificacionTwilio.class);
  DistanciaService distanciaService = mock(GeoddsService .class);

  @BeforeEach
  void init() {
    organizacion = new MotherOrganizacion().crearOrganizacionConMiembros(whatsappService, mailService, distanciaService);
    tipoDeSector = TipoDeSector.MUNICIPIO;
    sectorTerritorial = new SectorTerritorial(tipoDeSector);
    sectorTerritorial.agregarOrganizacion(organizacion);

  }
  @Test
  void sePuedeObtenerHCTotal() {
    Assertions.assertEquals(20, sectorTerritorial.obtenerHCTotal());
  }
}