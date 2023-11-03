package domain.services.distanciaAPI;

import domain.organizaciones.Ubicacion;

import java.util.Timer;
import java.util.TimerTask;

public class EjemploDeUso {

  public static void main(String[] args) {
/*
    Ubicacion ubi1 = new Ubicacion("maipu", 1, 100);
    Ubicacion ubi2 = new Ubicacion("O'Higgins", 457, 200);
    Tramo tramo = new Tramo(new TransporteNoEmisor(new Vehiculo(TipoVehiculo.AUTO, TipoCombustible.NO_COMBUSTIBLE)), ubi1, ubi2);
    Trayecto trayecto = new Trayecto();
    trayecto.anadirTramo(tramo);

    System.out.println("La distancia total del trayecto es: " + trayecto.getDistanciaTotal());
    System.out.println("La distancia del tramo es igual a la del trayecto: " + tramo.getDistancia().getValor());
*/

    DistanciaService servicioDistancia = new GeoddsService();
    Ubicacion origen = new Ubicacion("maipu", 300, 100);
    Ubicacion destino = new Ubicacion("O'Higgins", 457, 200);
    Distancia distancia = servicioDistancia.obtenerDistancia(origen, destino);

    System.out.println("La distancia es de: " + distancia.getValor());
    System.out.println("La unidad de la distancia es: " + distancia.getUnidad());

    TimerTask task = new TimerTask() {
      public void run() {
        //System.out.println("hola");
        //new ServicioJavaMail().enviarEmail("asd@gmail.com", "Prueba routes", "Hola");
      }
    };

    Timer timer = new Timer("Timer");

    //long interval = 604800000L; //Para una semana
    long interval = 600000L;
    //long interval = 5000L; // Para 5 segundos
    timer.schedule(task, 0L, interval);

  }
}
