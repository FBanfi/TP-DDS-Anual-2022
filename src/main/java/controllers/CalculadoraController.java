
package controllers;

import domain.organizaciones.Area;
import domain.organizaciones.Miembro;
import domain.organizaciones.Organizacion;
import domain.organizaciones.SectorTerritorial;
import domain.organizaciones.cuenta.Cuenta;
import domain.organizaciones.cuenta.TipoCuenta;
import domain.organizaciones.repositorios.RepositorioMiembros;
import domain.organizaciones.repositorios.RepositorioOrganizaciones;
import domain.organizaciones.repositorios.RepositorioSectoresTerritoriales;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.*;
import java.util.stream.Collectors;

public class CalculadoraController extends Controller {
  public void metodos(HandlebarsTemplateEngine engine) {
    Spark.path("/calculadora", () -> {
      Spark.get("/organizacion", (req, res) -> this.mostrarCalculadoraOrganizaciones(req, res), engine);
      Spark.post("/organizacion", (req, res) -> this.calcularHCOrganizacion(req, res), engine);
      Spark.get("/miembro", (req, res) -> this.mostrarCalculadoraMiembros(req, res), engine);
      Spark.post("/miembro", (req, res) -> this.calcularHCMiembro(req, res), engine);
      Spark.get("/sector-territorial", (req, res) -> this.mostrarCalculadoraSectorTerritorial(req, res), engine);
      Spark.post("/sector-territorial", (req, res) -> this.calcularHCSectorTerritorial(req, res), engine);
    });
  }
  public ModelAndView mostrarCalculadoraOrganizaciones(Request req, Response res) {

    if (!seLogueo(req)) {
      res.redirect("/login?origin=/calculadora/organizacion");
      return null;
    }

    Map<String, Object> model = new HashMap<>();
    String org = req.queryParams("organizacion");
    Organizacion organizacion;
    List<Organizacion> organizaciones  = RepositorioOrganizaciones.instance().todos();
    model.put("organizaciones", organizaciones);

    if (org != null) {
      organizacion = RepositorioOrganizaciones.instance().buscar(Integer.parseInt(org));
      model.put("areas", organizacion.getAreas());
      model.put("org", org);
    }

    prepararEncabezado(model, req, res);
    return new ModelAndView(model, "calculadora-organizaciones.html.hbs");
  }

  public ModelAndView mostrarCalculadoraMiembros(Request req, Response res) {

    if (!seLogueo(req)) {
      res.redirect("/login?origin=/calculadora/miembro");
      return null;
    }

    Cuenta cuenta = getCuenta(req);
    if(!cuenta.esTipoCuenta(TipoCuenta.MIEMBRO)) {
      res.redirect("/unauthorized");
      return null;
    }

    Map<String, Object> model = new HashMap<>();
    String org = req.queryParams("organizacion" );
    Miembro miembro = cuenta.getMiembro();
    List<Organizacion> organizaciones  = miembro.getOrganizaciones();
    model.put("organizaciones", organizaciones);
    model.put("miembro", miembro);

    prepararEncabezado(model, req, res);
    return new ModelAndView(model, "calculadora-miembros.html.hbs");
  }

  public ModelAndView mostrarCalculadoraSectorTerritorial(Request req, Response res) {

    if (!seLogueo(req)) {
      res.redirect("/login?origin=/calculadora/sector-territorial");
      return null;
    }

    Map<String, Object> model = new HashMap<>();
    String nombre = req.queryParams("nombre_sector");

    if (nombre != null) {
      SectorTerritorial sector = RepositorioSectoresTerritoriales.instance().obtenerSector(nombre).get(0);
      model.put("organizaciones", sector.getOrganizaciones());
      model.put("nombreSector", nombre);
    }

    prepararEncabezado(model, req, res);
    return new ModelAndView(model, "calculadora-sector-territorial.html.hbs");
  }

  public ModelAndView calcularHCSectorTerritorial(Request request, Response response){
    String nombreSector = request.queryParams("nombreSector");
    int idOrg = Integer.parseInt(request.queryParams("org"));
    String hc = request.queryParams("hc");
    Map<String, Object> model = new HashMap<>();

    try {
      SectorTerritorial sector = RepositorioSectoresTerritoriales.instance().obtenerSector(nombreSector).get(0);
      List<Organizacion> organizaciones = new ArrayList<>();
      organizaciones.addAll(sector.getOrganizaciones());

      double valorHC = 0;

      if (Objects.equals(idOrg, -1)) {
        valorHC = sector.obtenerHCTotal();
      } else {
        Organizacion org = organizaciones.stream().filter(o -> o.getId() == idOrg).collect(Collectors.toList()).get(0);
        SectorTerritorial sectorAux = new SectorTerritorial();
        sectorAux.agregarOrganizacion(org);
        valorHC = sectorAux.obtenerHCTotal();
        model.put("org", org.getNombre());
      }

      model.put("valorHC", valorHC);
      model.put("nombreSector", nombreSector);

    } catch (NoSuchElementException e) {
      response.status(400);

    }

    prepararEncabezado(model, request, response);
    return new ModelAndView(model, "calculadora-resultado-sector-territorial.html.hbs");
  }
  
  public ModelAndView calcularHCOrganizacion(Request request, Response response) {
    String org = request.queryParams("org");
    String areaSeleccionada = request.queryParams("area");
    String tipoCalculo = request.queryParams("Tipo Calculo");
    Map<String, Object> model = new HashMap<>();

    try {
      Organizacion organizacion = RepositorioOrganizaciones.instance().buscar(Integer.parseInt(org));
      List<Area> areas = new ArrayList<>();
      areas.addAll(organizacion.getAreas());

      double valorHC = 0;
      String nombreArea;
      if (Objects.equals(areaSeleccionada, "Todas")) {
        nombreArea = "Todas";
        valorHC = organizacion.obtenerHC();
      } else {
        Area area = areas.stream()
            .filter(a -> a.sameId(Integer.parseInt(areaSeleccionada)))
            .collect(Collectors.toList()).get(0);
        Organizacion orgAux = new Organizacion();
        orgAux.agregarArea(area);
        valorHC = orgAux.obtenerHC();
        nombreArea = area.getNombre();
      }

      model.put("valorHC", valorHC);
      model.put("organizacion", organizacion);
      model.put("nombreArea", nombreArea);
      model.put("tipo_calculo", tipoCalculo);

    } catch (NoSuchElementException e) {
      response.status(400);

    }

    prepararEncabezado(model, request, response);
    return new ModelAndView(model, "calculadora-resultado-organizaciones.html.hbs");
  }

  public ModelAndView calcularHCMiembro(Request req, Response res) {

    int idMiembro = Integer.parseInt(req.queryParams("miembro"));
    int idOrganizacion = Integer.parseInt(req.queryParams("organizacion"));
    Map<String, Object> model = new HashMap<>();

    try {
      Organizacion organizacion = RepositorioOrganizaciones.instance().buscar(idOrganizacion);
      Miembro miembro = RepositorioMiembros.instance().buscar(idMiembro);
      double valorHC = miembro.obtenerHCen(organizacion);
      model.put("valorHC", valorHC);
      model.put("nombreMiembro", miembro.getNombreCompleto());
      model.put("nombreOrganizacion", organizacion.getNombre());
    } catch (NoSuchElementException e) {
      res.status(400);
    }

    prepararEncabezado(model, req, res);
    return new ModelAndView(model, "calculadora-resultado-miembros.html.hbs");
  }
}