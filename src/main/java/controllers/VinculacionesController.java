package controllers;

import domain.organizaciones.Area;
import domain.organizaciones.Miembro;
import domain.organizaciones.Organizacion;
import domain.organizaciones.SolicitudDeVinculo;
import domain.organizaciones.cuenta.Cuenta;
import domain.organizaciones.repositorios.RepositorioCuentas;
import domain.organizaciones.repositorios.RepositorioOrganizaciones;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class VinculacionesController extends Controller {

  public void metodos(HandlebarsTemplateEngine engine) {
    Spark.get("/vinculaciones/nueva", (req, res) -> this.mostrarNuevaVinculacion(req, res), engine);
    Spark.post("/vinculaciones/nueva", (req, res) -> this.proponerVinculacion(req, res), engine);
    Spark.get("/vinculaciones/pendientes", (req, res) -> this.mostrarVinculaciones(req, res), engine);
    Spark.post("/vinculaciones/:id/eliminacion", (req, res) -> this.eliminarVinculacion(req, res), engine);
    Spark.get("/vinculaciones/:id/aceptacion", (req, res) -> this.aceptarVinculacion(req, res), engine);
  }

  public ModelAndView mostrarNuevaVinculacion(Request req, Response res) {
    Map<String, Object> model = new HashMap<>();
    String org = req.queryParams("organizacion");

    if (!seLogueo(req)) {
      res.redirect("/login?origin=/vinculaciones/nueva");
      return null;
    }

    if (org != null) {
      Organizacion organizacion = RepositorioOrganizaciones.instance().buscar(Integer.parseInt(org));
      model.put("ValorOpcionOrg", organizacion.getNombre());
      model.put("idValorOpcionOrg", organizacion.getId());
      model.put("areas", organizacion.getAreas());
    }

    model.put("organizaciones", RepositorioOrganizaciones.instance().todos());
    prepararEncabezado(model, req, res);
    return new ModelAndView(model, "nueva-vinculacion.html.hbs");
  }

  public ModelAndView proponerVinculacion(Request req, Response res) {
    Integer idOrg = Integer.parseInt(req.queryParams("organizacion"));
    Integer area = Integer.parseInt(req.queryParams("area"));
    Cuenta cuenta = RepositorioCuentas.instance().obtenerCuenta(req.session().attribute("cuenta_id"));
    Miembro miembre = cuenta.getMiembro();
    Map<String, Object> model = new HashMap<>();

    Organizacion org =  RepositorioOrganizaciones.instance().buscar(idOrg);
    Area areaAux = org.getAreas().stream().filter(a -> a.sameId(area)).collect(Collectors.toList()).get(0);

    miembre.solicitarVinculoConOrganizacion(org, areaAux);

    withTransaction(() -> {
      RepositorioOrganizaciones.instance().actualizar(org);
    });

    prepararEncabezado(model, req, res);
    return new ModelAndView(model, "miembro-vinculacion-creada.html.hbs");
  }

  public ModelAndView mostrarVinculaciones(Request req, Response res) {
    Map<String, Object> model = new HashMap<>();

    if (!seLogueo(req)) {
      res.redirect("/login?origin=/vinculaciones/pendientes");
      return null;
    }
    Cuenta cuenta = RepositorioCuentas.instance().obtenerCuenta(req.session().attribute("cuenta_id"));
    Organizacion org = cuenta.getOrganizacion();

    model.put("solicitudesPendientes", org.getSolicitudPendientes());

    prepararEncabezado(model, req, res);
    return new ModelAndView(model, "vinculaciones-pendientes.html.hbs");
  }

  public ModelAndView aceptarVinculacion(Request req, Response res) {
    Cuenta cuenta = RepositorioCuentas.instance().obtenerCuenta(req.session().attribute("cuenta_id"));
    Organizacion org = cuenta.getOrganizacion();
    SolicitudDeVinculo solicitudDeVinculo = org.getSolicitudPendientes().stream().filter(s -> s.sameId(Integer.parseInt(req.params("id")))).collect(Collectors.toList()).get(0);

    withTransaction(() -> {
        org.aceptarMiembro(solicitudDeVinculo);
    });

    res.redirect("/vinculaciones/pendientes");
    return null;
  }

  public ModelAndView eliminarVinculacion(Request req, Response res) {
    Cuenta cuenta = RepositorioCuentas.instance().obtenerCuenta(req.session().attribute("cuenta_id"));
    Organizacion org = cuenta.getOrganizacion();
    SolicitudDeVinculo solicitudDeVinculo = org.getSolicitudPendientes().stream().filter(s -> s.sameId(Integer.parseInt(req.params("id")))).collect(Collectors.toList()).get(0);

    withTransaction(() -> {
      org.rechazarSolicitud(solicitudDeVinculo);
    });

    res.redirect("/vinculaciones/pendientes");

    return null;
  }

}
