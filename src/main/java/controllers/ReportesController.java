package controllers;

import domain.mediciones.CriterioReporte;
import domain.mediciones.ReporteOrganizacion;
import domain.mediciones.ReporteSectorTerritorial;
import domain.organizaciones.Organizacion;
import domain.organizaciones.SectorTerritorial;
import domain.organizaciones.cuenta.Cuenta;
import domain.organizaciones.cuenta.TipoCuenta;
import domain.organizaciones.repositorios.RepositorioCuentas;
import domain.organizaciones.repositorios.RepositorioOrganizaciones;
import domain.organizaciones.repositorios.RepositorioReportes;
import domain.organizaciones.repositorios.RepositorioSectoresTerritoriales;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportesController extends Controller {

  public void metodos(HandlebarsTemplateEngine engine) {
    Spark.get("/reportes", (req, res) -> this.mostrarReportes(req, res), engine);
    Spark.get("/reportes/accion/consulta", (req, res) -> this.realizarConsulta(req, res), engine);
    Spark.get("/reportes/accion/generacion", (req, res) -> this.realizarGeneracion(req, res), engine);
    Spark.get("/reportes/generacion/exito", (req, res) -> this.mostrarExito(req, res), engine);
    Spark.get("/reportes/generacion/error", (req, res) -> this.mostrarError(req, res), engine);
  }

  public ModelAndView mostrarReportes(Request req, Response res) {
    Map<String, Object> model = new HashMap<>();

    if (!seLogueo(req)) {
      res.redirect("/login?origin=/reportes");
      return null;
    }
    prepararEncabezado(model, req, res);
    return new ModelAndView(model, "menu-reportes.html.hbs");
  }

  public ModelAndView realizarConsulta(Request req, Response res) {
    Map<String, Object> model = new HashMap<>();

    if (!seLogueo(req)) {
      res.redirect("/login?origin=/reportes");
      return null;
    }

    prepararEncabezado(model, req, res);
    model.put("tipo", req.queryParams("tipo"));

    //SWITCH
    switch (req.queryParams("tipo")) {
      case "HCTotalSectorTerritorial":
        model.put("NombreReporte", "HC Total Sector Territorial");
        model.put("TipoEvolucion", "Sector Territorial");

        if (req.queryParams("valor") == null) {
          model.put("evoluciones", RepositorioSectoresTerritoriales.instance().todos());
          model.put("reportes", RepositorioReportes.instance().obtenerReportesSectores(CriterioReporte.HC_TOTAL));
        } else {
          String idOrg = req.queryParams("valor");
          SectorTerritorial orga = RepositorioSectoresTerritoriales.instance().buscar(Integer.parseInt(idOrg));
          model.put("idValorOpcion", orga.getId());
          model.put("ValorOpcion", orga.getNombre());
          model.put("reportes", RepositorioReportes.instance().obtenerReportesSectores(CriterioReporte.COMPOSICION).stream().filter(o -> o.sameId(Integer.parseInt(idOrg))).collect(Collectors.toList()));
          return new ModelAndView(model, "vista-reporte-consulta.html.hbs");
        }

        return new ModelAndView(model, "vista-reporte-consulta.html.hbs");

      case "HCTotalOrganizacion":
        model.put("NombreReporte", "HC Total Organizacion");
        model.put("TipoEvolucion", "Organizacion");

        if (req.queryParams("valor") == null) {
          model.put("evoluciones", RepositorioOrganizaciones.instance().todos());
          model.put("reportes", RepositorioReportes.instance().obtenerReportesOrganizaciones(CriterioReporte.HC_TOTAL));
        } else {
          String idOrg = req.queryParams("valor");
          Organizacion orga = RepositorioOrganizaciones.instance().buscar(Integer.parseInt(idOrg));
          model.put("idValorOpcion", orga.getId());
          model.put("ValorOpcion", orga.getNombre());
          model.put("reportes", RepositorioReportes.instance().obtenerReportesOrganizaciones(CriterioReporte.COMPOSICION).stream().filter(o -> o.sameId(Integer.parseInt(idOrg))).collect(Collectors.toList()));
          return new ModelAndView(model, "vista-reporte-consulta.html.hbs");
        }

        return new ModelAndView(model, "vista-reporte-consulta.html.hbs");

      case "CompHCTotalSectorTerritorial":
        model.put("NombreReporte", "Composicion HC Total Sector Territorial");
        model.put("TipoEvolucion", "Sector Territorial");

        if (req.queryParams("valor") == null) {
          model.put("evoluciones", RepositorioSectoresTerritoriales.instance().todos());
          model.put("reportes", RepositorioReportes.instance().obtenerReportesSectores(CriterioReporte.COMPOSICION));
        } else {
          String idOrg = req.queryParams("valor");
          SectorTerritorial orga = RepositorioSectoresTerritoriales.instance().buscar(Integer.parseInt(idOrg));
          model.put("idValorOpcion", orga.getId());
          model.put("ValorOpcion", orga.getNombre());
          model.put("reportes", RepositorioReportes.instance().obtenerReportesSectores(CriterioReporte.COMPOSICION).stream().filter(o -> o.sameId(Integer.parseInt(idOrg))).collect(Collectors.toList()));
          return new ModelAndView(model, "vista-reporte-consulta.html.hbs");
        }

        return new ModelAndView(model, "vista-reporte-consulta.html.hbs");

      case "CompHCTotalOrganizacion":
        model.put("NombreReporte", "Composicion HC Total Organizacion");
        model.put("TipoEvolucion", "Organizacion");

        if (req.queryParams("valor") == null) {
          model.put("evoluciones", RepositorioOrganizaciones.instance().todos());
          model.put("reportes", RepositorioReportes.instance().obtenerReportesOrganizaciones(CriterioReporte.COMPOSICION));
        } else {
          String idOrg = req.queryParams("valor");
          Organizacion orga = RepositorioOrganizaciones.instance().buscar(Integer.parseInt(idOrg));
          model.put("idValorOpcion", orga.getId());
          model.put("ValorOpcion", orga.getNombre());
          model.put("reportes", RepositorioReportes.instance().obtenerReportesOrganizaciones(CriterioReporte.COMPOSICION).stream().filter(o -> o.sameId(Integer.parseInt(idOrg))).collect(Collectors.toList()));
          return new ModelAndView(model, "vista-reporte-consulta.html.hbs");
        }

        return new ModelAndView(model, "vista-reporte-consulta.html.hbs");

      case "EvolHCTotalSectorTerritorial":  //COMPLETAR
        model.put("NombreReporte", "Evolucion HC Total Sector Territorial");
        model.put("TipoEvolucion", "Sector Territorial");

        if (req.queryParams("valor") == null) {
          model.put("evoluciones", RepositorioSectoresTerritoriales.instance().todos());
          model.put("reportes", RepositorioReportes.instance().obtenerReportesSectores());
        } else {
          String idSector = req.queryParams("valor");
          SectorTerritorial sect = RepositorioSectoresTerritoriales.instance().buscar(Integer.parseInt(idSector));
          model.put("idValorOpcion", sect.getId());
          model.put("ValorOpcion", sect.getNombre());
          List<ReporteSectorTerritorial> repsSect = RepositorioReportes.instance().obtenerReportesSectores().stream().filter(o -> o.sameId(Integer.parseInt(idSector))).collect(Collectors.toList());
          model.put("reportes", repsSect.stream().map(r -> r.getSector().obtenerEvolucionEntreFechas(LocalDate.parse(req.queryParams("desde")), LocalDate.parse(req.queryParams("hasta")))).collect(Collectors.toList()));
          return new ModelAndView(model, "vista-reporte-consulta-evolucion.html.hbs");
        }

        return new ModelAndView(model, "vista-reporte-consulta-evolucion.html.hbs");

      case "EvolHCTotalOrganizacion":   //COMPLETAR
        model.put("NombreReporte", "Evolucion HC Total Organizacion");
        model.put("TipoEvolucion", "Organizacion");

        if (req.queryParams("valor") == null) {
          model.put("evoluciones", RepositorioOrganizaciones.instance().todos());
          model.put("reportes", RepositorioReportes.instance().obtenerReportesOrganizaciones());
        } else {
          String idOrg = req.queryParams("valor");
          Organizacion orga = RepositorioOrganizaciones.instance().buscar(Integer.parseInt(idOrg));
          model.put("idValorOpcion", orga.getId());
          model.put("ValorOpcion", orga.getNombre());
          List<ReporteOrganizacion> repsOrg = RepositorioReportes.instance().obtenerReportesOrganizaciones().stream().filter(o -> o.sameId(Integer.parseInt(idOrg))).collect(Collectors.toList());
          model.put("reportes", repsOrg.stream().map(r -> r.getOrg().obtenerEvolucionEntreFechas(LocalDate.parse(req.queryParams("desde")), LocalDate.parse(req.queryParams("hasta")))).collect(Collectors.toList()));
          return new ModelAndView(model, "vista-reporte-consulta-evolucion.html.hbs");
        }

        return new ModelAndView(model, "vista-reporte-consulta-evolucion.html.hbs");
    }

    return new ModelAndView(model, "agenteSectorial-reportes-HCTotalSectorTerritorial-generacion.html.hbs");
  }

  public ModelAndView realizarGeneracion(Request req, Response res) {
    Map<String, Object> model = new HashMap<>();
    Cuenta cuenta = RepositorioCuentas.instance().obtenerCuenta(req.session().attribute("cuenta_id"));

    if (!seLogueo(req)) {
      res.redirect("/login?origin=/reportes");
      return null;
    }

    prepararEncabezado(model, req, res);

    //SWITCH
    switch (req.queryParams("tipo")) {
      case "HCTotalSectorTerritorial":
        if (cuenta.getTipoCuenta() != TipoCuenta.AGENTE_SECTORIAL)
          res.redirect("/reportes/generacion/error");

        withTransaction(() -> {
          RepositorioReportes.instance().agregarReporte(cuenta.getSectorTerritorial().obtenerReporteHCTotal());
        });
        res.redirect("/reportes/generacion/exito");
        break;
      case "HCTotalOrganizacion":
        if (cuenta.getTipoCuenta() != TipoCuenta.ORGANIZACION)
          res.redirect("/reportes/generacion/error");

        withTransaction(() -> {
          RepositorioReportes.instance().agregarReporte(cuenta.getOrganizacion().obtenerReporteHCTotal());
        });
        res.redirect("/reportes/generacion/exito");
        break;
      case "CompHCTotalSectorTerritorial":
        if (cuenta.getTipoCuenta() != TipoCuenta.AGENTE_SECTORIAL)
          res.redirect("/reportes/generacion/error");

        withTransaction(() -> {
          RepositorioReportes.instance().agregarReporte(cuenta.getSectorTerritorial().obtenerReporteHCComposicion());
        });
        res.redirect("/reportes/generacion/exito");
        break;
      case "CompHCTotalOrganizacion":
        if (cuenta.getTipoCuenta() != TipoCuenta.ORGANIZACION)
          res.redirect("/reportes/generacion/error");

        withTransaction(() -> {
          RepositorioReportes.instance().agregarReporte(cuenta.getOrganizacion().obtenerReporteHCComposicion());
        });
        res.redirect("/reportes/generacion/exito");
        break;
    }

    return null;
  }

  private ModelAndView mostrarError(Request req, Response res) {
    Map<String, Object> model = new HashMap<>();
    prepararEncabezado(model, req, res);
    return new ModelAndView(model, "operacion-creada-sin-exito.html.hbs");
  }

  private ModelAndView mostrarExito(Request req, Response res) {
    Map<String, Object> model = new HashMap<>();
    prepararEncabezado(model, req, res);
    return new ModelAndView(model, "operacion-creada-con-exito.html.hbs");
  }

}