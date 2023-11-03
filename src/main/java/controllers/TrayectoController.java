package controllers;

import domain.organizaciones.*;
import domain.organizaciones.cuenta.Cuenta;
import domain.organizaciones.repositorios.*;
import domain.services.distanciaAPI.GeoddsService;
import domain.transporte.MedioDeTransporte;
import domain.transporte.TransportePublico;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrayectoController extends Controller {
  public void metodos(HandlebarsTemplateEngine engine) {
    Spark.get("/trayectos/seleccion", (req, res) -> this.mostrarSeleccionTrayecto(req, res), engine);
    Spark.get("/trayectos/nuevo", (req, res) -> this.mostrarCreacionNuevoTrayecto(req, res), engine);
    Spark.post("/trayectos/nuevo", (req, res) -> this.crearNuevoTrayecto(req, res), engine);
    Spark.post("/trayectos/eliminacion/:id_trayecto", (req, res) -> this.eliminarTrayecto(req, res));
    Spark.get("/trayectos/edicion/:id_trayecto", (req, res) -> this.mostrarEdicionTrayecto(req, res), engine);
    Spark.get("/trayectos/:id_trayecto/tramos/nuevo", (req, res) -> this.mostrarNuevoTramo(req, res), engine);
    Spark.post("/trayectos/:id_trayecto/tramos/nuevo", (req, res) -> this.crearNuevoTramo(req, res), engine);
    Spark.get("/trayectos/:id_trayecto/tramos-transporte-publico/nuevo", (req, res) -> this.mostrarNuevoTramoTransportePublico(req, res), engine);
    Spark.post("/trayectos/:id_trayecto/tramos-transporte-publico/nuevo", (req, res) -> this.crearNuevoTramoTransportePublico(req, res), engine);
    Spark.post("/trayectos/:id_trayecto/tramos/:id_tramo/eliminacion", (req, res) -> this.eliminarTramo(req, res));
    // Spark.get("/trayectos/:id_trayecto/tramos/:id_tramo", (req, res) -> trayectoController.mostrarNuevoTrayecto(), engine);
    // Spark.get("/trayectos/paradas/nueva", (req, res) -> trayectoController.mostrarNuevaParada(), engine);
  }

  private ModelAndView eliminarTrayecto(Request request, Response response) {
    if (!seLogueo(request)) {
      response.redirect("/login?origin=/trayectos/");
    }
    Miembro miembro = RepositorioCuentas.instance().buscar(request.session().attribute("cuenta_id")).getMiembro();
    int id_trayecto = Integer.parseInt(request.params("id_trayecto"));
    withTransaction( () -> {
      Trayecto trayecto = RepositorioTrayectos.instance().buscar(id_trayecto);
      if(trayecto.esCompartido()) {
        miembro.quitarTrayecto(trayecto);
      }
      else {
        miembro.quitarTrayecto(trayecto);
        trayecto.eliminar();
        RepositorioTrayectos.instance().eliminar(id_trayecto);
      }
    });
    Map<String, Object> model = new HashMap();
    prepararEncabezado(model, request, response);
    model.put("tipoMensaje", "Notificación");
    model.put("mensaje", "Trayecto eliminado");
    return new ModelAndView(model, "mensaje.html.hbs");
  }

  public ModelAndView mostrarSeleccionTrayecto(Request request, Response response) {

    if (!seLogueo(request)) {
      response.redirect("/login?origin=/trayectos/seleccion");
      return null;
    }

    Miembro miembro = RepositorioCuentas.instance().buscar(request.session().attribute("cuenta_id")).getMiembro();
    Map<String, Object> model = new HashMap();
    String id = request.queryParams("seleccion_trayecto");

    if (id != null) {
      response.redirect("/trayectos/" + id);
    }

    model.put("trayectos", miembro.getTrayectos());
    prepararEncabezado(model, request, response);
    return new ModelAndView(model, "miembro-seleccion-trayecto.html.hbs");
  }

  public ModelAndView mostrarCreacionNuevoTrayecto(Request request, Response response) {

    if (!seLogueo(request)) {
      response.redirect("/login?origin=/trayectos/nuevo");
      return null;
    }

    Map<String, Object> model = new HashMap<>();

    prepararEncabezado(model, request, response);
    return new ModelAndView(model, "miembro-seleccion-nuevo-trayecto.html.hbs");
  }

  public ModelAndView crearNuevoTrayecto(Request request, Response response) {
    Cuenta cuenta = RepositorioCuentas.instance().obtenerCuenta(request.session().attribute("cuenta_id"));
    Miembro miembro = cuenta.getMiembro();
    String nombre_trayecto = request.queryParams("nombre_trayecto");

    Trayecto nuevo = new Trayecto(nombre_trayecto);
    miembro.agregarTrayecto(nuevo);

    withTransaction(() -> {
      RepositorioTrayectos.instance().agregar(nuevo);
    });

    response.redirect("/trayectos/" + nuevo.getId());
    return null;
  }

  public ModelAndView mostrarEdicionTrayecto(Request req, Response res) {
    //Miembro miembro = req.session().attribute("miembro");
    int id_trayecto = Integer.parseInt(req.params("id_trayecto"));

    if (!seLogueo(req)) {
      res.redirect("/login?origin=/trayectos/edicion/"+id_trayecto);
      return null;
    }

    Trayecto trayecto = RepositorioTrayectos.instance().buscar(id_trayecto);

    Map<String, Object> model = new HashMap<>();
    model.put("nombre_trayecto", trayecto.getNombre());
    model.put("tramos", trayecto.getTramos());
    model.put("id_trayecto", id_trayecto);

    prepararEncabezado(model, req, res);
    return new ModelAndView(model, "editar-trayecto.html.hbs");
  }

  public ModelAndView mostrarNuevaParada(Request req, Response res) {
    Map<String, Object> model = new HashMap<>();

    if (!seLogueo(req)) {
      res.redirect("/login?origin=/trayectos/paradas/nueva");
      return null;
    }

    prepararEncabezado(model, req, res);
    return new ModelAndView(model, "nueva-parada.html.hbs");
  }

  public ModelAndView mostrarNuevoTramo(Request req, Response res) {
    int id_trayecto = Integer.parseInt(req.params("id_trayecto"));

    if (!seLogueo(req)) {
      res.redirect("/login?origin=/trayectos/"+id_trayecto+"/tramos/nuevo");
      return null;
    }

    Trayecto trayecto = RepositorioTrayectos.instance().buscar(id_trayecto);

    Map<String, Object> model = new HashMap<>();
    model.put("id_trayecto", id_trayecto);
    model.put("medios_transporte", RepositorioMedioDeTransporte.instance().todos());

    prepararEncabezado(model, req, res);
    return new ModelAndView(model, "miembro-tramo-nuevo.html.hbs");
  }

  public ModelAndView mostrarNuevoTramoTransportePublico(Request req, Response res) {
    int id_trayecto = Integer.parseInt(req.params("id_trayecto"));

    if (!seLogueo(req)) {
      res.redirect("/login?origin=/trayectos/"+id_trayecto+"/tramos-transporte-publico/nuevo");
      return null;
    }

    Trayecto trayecto = RepositorioTrayectos.instance().buscar(id_trayecto);
    Map<String, Object> model = new HashMap<>();
    String idTransportePublico = req.queryParams("transporte_publico");
    String idSentido = req.queryParams("sentido");
    TransportePublico transporte;
    Parada sentidoIda = null;
    Parada sentidoVuelta = null;

    if (idTransportePublico == null) {
      model.put("transportes_publicos", RepositorioMedioDeTransporte.instance().obtenerTransporteSegun("publico"));
    } else {
      transporte = RepositorioMedioDeTransporte.instance().obtenerTransportePublico(Integer.parseInt(idTransportePublico));
      sentidoIda = transporte.getTramoIda().get(transporte.getTramoIda().size() - 1);
      sentidoVuelta = transporte.getTramoVuelta().get(transporte.getTramoVuelta().size() - 1);
      List<Parada> sentidos = new ArrayList<>();
      sentidos.add(sentidoIda);
      sentidos.add(sentidoVuelta);

      model.put("sentidos", sentidos);
      model.put("valorOpcionTransporte", transporte.getNombre());
      model.put("idValorOpcionTransporte", transporte.getId());
    }
    if(idSentido != null) {
      transporte = RepositorioMedioDeTransporte.instance().obtenerTransportePublico(Integer.parseInt(req.queryParams("transporte_publico")));
      Parada parada = RepositorioParadas.instance().buscar(Integer.parseInt(idSentido));
      List<Parada> paradasIda = new ArrayList<>();
      List<Parada> paradasVuelta = new ArrayList<>();

      if (idSentido.equals(String.valueOf(sentidoIda.getId()))) {
        paradasIda.addAll(transporte.getTramoIda());
        paradasVuelta.addAll(transporte.getTramoIda());

      } else if (idSentido.equals(String.valueOf(sentidoVuelta.getId()))) {
        paradasIda.addAll(transporte.getTramoVuelta());
        paradasVuelta.addAll(transporte.getTramoVuelta());

      }
      model.put("origenes", paradasIda);
      model.put("destinos", paradasVuelta);
      model.put("idValorOpcionSentido", parada.getId());
      model.put("valorOpcionSentido", parada.getNombre());
    }
    model.put("id_trayecto", id_trayecto);

    prepararEncabezado(model, req, res);
    return new ModelAndView(model, "miembro-tramo-tpp-nuevo.html.hbs");
  }

  public ModelAndView crearNuevoTramo(Request req, Response res) {
    int id_trayecto = Integer.parseInt(req.params("id_trayecto"));
    int id_medio_transporte = Integer.parseInt(req.queryParams("medio_de_transporte"));
    String calle_origen = req.queryParams("calle_origen");
    int altura_origen = Integer.parseInt(req.queryParams("altura_origen"));
    String calle_destino = req.queryParams("calle_destino");
    int altura_destino = Integer.parseInt(req.queryParams("altura_destino"));
    Trayecto trayecto = RepositorioTrayectos.instance().buscar(id_trayecto);

    MedioDeTransporte medioDeTransporte = RepositorioMedioDeTransporte.instance().buscar(id_medio_transporte);

    Tramo nuevoTramo =
        new Tramo(medioDeTransporte, new Ubicacion(calle_destino, altura_destino),
            new Ubicacion(calle_origen, altura_origen), new GeoddsService());
    trayecto.anadirTramo(nuevoTramo);

    withTransaction(() -> {
      RepositorioTramos.instance().agregar(nuevoTramo);
    });

    res.redirect("/trayectos/" + id_trayecto);
    return null;
  }

  public ModelAndView crearNuevoTramoTransportePublico(Request req, Response res) {
    int id_trayecto = Integer.parseInt(req.params("id_trayecto"));
    int id_medio_transporte = Integer.parseInt(req.queryParams("transporte_publico"));
    int parada_origen = Integer.parseInt(req.queryParams("origen"));
    int parada_destino = Integer.parseInt(req.queryParams("destino"));

    Trayecto trayecto = RepositorioTrayectos.instance().buscar(id_trayecto);
    TransportePublico medioDeTransporte;

    Parada paradaOrigen = RepositorioParadas.instance().buscar(parada_origen);
    Parada paradaDestino = RepositorioParadas.instance().buscar(parada_destino);
    medioDeTransporte = RepositorioMedioDeTransporte.instance().obtenerTransportePublico(id_medio_transporte);
    Tramo nuevoTramo = new Tramo(medioDeTransporte, new Ubicacion(paradaOrigen.getNombre(), paradaOrigen.getLocal(), paradaOrigen.getAltura()),
        new Ubicacion(paradaDestino.getNombre(), paradaDestino.getLocal(), paradaDestino.getAltura()), new GeoddsService());
    trayecto.anadirTramo(nuevoTramo);

    withTransaction(() -> {
      RepositorioTramos.instance().agregar(nuevoTramo);
    });

    res.redirect("/trayectos/" + id_trayecto);
    return null;
  }

  public ModelAndView eliminarTramo(Request req, Response res) {
    //TODO IMPLEMENTAR
    int id_trayecto = Integer.parseInt(req.params("id_trayecto"));
    int id_tramo = Integer.parseInt(req.params("id_tramo"));

    Trayecto trayecto = RepositorioTrayectos.instance().buscar(id_trayecto);
    Tramo tramo = RepositorioTramos.instance().buscar(id_tramo);

    withTransaction(() -> {
      trayecto.eliminarTramo(tramo);
    });
    res.redirect("/trayectos/edicion/" + id_trayecto);
    return null;
  }
}