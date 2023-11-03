package domain.organizaciones.enums;

public enum RazonSocial {
  SOCIEDAD_RESPONSABILIDAD_LIMITADA,
  SOCIEDAD_ANONIMA,
  SOCIEDAD_ACCIONES_SIMPLIFICADAS;

  public String getEnumTipo() {
    return this.toString();
  }
}
