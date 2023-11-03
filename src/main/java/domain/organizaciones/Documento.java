package domain.organizaciones;

import domain.organizaciones.enums.TipoDocumento;
import domain.persistencia.EntidadPersistente;

import javax.persistence.*;

@Embeddable
public class Documento {
  @Enumerated(EnumType.ORDINAL)
  private TipoDocumento tipo;
  @Column
  private String documento;

  public Documento(TipoDocumento tipo, String documento) {
    this.tipo = tipo;
    this.documento = documento;
  }

  public Documento() {

  }
}
