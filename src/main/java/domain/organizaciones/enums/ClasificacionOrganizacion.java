package domain.organizaciones.enums;

public enum ClasificacionOrganizacion {
  MINISTERIO,
  ESCUELA,
  UNIVERSIDAD,
  EMPRESA_DEL_SECTOR_PRIMARIO,
  EMPRESA_DEL_SECTOR_SECUNDARIO;

  public String getEnumTipo() {
    return this.toString();
  }
}
