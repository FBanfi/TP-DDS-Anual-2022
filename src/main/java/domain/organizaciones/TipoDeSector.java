package domain.organizaciones;

public enum TipoDeSector {
  MUNICIPIO,
  PROVINCIA;

  public String getEnumTipo() {
    return this.toString();
  }
}
