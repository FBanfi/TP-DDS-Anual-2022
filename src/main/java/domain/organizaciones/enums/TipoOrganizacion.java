package domain.organizaciones.enums;

public enum TipoOrganizacion {
  GUBERNAMENTAL,
  ONG,
  EMPRESA,
  INSTITUCION;

  public String getEnumTipo() {
    return this.toString();
  }
}