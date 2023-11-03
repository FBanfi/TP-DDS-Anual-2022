package main;

import domain.mediciones.*;
import domain.organizaciones.*;
import domain.organizaciones.cuenta.Cuenta;
import domain.organizaciones.cuenta.TipoCuenta;
import domain.organizaciones.enums.ClasificacionOrganizacion;
import domain.organizaciones.enums.RazonSocial;
import domain.organizaciones.enums.TipoDocumento;
import domain.organizaciones.enums.TipoOrganizacion;
import domain.services.distanciaAPI.DistanciaService;
import domain.services.distanciaAPI.GeoddsService;
import domain.transporte.MedioDeTransporte;
import domain.transporte.TransportePublico;
import domain.transporte.Vehiculo;
import domain.transporte.enums.TipoVehiculo;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class Bootstrap implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {
  DistanciaService distanciaService = new GeoddsService();
  public static void main(String[] args) {
    new Bootstrap().run();
  }

  public void run() {

    withTransaction(() -> {
      //Dejo estos de ejemplo para, desp lo tenemos que sacar cuando persistamos lo que realmente queremos
      //RepositorioCumpleanios.instancia.agregar(new Cumpleanios("dblandit", LocalDate.now()));
      //persist(new Usuario("fran", "1234"));
      Organizacion org = new Organizacion("2Diseños",
          RazonSocial.SOCIEDAD_ANONIMA, TipoOrganizacion.EMPRESA,
          new Ubicacion("Av Cochabamba", 1, 3),
          ClasificacionOrganizacion.EMPRESA_DEL_SECTOR_PRIMARIO);

      Area area1 = new Area("Area Anonima1");
      Area area2 = new Area("Area Anonima2");

      PuestoDeTrabajo puestoMiembro = new PuestoDeTrabajo(org, area1);
      PuestoDeTrabajo puestoMiembro1 = new PuestoDeTrabajo(org, area2);
      Miembro miembroCuenta =
          new Miembro("Franquito", new Documento(TipoDocumento.DNI, "14234234"), puestoMiembro);
      Miembro miembro = new Miembro("Cacho", new Documento(TipoDocumento.DNI, "14234234"), puestoMiembro);
      Miembro miembro1 = new Miembro("Tito", new Documento(TipoDocumento.DNI, "142342ss"), puestoMiembro1);

      SolicitudDeVinculo solicitudMiembroCuenta = new SolicitudDeVinculo(area1, miembroCuenta);
      SolicitudDeVinculo solicitudMiembro = new SolicitudDeVinculo(area1, miembro);
      SolicitudDeVinculo solicitudMiembro1 = new SolicitudDeVinculo(area2, miembro1);

      org.agregarArea(area1);
      org.agregarArea(area2);

      org.aceptarMiembro(solicitudMiembroCuenta);
      org.aceptarMiembro(solicitudMiembro);
      org.aceptarMiembro(solicitudMiembro1);

      SectorTerritorial sector = new SectorTerritorial("sector1", TipoDeSector.MUNICIPIO);
      sector.agregarOrganizacion(org);


      Cuenta cuentaMiembro =
          new Cuenta("Franquito", "Miracomomemeneo1!", TipoCuenta.MIEMBRO, miembroCuenta);
      Cuenta cuentaOrg =
          new Cuenta("Luquitas", "cacho", TipoCuenta.ORGANIZACION, org);
      Cuenta cuentaAgente =
          new Cuenta("Juancito", "Keloke", TipoCuenta.AGENTE_SECTORIAL, sector);


      // TRAYECTO
      List<Tramo> tramos1 = new ArrayList<>();

        Vehiculo vehiculo = new Vehiculo(TipoVehiculo.AUTO,
            new Consumo(10, TipoConsumo.NAFTA,
                new Periodicidad(TipoPeriodicidad.MENSUAL, "01/2022"),
                new FactorEmision(Unidad.LT)
            )
        );
      MedioDeTransporte transportePublico = new TransportePublico(vehiculo, "Auteto de Juanceto");

      Vehiculo vehiculo1 = new Vehiculo(TipoVehiculo.COLECTIVO,
          new Consumo(10, TipoConsumo.NAFTA,
              new Periodicidad(TipoPeriodicidad.MENSUAL, "01/2022"),
              new FactorEmision(Unidad.LT)
        )
      );

      Parada parada3 = new Parada(distanciaService,
          "villa lugano", new Ubicacion("mozart", 0 , 3100));
      Parada parada2 = new Parada(distanciaService,
          "chimborazo",new Ubicacion("mozart", 0 , 2700), parada3);
      Parada parada1 = new Parada(distanciaService,
          "cochabamba", new Ubicacion("mozart", 0 , 2300), parada2);
      List<Parada> tramoIda = new ArrayList<>();
      tramoIda.add(parada1);
      tramoIda.add(parada2);
      tramoIda.add(parada3);

      Parada parada5 = new Parada(distanciaService,
          "Balaustro",new Ubicacion("mozart", 0 , 2700), parada1);
      Parada parada4 = new Parada(distanciaService,
          "Fochetta", new Ubicacion("mozart", 0 , 2300), parada5);

      List<Parada> tramoVuelta = new ArrayList<>();
      tramoVuelta.add(parada3);
      tramoVuelta.add(parada4);
      tramoVuelta.add(parada5);
      tramoVuelta.add(parada1);

      MedioDeTransporte transportePublico1 = new TransportePublico(vehiculo, "La Scaloneta", tramoIda, tramoVuelta);

      tramos1.add(
          new Tramo(transportePublico,
            new Ubicacion("calle",1,2),
            new Ubicacion("calle",2,3),
            distanciaService)
      );

      Trayecto trayecto = new Trayecto(tramos1, "Trayecteto", org);

      miembroCuenta.agregarTrayecto(trayecto);
      miembro1.agregarTrayecto(trayecto);
      miembro.agregarTrayecto(trayecto);

      DetalleReporte detalleReporte = new DetalleReporte("unTipo", 10);
      List<DetalleReporte> detallesReportes = new ArrayList<>();
      detallesReportes.add(detalleReporte);

      ReporteOrganizacion reporteOrganizacion = new ReporteOrganizacion(CriterioReporte.EVOLUCION, LocalDate.now(), detallesReportes, org);
      persist(org);
      persist(distanciaService);
      persist(transportePublico1);
      persist(transportePublico);
      tramos1.forEach(tramo -> {persist(tramo);});
      persist(trayecto);
      persist(miembro);
      persist(miembro1);
      persist(miembroCuenta);
      persist(cuentaMiembro);
      persist(cuentaOrg);
      persist(cuentaAgente);
      persist(detalleReporte);
      persist(reporteOrganizacion);
      persist(sector);
    });
  }

}