package main;

import com.twilio.rest.api.v2010.account.incomingphonenumber.Local;
import controllers.*;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import spark.ModelAndView;
import spark.Spark;
import spark.debug.DebugScreen;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Routes {

  public static void main(String[] args) {
    System.out.println("Corriendo bootstrap...");
    //new Bootstrap().run();
    //DebugScreen.enableDebugScreen();

    System.out.println("Iniciando servidor...");
    //Spark.port(getHerokuAssignedPort());
    Spark.port(8080);
    Spark.staticFileLocation("/public");
    HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();

    HomeController homeController = new HomeController();
    SessionController sessionController = new SessionController();
    CalculadoraController calculadoraController = new CalculadoraController();
    MedicionesController medicionesController = new MedicionesController();
    TrayectoController trayectoController = new TrayectoController();
    RecomendacionesController recomendacionesController = new RecomendacionesController();
    VinculacionesController vinculacionesController = new VinculacionesController();
    ReportesController reportesController = new ReportesController();

    Spark.get("/", (req, res) -> homeController.mostrarHome(req, res), engine);
    Spark.get("/home", (req, res) -> homeController.mostrarHome(req, res), engine);

    sessionController.metodos(engine);
    calculadoraController.metodos(engine);
    medicionesController.metodos(engine);
    trayectoController.metodos(engine);
    Spark.get("/recomendaciones", (req, res) -> recomendacionesController.mostrarRecomendaciones(req, res), engine);
    vinculacionesController.metodos(engine);
    reportesController.metodos(engine);
    Spark.get("/error", (request, response) -> {
      Map<String, Object> model = new HashMap();
      (new Controller()).prepararEncabezado(model, request, response);
      model.put("tipoMensaje", "Error");
      model.put("mensaje", request.queryParams("mensaje"));
      return new ModelAndView(model, "mensaje.html.hbs");
    }, engine);
    System.out.println("Servidor iniciado!");

    Spark.after((request,response)->{
      PerThreadEntityManagers.getEntityManager();
      PerThreadEntityManagers.closeEntityManager();
    });
    Spark.exception(Exception.class, (exception, request, response) -> {
      response.redirect("/error?mensaje="+exception.getMessage());
    });
  }

  static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }

    return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
  }

}