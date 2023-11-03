package services.mailAPI;

import domain.sugerenciasYnotificaciones.NotificacionJavaMail;
import org.junit.jupiter.api.Test;

class ServicioMailTest {

  @Test
  void puedeEnviarMail() {
    new NotificacionJavaMail().enviarEmail("cachoantune@gmail", "prueba", "probando");
  }
}