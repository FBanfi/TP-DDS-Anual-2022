package domain.organizaciones;

import domain.persistencia.EntidadPersistente;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static java.util.Objects.requireNonNull;

//Armo una solicitud para que la misma se almacene
// en la organizacion y pueda ser respondida en cualquier momento
//No hay logica de aceptacion/rechazo, es el
// usuario quien decide por eso creo que hay que almacenarla

@Entity
@Table(name = "solicitudes_vinculo")
public class SolicitudDeVinculo extends EntidadPersistente {
  @ManyToOne
  private Miembro miembroSolicitante;
  @ManyToOne //Checkear
  // lucas Bat: Para mi esta bien, ya que un miembro va a solicitar vincularse a la org mediante UN area
  private Area area;

  public SolicitudDeVinculo(Area area, Miembro miembro) {
    this.area = requireNonNull(area,"Falta el area!");
    this.miembroSolicitante = miembro;
  }

  public SolicitudDeVinculo() {

  }

  public String getNombreMiembro(){
    return miembroSolicitante.getNombreCompleto();
  }

  public String getArea(){
    return area.getNombre();
  }

  public void aceptar(Organizacion org) {
    PuestoDeTrabajo puestoDeTrabajo = new PuestoDeTrabajo(org, this.area);
    miembroSolicitante.agregarOrganizacion(puestoDeTrabajo);
    area.agregarMiembro(miembroSolicitante);
  }
}
