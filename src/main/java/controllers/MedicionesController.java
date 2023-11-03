package controllers;

import domain.csv.DatoActividadParserCSV;
import domain.mediciones.*;
import domain.organizaciones.cuenta.Cuenta;
import domain.organizaciones.cuenta.TipoCuenta;
import domain.organizaciones.repositorios.RepositorioConsumos;
import domain.organizaciones.repositorios.RepositorioCuentas;
import org.apache.commons.io.FileUtils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicionesController extends Controller {

  public void metodos(HandlebarsTemplateEngine engine) {
    Spark.get("/mediciones/nueva", (req, res) -> this.mostrarNuevaMedicion(req,res), engine);
    Spark.post("/mediciones/nueva", (req, res) -> this.crearNuevaMedicion(req,res), engine);
    Spark.post("/mediciones/archivo", (req, res) -> this.archivoMediciones(req,res), engine);
    Spark.get("/mediciones", (req, res) -> this.mostrarMediciones(req,res), engine);
  }

  public ModelAndView mostrarNuevaMedicion(Request req, Response res) {
    Map<String, Object> model = new HashMap<>();

    if (!seLogueo(req)) {
      res.redirect("/login?origin=/mediciones/nueva");
      return null;
    }

    Cuenta cuenta = RepositorioCuentas.instance().obtenerCuenta(req.session().attribute("cuenta_id"));

    if (cuenta.getTipoCuenta() != TipoCuenta.ORGANIZACION) {
      res.redirect("/home");
    }

    model.put("tiposConsumo", TipoConsumo.values());
    model.put("periodicidades", TipoPeriodicidad.values());

    prepararEncabezado(model, req, res);
    return new ModelAndView(model, "nueva-medicion.html.hbs");
  }

  public ModelAndView crearNuevaMedicion(Request req, Response res) {
    double valor = Double.parseDouble(req.queryParams("valor"));
    String tipoConsumo = req.queryParams("tipo_medicion");
    String tipoPeriodicidad = req.queryParams("tipo_periodicidad");

    Consumo consumo = new Consumo(valor, TipoConsumo.valueOf(tipoConsumo),
        new Periodicidad(TipoPeriodicidad.obtnerPeriodicidadValidada(tipoPeriodicidad), tipoPeriodicidad),
        new FactorEmision(TipoConsumo.valueOf(tipoConsumo).getUnidad()));

    Medicion medicion = new Medicion();
    withTransaction(() -> {
      RepositorioConsumos.instance().agregar(consumo);
    });
    res.redirect("/mediciones");
    return null;
  }

  public ModelAndView mostrarMediciones(Request req, Response res) {
    Map<String, Object> model = new HashMap<>();

    if (!seLogueo(req)) {
      res.redirect("/login?origin=/mediciones/nueva");
      return null;
    }

    Cuenta cuenta = RepositorioCuentas.instance().obtenerCuenta(req.session().attribute("cuenta_id"));

    if (cuenta.getTipoCuenta() != TipoCuenta.ORGANIZACION) {
      res.redirect("/home");
    }

    model.put("consumos", RepositorioConsumos.instance().todos());

    prepararEncabezado(model, req, res);
    return new ModelAndView(model, "mediciones.html.hbs");
  }

  public ModelAndView archivoMediciones(Request req, Response res) {
    req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
    try (InputStream is = req.raw().getPart("uploaded_file").getInputStream()) {
      // Use the input stream to create a file
      String path = System.getProperty("user.dir")+"/src/main/resources/temp/file.csv";
      InputStream stream = req.raw().getPart("uploaded_file").getInputStream();
      File targetFile = new File(path);

      FileUtils.copyInputStreamToFile(stream, targetFile);

      DatoActividadParserCSV parserCSV = new DatoActividadParserCSV();
      List<Consumo> consumos = parserCSV.parsearCSV(path);
      withTransaction(() -> {
        for (Consumo consumo : consumos) {
          RepositorioConsumos.instance().agregar(consumo);
        }
      });
    }
    catch (Exception e) {
      return new ModelAndView(null, "medicion-no-creada-archivo.html.hbs");
    }
    return new ModelAndView(null, "medicion-creada-archivo.html.hbs");
  }
}