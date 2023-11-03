package organizaciones.mothers;

import domain.organizaciones.Area;
import domain.organizaciones.Organizacion;
import domain.services.distanciaAPI.DistanciaService;
import domain.services.distanciaAPI.GeoddsService;
import domain.sugerenciasYnotificaciones.NotificacionTwilio;
import domain.sugerenciasYnotificaciones.NotificacionJavaMail;
import org.mockito.Mockito;

public class MotherArea {
  NotificacionJavaMail mailService = Mockito.mock(NotificacionJavaMail.class);
  NotificacionTwilio whatsappService = Mockito.mock(NotificacionTwilio.class);
  DistanciaService distanciaService = Mockito.mock(GeoddsService.class);

  public Area crearArea() {
    Organizacion org = new MotherOrganizacion()
        .crearOrganizacionConMiembros(whatsappService, mailService, distanciaService);
    org.getAreas().get(0);

    return org.getAreas().get(0);
  }
}
