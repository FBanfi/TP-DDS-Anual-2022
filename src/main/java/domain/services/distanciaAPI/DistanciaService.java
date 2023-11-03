package domain.services.distanciaAPI;

import domain.organizaciones.Ubicacion;
import domain.persistencia.EntidadPersistente;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
public abstract class DistanciaService  extends EntidadPersistente {
  public abstract Distancia obtenerDistancia(Ubicacion origen, Ubicacion destino);
}