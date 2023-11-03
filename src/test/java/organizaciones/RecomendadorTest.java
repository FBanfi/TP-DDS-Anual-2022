package organizaciones;

import domain.sugerenciasYnotificaciones.NotificacionTwilio;
import domain.sugerenciasYnotificaciones.NotificacionJavaMail;
import org.junit.jupiter.api.BeforeEach;
import domain.services.distanciaAPI.DistanciaService;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import static org.mockito.Mockito.*;

public class RecomendadorTest extends AbstractPersistenceTest  implements WithGlobalEntityManager {
  NotificacionJavaMail mailService;
  NotificacionTwilio whatsappService;
  DistanciaService distanciaService;

  @BeforeEach
  void init() {
    mailService = mock(NotificacionJavaMail.class);
    whatsappService = mock(NotificacionTwilio.class);
    distanciaService = mock(DistanciaService.class);

  }
}
/*
  @Test
  public void unaOrganizacionPuedeRecibirRecomendacionesCalendarizadas() throws InterruptedException {
    Recomendador recomendador = new Recomendador("www.wikipedia.com.ar", 604800000L);
    MotherOrganizacion motherOrganizacion = new MotherOrganizacion();

    Organizacion organizacion = motherOrganizacion.crearOrganizacionConMiembros(whatsappService, mailService, distanciaService);
    MotherMiembro motherMiembro = new MotherMiembro(mailService, whatsappService);

    Miembro miembro1 = motherMiembro.crearMiembroArea1();
    Miembro miembro2 = motherMiembro.crearMiembroArea2();
    organizacion.agregarSubscriptorARecomendaciones(miembro1);
    organizacion.agregarSubscriptorARecomendaciones(miembro2);

    RepositorioOrganizaciones.instance().agregar(organizacion);
    recomendador.recomendar();

    TimeUnit.SECONDS.sleep(10);

    verify(whatsappService, times(1))
        .notificar(miembro1.getNumTelefono(), miembro1.getEmail(), "www.wikipedia.com.ar", "Recomendacion");
    verify(whatsappService, times(1))
        .notificar(miembro2.getNumTelefono(), miembro1.getEmail(), "www.wikipedia.com.ar", "Recomendacion");
    verify(mailService, times(1))
        .notificar(miembro1.getNumTelefono(), miembro1.getEmail(), "www.wikipedia.com.ar", "Recomendacion");
    verify(mailService, times(1))
        .notificar(miembro2.getNumTelefono(), miembro1.getEmail(), "www.wikipedia.com.ar", "Recomendacion");
  }

}
 */