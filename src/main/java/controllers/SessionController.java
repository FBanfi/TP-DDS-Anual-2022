package controllers;

import domain.organizaciones.*;
import domain.organizaciones.cuenta.AgenteSectorial;
import domain.organizaciones.cuenta.Cuenta;
import domain.organizaciones.cuenta.TipoCuenta;
import domain.organizaciones.enums.ClasificacionOrganizacion;
import domain.organizaciones.enums.RazonSocial;
import domain.organizaciones.enums.TipoDocumento;
import domain.organizaciones.enums.TipoOrganizacion;
import domain.organizaciones.repositorios.RepositorioCuentas;
import domain.sugerenciasYnotificaciones.NotificacionJavaMail;
import domain.sugerenciasYnotificaciones.NotificacionTwilio;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.lang.annotation.Documented;
import java.util.*;
import java.util.stream.Collectors;

public class SessionController extends Controller {

  public void metodos(HandlebarsTemplateEngine engine) {
    Spark.get("/login", (req, res) -> this.mostrarLogin(req, res), engine);
    Spark.post("/login", (req, res) -> this.iniciarSesion(req,res), engine);
    Spark.get("/logout", (req, res) -> this.cerrarSesion(req,res), engine);
    Spark.get("/singin", (req, res) -> this.mostrarSingIn(), engine);
    Spark.post("/singin", (req, res) -> this.registrarSesion(req,res), engine);
    Spark.post("/singin/:tipo_cuenta", (req, res) -> this.registrarCuenta(req,res), engine);
  }

  public ModelAndView mostrarLogin(Request req, Response res) {
    Map<String, Object> model = new HashMap<>();
    model.put("origin", req.queryParams("origin"));
    prepararEncabezado(model, req, res);
    return new ModelAndView(model, "login.html.hbs");
  }

  public ModelAndView iniciarSesion(Request req, Response res) {
    String origin = req.queryParams("origin");
    try {
      Cuenta cuenta = RepositorioCuentas.instance().buscarPorUsuarioYContrasenia(req.queryParams("user"), req.queryParams("pass")).get(0);
      req.session().attribute("cuenta_id", cuenta.getId());
      res.redirect(origin);
    } catch (NoSuchElementException e) {
      res.redirect("/login"); //redirigir agregando un mensaje de error
      return null;
    }
    return null;
  }

  public ModelAndView mostrarSingIn() {
    Map<String, Object> model = new HashMap<>();
    List<TipoCuenta> tipos = Arrays.stream(TipoCuenta.values()).collect(Collectors.toList());

    model.put("tipos", tipos);

    return new ModelAndView(model, "singin.html.hbs");
  }

  public ModelAndView registrarSesion(Request req, Response res) {
    String valor = req.queryParams("tipo_cuenta");
    Map<String, Object> model = new HashMap<>();
    model.put("tipo_cuenta", TipoCuenta.valueOf(valor));
    if(TipoCuenta.valueOf(valor)==TipoCuenta.MIEMBRO) {
      List<TipoDocumento> tipos = Arrays.stream(TipoDocumento.values()).collect(Collectors.toList());
      model.put("tipos", tipos);
      return new ModelAndView(model, "singin-miembro.html.hbs");
    }
    else if(TipoCuenta.valueOf(valor)==TipoCuenta.ORGANIZACION) {
      List<RazonSocial> razones_sociales = Arrays.stream(RazonSocial.values()).collect(Collectors.toList());
      List<TipoOrganizacion> tipos_organizacion = Arrays.stream(TipoOrganizacion.values()).collect(Collectors.toList());
      List<ClasificacionOrganizacion> clasifiaciones_organizaciones = Arrays.stream(ClasificacionOrganizacion.values()).collect(Collectors.toList());
      model.put("razones_sociales", razones_sociales);
      model.put("tipos_organizacion", tipos_organizacion);
      model.put("clasifiaciones_organizaciones", clasifiaciones_organizaciones);
      return new ModelAndView(model, "singin-organizacion.html.hbs");
    }
    else if(TipoCuenta.valueOf(valor)==TipoCuenta.AGENTE_SECTORIAL) {
      List<TipoDeSector> tipos_sectores = Arrays.stream(TipoDeSector.values()).collect(Collectors.toList());
      model.put("tipos_sectores", tipos_sectores);
      return new ModelAndView(model, "singin-agente-sectorial.html.hbs");
    }
    else
      throw  new RuntimeException("Tipo cuenta "+valor+" no contemplado");
  }
  public ModelAndView registrarCuenta(Request req, Response res) {
    String user = req.queryParams("user");
    String pass = req.queryParams("pass");
    String valor = req.params("tipo_cuenta").toUpperCase();
    Cuenta nuevaCuenta;
    try {
      if(TipoCuenta.valueOf(valor)==TipoCuenta.MIEMBRO) {
        String nombre = req.queryParams("nombre");
        String documento = req.queryParams("documento");
        String tipo_documento = req.queryParams("tipo_documento");
        String email = req.queryParams("email");
        String telefono = req.queryParams("telefono");
        Documento docu = new Documento(TipoDocumento.valueOf(tipo_documento), documento);
        Miembro miembroCuenta = new Miembro(nombre, docu, telefono, email);
        miembroCuenta.agregarNotificacion(new NotificacionJavaMail());
        miembroCuenta.agregarNotificacion(new NotificacionTwilio());
        nuevaCuenta = new Cuenta(user, pass, TipoCuenta.valueOf(valor), miembroCuenta);
        miembroCuenta.notificar("Cuenta creada", "Confirmacion creacion de cuenta");
      }
      else if(TipoCuenta.valueOf(valor)==TipoCuenta.ORGANIZACION) {
        String nombre = req.queryParams("nombre");
        String razonSocial = req.queryParams("razon-social");
        String tipoOrganizacion = req.queryParams("tipo-organizacion");
        String clasifiacionOrganizacion = req.queryParams("clasifiacion-organizacion");
        String calle = req.queryParams("calle");
        int altura = Integer.parseInt(req.queryParams("altura"));
        Ubicacion ubicacion = new Ubicacion(calle, altura);
        Organizacion orgCuenta =
            new Organizacion(nombre, RazonSocial.valueOf(razonSocial),
                TipoOrganizacion.valueOf(tipoOrganizacion),
                ubicacion, ClasificacionOrganizacion.valueOf(clasifiacionOrganizacion));
        nuevaCuenta = new Cuenta(user, pass, TipoCuenta.valueOf(valor), orgCuenta);
      }
      else if(TipoCuenta.valueOf(valor)==TipoCuenta.AGENTE_SECTORIAL) {
        String nombre = req.queryParams("nombre");
        String tipoSector = req.queryParams("tipo-sector");
        SectorTerritorial sectorCuenta = new SectorTerritorial(nombre, TipoDeSector.valueOf(tipoSector));
        nuevaCuenta = new Cuenta(user, pass, TipoCuenta.valueOf(valor), sectorCuenta);
      }
      else
        throw  new RuntimeException("Tipo cuenta "+valor+" no contemplado");
      withTransaction(() -> {
        RepositorioCuentas.instance().agregarCuenta(nuevaCuenta);
      });
      req.session().attribute("cuenta_id", nuevaCuenta.getId());
      res.redirect("/home");
    } catch (NoSuchElementException e) {
      res.redirect("/login"); //redirigir agregando un mensaje de error
    }
    return null;
  }


  public ModelAndView cerrarSesion(Request request, Response response) {
    request.session().attribute("cuenta_id", null);
    response.redirect("/home");
    return null;
  }
}