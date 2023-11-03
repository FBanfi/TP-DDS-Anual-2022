package persistencia;

import domain.mediciones.*;
import domain.organizaciones.*;

import domain.sugerenciasYnotificaciones.NotificacionTwilio;
import domain.sugerenciasYnotificaciones.NotificacionJavaMail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import domain.organizaciones.enums.ClasificacionOrganizacion;
import domain.organizaciones.enums.TipoOrganizacion;
import domain.organizaciones.repositorios.Repositorio;
import domain.organizaciones.repositorios.RepositorioOrganizaciones;
import domain.organizaciones.repositorios.RepositorioReportes;
import domain.services.distanciaAPI.DistanciaService;
import domain.services.distanciaAPI.GeoddsService;
import organizaciones.mothers.*;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.mock;

public class TestPersistencia extends AbstractPersistenceTest implements WithGlobalEntityManager {
  NotificacionJavaMail mailService = new NotificacionJavaMail();
  NotificacionTwilio whatsappService = new NotificacionTwilio();
  DistanciaService distanciaService = mock(GeoddsService.class);

  @Override
  @BeforeEach
  public void setup() {
    super.setup();
  }

  @Override
  @AfterEach
  public void tearDown() {
    super.tearDown();
  }

  @Test
  public void sePuedePersistirOrganizacionYRecuperarla() throws Exception {
    Organizacion o = (new MotherOrganizacion()).crearOrganizacionSociedadAnonima();
    RepositorioOrganizaciones repo = RepositorioOrganizaciones.instance();
    repo.agregar(o);
    Assertions.assertEquals(1, repo.cantidad());
  }

  @Test
  public void sePuedePersistirConsumoYRecuperarla() throws Exception {
    withTransaction(() -> {
      Consumo consumo = (new MotherConsumo()).crearConsumo(TipoConsumo.NAFTA);
      Repositorio<Consumo> repo = new Repositorio<>("Consumo");
      repo.agregar(consumo);
      Assertions.assertEquals(1, repo.cantidad());   });
  }

  @Test
  public void recuperarConsumoCompleto() throws Exception {
    withTransaction(() -> {
      Repositorio<Consumo> repo = new Repositorio<>("Consumo");
      Assertions.assertDoesNotThrow(() -> repo.getUltimo().validarTipoConsumo(repo.getUltimo().getTipoConsumo(), new FactorEmision(Unidad.LT)));
    });
  }

  @Test
  public void sePuedePersistirParadaYRecuperarla() throws Exception {
    Parada parada = new MotherParada().crearParadaCon3ParadasAdentro();
    Repositorio<Parada> repo = new Repositorio<>("Parada");
    repo.agregar(parada);
    Assertions.assertEquals(3, repo.cantidad());
  }

  @Test
  public void test() {
    Assertions.assertEquals(Unidad.LT, TipoConsumo.NAFTA.getUnidad());
    Assertions.assertNotEquals(Unidad.LTS, TipoConsumo.NAFTA.getUnidad());
  }

  @Test
  public void sePuedeGenerarReporteHCPorSectoresTerritoriales() {
    Repositorio<SectorTerritorial> repoSectores = new Repositorio<>("SectorTerritorial");
    SectorTerritorial sector1 = new SectorTerritorial(TipoDeSector.MUNICIPIO);

    Organizacion org1 =
        new MotherOrganizacion().crearOrganizacionConMiembros(whatsappService, mailService, distanciaService);
    sector1.agregarOrganizacion(org1);
    repoSectores.agregar(sector1);

    RepositorioReportes repoReporte = RepositorioReportes.instance();
    repoReporte.agregar(sector1.obtenerReporteHCTotal());

    List<ReporteSectorTerritorial> alcanceSectorTerritorial =
        repoReporte.obtenerReporteSectores(sector1, CriterioReporte.HC_TOTAL);

    Assertions.assertEquals(LocalDate.now(), alcanceSectorTerritorial.get(0).getFecha());
    Assertions.assertEquals(20, alcanceSectorTerritorial.get(0).getDetalles().get(0).getValorDetalle());
    Assertions.assertEquals(CriterioReporte.HC_TOTAL,sector1.obtenerReporteHCTotal().getCriterioReporte());
  }

  @Test
  public void sePuedeGenerarReporteHCPorOrganizacion() {
    RepositorioOrganizaciones repoOrg = RepositorioOrganizaciones.instance();
    Organizacion org1 =
        new MotherOrganizacion().crearOrganizacionConMiembros(whatsappService, mailService, distanciaService);
    repoOrg.agregarOrganizacion(org1);

    RepositorioReportes repoReporte = RepositorioReportes.instance();
    ReporteOrganizacion reporte = org1.obtenerReporteHCTotal();
    repoReporte.agregar(reporte);


    List<ReporteOrganizacion> reportes =
        repoReporte.obtenerReporteOrganizacion(org1,CriterioReporte.HC_TOTAL);
    Assertions.assertEquals(LocalDate.now(), reportes.get(0).getFecha());
    Assertions.assertEquals(20, reportes.get(0).getDetalles().get(0).getValorDetalle());
    Assertions.assertEquals(CriterioReporte.HC_TOTAL, reporte.getCriterioReporte());
  }

  @Test
  public void sePuedeGenerarReporteHCPorTipoDeOrganizacion () {
      RepositorioOrganizaciones repoOrg = RepositorioOrganizaciones.instance();
      Organizacion org1 =
          new MotherOrganizacion().crearOrganizacionConMiembros(whatsappService, mailService, distanciaService);
      Organizacion org2 =
          new MotherOrganizacion().crearOrganizacionConMiembros(whatsappService, mailService, distanciaService);
      repoOrg.agregarOrganizacion(org1);
      repoOrg.agregarOrganizacion(org2);
      RepositorioReportes repoReporte = RepositorioReportes.instance();
      ReporteOrganizacion reporte1 = org1.obtenerReporteHCTotal();
      ReporteOrganizacion reporte2 = org1.obtenerReporteHCTotal();
      repoReporte.agregar(reporte1);
      repoReporte.agregar(reporte2);

      List<ReporteOrganizacion> reportes =
          repoReporte
          .obtenerReporteTipoYClasificacionOrganizacion(
              TipoOrganizacion.EMPRESA,
              ClasificacionOrganizacion.EMPRESA_DEL_SECTOR_PRIMARIO,
              CriterioReporte.HC_TOTAL);

      Assertions.assertEquals(2, reportes.size());
      Assertions.assertEquals(LocalDate.now(), reportes.get(0).getFecha());
      Assertions.assertEquals(LocalDate.now(), reportes.get(1).getFecha());
      Assertions.assertEquals(20, reportes.get(0).getDetalles().get(0).getValorDetalle());
      Assertions.assertEquals(20, reportes.get(1).getDetalles().get(0).getValorDetalle());
      Assertions.assertEquals(CriterioReporte.HC_TOTAL, reportes.get(0).getCriterioReporte());
      Assertions.assertEquals(CriterioReporte.HC_TOTAL, reportes.get(1).getCriterioReporte());
    }

  @Test
  public void sePuedeGenerarReporteDeComposicionDeHCTotalDeUnSectorTerritorial () {
    Repositorio<SectorTerritorial> repoSectores = new Repositorio<>("SectorTerritorial");
    SectorTerritorial sector1 = new SectorTerritorial(TipoDeSector.MUNICIPIO);
    Organizacion org1 =
        new MotherOrganizacion().crearOrganizacionConMiembros(whatsappService, mailService, distanciaService);
    Organizacion org2 =
        new MotherOrganizacion().crearOrganizacionConMiembros(whatsappService, mailService, distanciaService);

    sector1.agregarOrganizacion(org1);
    sector1.agregarOrganizacion(org2);
    repoSectores.agregar(sector1);

    RepositorioReportes repoReporte = RepositorioReportes.instance();
    Reporte reporte = sector1.obtenerReporteHCComposicion();
    repoReporte.agregar(reporte);
    List<ReporteSectorTerritorial> reportes = repoReporte.obtenerReporteSectores(sector1, CriterioReporte.COMPOSICION);

    Assertions.assertEquals(LocalDate.now(), reportes.get(0).getFecha());
    Assertions.assertEquals(2, reportes.get(0).getDetalles().size());
    Assertions.assertEquals(20, reportes.get(0).getDetalles().get(0).getValorDetalle());
    Assertions.assertEquals(20, reportes.get(0).getDetalles().get(1).getValorDetalle());
    Assertions.assertEquals(CriterioReporte.COMPOSICION, reporte.getCriterioReporte());
  }

  @Test
  public void sePuedeGenerarReporteDeComposicionDeHCTotalDeUnaOrganizacion () {
    RepositorioOrganizaciones repoOrg = RepositorioOrganizaciones.instance();
    Organizacion org1 =
        new MotherOrganizacion().crearOrganizacionConMiembros(whatsappService, mailService, distanciaService);
    repoOrg.agregarOrganizacion(org1);
    RepositorioReportes repoReporte = RepositorioReportes.instance();
    ReporteOrganizacion reporte = org1.obtenerReporteHCComposicion();
    repoReporte.agregar(reporte);

    List<ReporteOrganizacion> reportes =
        repoReporte.obtenerReporteOrganizacion(org1, CriterioReporte.COMPOSICION);
    Assertions.assertEquals(LocalDate.now(), reportes.get(0).getFecha());
    Assertions.assertEquals(2, reportes.get(0).getDetalles().size());

    Assertions.assertEquals(10, reportes.get(0).getDetalles().get(0).getValorDetalle());
    Assertions.assertEquals(10, reportes.get(0).getDetalles().get(1).getValorDetalle());

    Assertions.assertEquals(CriterioReporte.COMPOSICION, reporte.getCriterioReporte());
  }

  @Test
  public void sePuedeGenerarReporteEvolucionPorOrganizacion() {
    RepositorioOrganizaciones repoOrg = RepositorioOrganizaciones.instance();
    Organizacion org1 =
        new MotherOrganizacion().crearOrganizacionConMiembros(whatsappService, mailService, distanciaService);
    RepositorioReportes repoReporte = RepositorioReportes.instance();

    repoOrg.agregarOrganizacion(org1);
    ReporteOrganizacion reporte1 = org1.obtenerReporteHCTotal();
    repoReporte.agregar(reporte1);
    Miembro miembrito = new MotherMiembro().crearMiembro(org1, org1.getAreas().get(0)); // TIENE 10 DE HC
    miembrito.agregarTrayecto(new MotherTrayecto().crearTrayectoNoCompartido(distanciaService));

    ReporteOrganizacion reporte2 = org1.obtenerReporteHCTotal();

    repoReporte.agregar(reporte2);

    List<ReporteOrganizacion> reportes =
        repoReporte.obtenerReporteOrganizacion(org1,CriterioReporte.HC_TOTAL);
    Assertions.assertEquals(LocalDate.now(), reportes.get(0).getFecha());

    Assertions.assertEquals(20, reportes.get(0).getDetalles().get(0).getValorDetalle());
    Assertions.assertEquals(30, reportes.get(1).getDetalles().get(0).getValorDetalle());

    Assertions.assertEquals(CriterioReporte.HC_TOTAL, reporte1.getCriterioReporte());
    Assertions.assertEquals(CriterioReporte.HC_TOTAL, reporte2.getCriterioReporte());


  }

  @Test
  public void sePuedeGenerarReporteDeEvolucionDeHCTotalDeUnaOrganizacion () {
      RepositorioOrganizaciones repoOrg = RepositorioOrganizaciones.instance();
      Organizacion org1 =
          new MotherOrganizacion().crearOrganizacionConMiembros(whatsappService, mailService, distanciaService);
      RepositorioReportes repoReporte = RepositorioReportes.instance();

      LocalDate desde = LocalDate.of(2022, 10, 1);
      LocalDate hasta = LocalDate.of(2022, 11, 2);

      repoOrg.agregarOrganizacion(org1);
      ReporteOrganizacion reporte1 = org1.obtenerReporteHCTotal();
      reporte1.setFecha(desde);
      repoReporte.agregar(reporte1);
      Miembro miembrito = new MotherMiembro().crearMiembro(org1, org1.getAreas().get(0)); // TIENE 10 DE HC
      miembrito.agregarTrayecto(new MotherTrayecto().crearTrayectoNoCompartido(distanciaService));

      ReporteOrganizacion reporte2 = org1.obtenerReporteHCTotal();
      reporte1.setFecha(hasta);
    reporte2.setFecha(hasta);
      repoReporte.agregar(reporte2);

      int size = org1.obtenerEvolucionEntreFechas(desde, hasta).size();

      Assertions.assertEquals(2, size);
  }

  @Test
  public void sePuedeGenerarReporteDeEvolucionDeHCTotalDeUnSectorTerritorial () {
    Repositorio<SectorTerritorial> repoSectores = new Repositorio<>("SectorTerritorial");
    SectorTerritorial sector1 = new SectorTerritorial();

    RepositorioReportes repoReporte = RepositorioReportes.instance();
    Organizacion org1 =
        new MotherOrganizacion().crearOrganizacionConMiembros(whatsappService, mailService, distanciaService);
    sector1.agregarOrganizacion(org1);
    LocalDate desde = LocalDate.of(2022, 10, 1);
    LocalDate hasta = LocalDate.of(2022, 11, 2);

    repoSectores.agregar(sector1);
    ReporteSectorTerritorial reporte1 = sector1.obtenerReporteHCTotal();
    reporte1.setFecha(desde);
    repoReporte.agregar(reporte1);
    Miembro miembrito = new MotherMiembro().crearMiembro(org1, org1.getAreas().get(0)); // TIENE 10 DE HC
    miembrito.agregarTrayecto(new MotherTrayecto().crearTrayectoNoCompartido(distanciaService));

    ReporteSectorTerritorial reporte2 = sector1.obtenerReporteHCTotal();
    reporte1.setFecha(hasta);
    reporte2.setFecha(hasta);
    repoReporte.agregar(reporte2);

    int size = sector1.obtenerEvolucionEntreFechas(desde, hasta).size();

    Assertions.assertEquals(2, size);
  }

}