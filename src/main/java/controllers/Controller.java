package controllers;

import domain.organizaciones.cuenta.Cuenta;
import domain.organizaciones.repositorios.RepositorioCuentas;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.Map;

public class Controller implements WithGlobalEntityManager, TransactionalOps {

  public void metodos(HandlebarsTemplateEngine engine){};

  public Map<String, Object> prepararEncabezado(Map<String, Object> model, Request request, Response response){
    model.put("sesionIniciada", request.session().attribute("cuenta_id") != null);
    model.put("sesionNoIniciada", request.session().attribute("cuenta_id") == null);
    if(seLogueo(request)){
      Cuenta cuenta = RepositorioCuentas.instance().obtenerCuenta(request.session().attribute("cuenta_id"));
      model.put("nombreUsuario", cuenta.getNombreUsuario());
      model.put("layoutOrganizacion", getCuenta(request).getOrganizacion());
      model.put("layoutMiembro", getCuenta(request).getMiembro());
      model.put("layoutAgenteSectorial", getCuenta(request).getSectorTerritorial());
    }
    else {
      model.put("nombreUsuario", null);
      model.put("layoutOrganizacion", null);
      model.put("layoutMiembro", null);
      model.put("layoutAgenteSectorial", null);
    }
    return model;
  }

  public Boolean seLogueo(Request request){
    return request.session().attribute("cuenta_id") != null;
  }

  public Cuenta getCuenta(Request request){
    return RepositorioCuentas.instance().buscar(request.session().attribute("cuenta_id"));
  }
}
