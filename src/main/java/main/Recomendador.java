package main;

import domain.organizaciones.repositorios.RepositorioOrganizaciones;
//corre en main o con cron
public class Recomendador {
  public static void main(String[] args) {
    String paginaGuiaRecomendaciones = args[0];
    RepositorioOrganizaciones.instance().todos().forEach(o -> o.notificarSuscriptores(paginaGuiaRecomendaciones));
  }
}