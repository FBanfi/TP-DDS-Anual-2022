package domain.organizaciones.enums;

public enum TipoDocumento {
  PASAPORTE,
  DNI,
  LIBRETA_CIVICA;

  public String getEnumTipo() {
    return this.toString();
  }
}
