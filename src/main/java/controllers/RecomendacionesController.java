package controllers;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class RecomendacionesController extends Controller {



  public ModelAndView mostrarRecomendaciones(Request req, Response res) {
    Map<String, Object> model = new HashMap<>();

    prepararEncabezado(model, req, res);

    return new ModelAndView(model, "recomendaciones.html.hbs");
  }

  @Override
  public void metodos(HandlebarsTemplateEngine engine) {

  }
}
