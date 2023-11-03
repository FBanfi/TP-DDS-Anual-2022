package controllers;

import domain.organizaciones.cuenta.Cuenta;
import domain.organizaciones.cuenta.TipoCuenta;
import domain.organizaciones.repositorios.RepositorioCuentas;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class HomeController extends Controller {

  public ModelAndView mostrarHome(Request req, Response res) {

    Map model = new HashMap<>();

    if (!seLogueo(req)) {
      res.redirect("/login?origin=/home");
      return null;
    }

    Cuenta cuenta = RepositorioCuentas.instance().obtenerCuenta(req.session().attribute("cuenta_id"));

    if (cuenta.getTipoCuenta() == TipoCuenta.AGENTE_SECTORIAL){
      prepararEncabezado(model, req, res);
      return new ModelAndView(model, "home-agente-sectorial.html.hbs");
    }
    if (cuenta.getTipoCuenta() == TipoCuenta.ORGANIZACION){
      prepararEncabezado(model, req, res);
      return new ModelAndView(model, "home-organizacion.html.hbs");
    }
    if (cuenta.getTipoCuenta() == TipoCuenta.MIEMBRO){
      prepararEncabezado(model, req, res);
      return new ModelAndView(model, "home-miembro.html.hbs");
    }

    prepararEncabezado(model, req, res);
    return null;
  }

  @Override
  public void metodos(HandlebarsTemplateEngine engine) {

  }
}