package domain.organizaciones.cuenta;

public enum TipoCuenta {
  AGENTE_SECTORIAL("Agente Sectorial"),
  ORGANIZACION("Organizacion"),
  MIEMBRO("Miembro");

  private String valor;

  TipoCuenta(String valor){
    this.valor = valor;
  }

  public String getValor(){return valor;}

  public String getEnumTipo(){return this.toString();}
}
