package domain.services.distanciaAPI;

public class Distancia {
  public String valor;
  public String unidad;

  public Distancia(String valor, String unidad) {
    this.unidad = unidad;
    this.valor = valor;
  }

  public double getValor() {
    return Double.parseDouble(valor);
  }

  public String getUnidad() {
    return unidad;
  }
}
