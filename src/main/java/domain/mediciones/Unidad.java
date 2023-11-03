package domain.mediciones;

public enum Unidad {
  M3,
  LT,
  KG,
  LTS,
  KWH,
  KM,
  SIN_UNIDAD;

  public boolean esIgualA(Unidad unidad) {
    return unidad.equals(this);
  }
}
