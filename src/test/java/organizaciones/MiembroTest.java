package organizaciones;

import domain.mediciones.*;
import domain.organizaciones.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import organizaciones.mothers.MotherOrganizacion;
import domain.organizaciones.enums.TipoDocumento;
import domain.services.distanciaAPI.GeoddsService;
import domain.services.distanciaAPI.DistanciaService;
import domain.sugerenciasYnotificaciones.NotificacionTwilio;
import domain.sugerenciasYnotificaciones.NotificacionJavaMail;
import domain.transporte.MedioDeTransporte;
import domain.transporte.TransporteParticular;
import domain.transporte.TransportePublico;
import domain.transporte.Vehiculo;
import domain.transporte.enums.TipoVehiculo;

import java.util.*;

import static org.mockito.Mockito.*;

class MiembroTest {
  Area area;
  Organizacion organizacion;
  Organizacion organizacion2;
  PuestoDeTrabajo puestoTrabajo;
  Miembro miembro;
  Miembro miembro2;
  NotificacionJavaMail mailService;
  NotificacionTwilio whatsappService;
  DistanciaService distanciaService;

  @BeforeEach
  void init() {
    area = new Area("AreaPrueba");
    MotherOrganizacion motherOrganizacion = new MotherOrganizacion();
    organizacion = motherOrganizacion.crearOrganizacionSociedadAnonima();
    organizacion2 = motherOrganizacion.crearOrganizacionSociedadAccionesSimplificadas();
    puestoTrabajo = new PuestoDeTrabajo(organizacion , area);
    whatsappService = mock(NotificacionTwilio.class);
    distanciaService = mock(GeoddsService.class);
    mailService = mock(NotificacionJavaMail.class);
    miembro = new Miembro("test",new Documento(TipoDocumento.DNI, "0"), puestoTrabajo);
    miembro.agregarNotificacion(whatsappService);
    miembro.agregarNotificacion(mailService);
    miembro.setNumTelefono("+5491158470891");
    miembro.setEmail("cachoAntune@gmail.com");
    miembro2 = new Miembro("test",new Documento(TipoDocumento.DNI, "0"), puestoTrabajo);
    miembro2.setNumTelefono("+5491158470891");
    miembro2.setEmail("cachitoAntune@gmail.com");
    miembro2.agregarNotificacion(whatsappService);
    miembro2.agregarNotificacion(mailService);
  }

  @Test
  void unMiembroEstaEnLaMismaOrganizacionQueOtroMiembro(){
    Assertions.assertTrue(miembro.estaEnLaMismaOrganizacionQue(miembro2));
  }

  @Test
  void unMiembroNoEstaEnLaMismaOrganizacionQueOtroMiembro(){
    Miembro miembro3 =  new Miembro("test",new Documento(TipoDocumento.DNI, "0"), new PuestoDeTrabajo(organizacion2, area));
    Assertions.assertTrue(!miembro.estaEnLaMismaOrganizacionQue(miembro3));
  }

  @Test
  void sePuedeVincularMiembroConOrganizacion() {
    Assertions.assertDoesNotThrow( () -> {
      miembro.solicitarVinculoConOrganizacion(organizacion, area);
      organizacion.aceptarSolicitudes();});
    Assertions.assertTrue(miembro.perteneceAOrganizacion(organizacion));
  }

  @Test
  void unMiembroPuedeCompartirUnTrayectoConTransporteParticularConOtroMiembro() {

    Trayecto trayecto = new Trayecto(obtenerListaDeTramos(obtenerTransporteParticular()));
    miembro.agregarTrayecto(trayecto);
    miembro.compartirTrayectoCon(trayecto, miembro2);
    
    Assertions.assertTrue(miembro2.getTrayectos().contains(trayecto));
  }

  @Test
  void unMiembroNoPuedeCompartirUnTrayectoConTransportePublicoConOtroMiembro() {

    Trayecto trayecto = new Trayecto(obtenerListaDeTramos(obtenerTransportePublico()));
    miembro.agregarTrayecto(trayecto);
    Assertions.assertThrows(RuntimeException.class , ()-> miembro.compartirTrayectoCon(trayecto, miembro2));
    Assertions.assertTrue(!miembro2.getTrayectos().contains(trayecto));
  }

  @Test
  void unaMiembroPuedeRecibirRecomendacion(){
    String guiaRecomendacion = "https://aulasvirtuales.frba.utn.edu.ar/";

    miembro.recibirRecomendacion(guiaRecomendacion);
    verify(whatsappService, times(1)).notificar(guiaRecomendacion, miembro.getNumTelefono(), miembro.getEmail(), "Recomendacion");
    verify(mailService, times(1)).notificar(guiaRecomendacion, miembro.getNumTelefono(), miembro.getEmail(), "Recomendacion");
  }

  private List<Tramo> obtenerListaDeTramos(MedioDeTransporte transporte) {
    List<Tramo> tramos = new ArrayList<>();
    tramos.add(new Tramo(transporte,
        new Ubicacion("Rabufetti", 1, 1),
        new Ubicacion("Rabufetti", 1, 100),
        new GeoddsService()));

    return tramos;
  }

  private TransporteParticular obtenerTransporteParticular() {
    return new TransporteParticular(new Vehiculo(TipoVehiculo.AUTO, crearConsumo(TipoConsumo.NAFTA)));
  }

  private TransportePublico obtenerTransportePublico() {
    return new TransportePublico(new Vehiculo(TipoVehiculo.COLECTIVO, crearConsumo(TipoConsumo.NAFTA)));
  }

  private Consumo crearConsumo(TipoConsumo combustible){
    return new Consumo(10, combustible, new Periodicidad(TipoPeriodicidad.MENSUAL, "01/2022"), new FactorEmision(Unidad.LT));
  }

}