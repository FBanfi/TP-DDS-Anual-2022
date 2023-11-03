package domain.organizaciones;


import domain.persistencia.EntidadPersistente;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "puestos_de_trabajo")
public class PuestoDeTrabajo extends EntidadPersistente {

  @ManyToOne(cascade = CascadeType.ALL)
  private Area area;

  @ManyToOne(cascade = CascadeType.ALL) //un puesto de trabajo es de UNA org, y una org puede formar parte del puesto de trabajo de MUCHOS miembros (idem para las areas)
  private Organizacion organizacion;

  //que onda esto che, no tiene sentido?
  // lucas Bat: Para mi si, ya que puede ocurrir que varios puestos de trabajo tengan el mismo proyecto, es mas, nos sirve para luego
  // poder juntar aquellos que lo hagan en transporte publico para poder calcular la huella
  @ManyToOne(cascade = CascadeType.ALL)
  private Trayecto trayecto;

  public PuestoDeTrabajo(Organizacion organizacion, Area area) {
    this.area = area;
    this.organizacion = organizacion;
  }

  public PuestoDeTrabajo() {

  }

  public Area getArea() {
    return area;
  }

  public void setOrganizacion(Organizacion org) {
    organizacion = org;
  }

  public Organizacion getOrganizacion() {
    return organizacion;
  }
}