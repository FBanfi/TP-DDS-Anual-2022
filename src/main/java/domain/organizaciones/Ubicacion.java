package domain.organizaciones;

import javax.persistence.Embeddable;

@Embeddable
public class Ubicacion {
  private String calle;

  private int local;

  private int altura;

  public Ubicacion(String calle, int local, int altura) {
    this.calle = calle;
    this.local = local;
    this.altura = altura;
  }

  public Ubicacion(String calle, int altura) {
    this.calle = calle;
    this.local = 1;
    this.altura = altura;
  }
  public Ubicacion() {

  }

  public String getDireccion(){
    return calle + String.valueOf(altura);
  }

  public String getCalle() {
    return calle;
  }

  public int getLocal() {
    return local;
  }

  public int getAltura() {
    return altura;
  }

  public void setCalle(String calle) {
    this.calle = calle;
  }

  public void setLocal(int local) {
    this.local = local;
  }

  public void setAltura(int altura) {
    this.altura = altura;
  }
}
