package domain.sugerenciasYnotificaciones;

import domain.persistencia.EntidadPersistente;

import javax.persistence.*;

@Entity
@Table(name = "notificaciones")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
public abstract class Notificacion extends EntidadPersistente {

    public abstract void notificar(String mensaje, String numero, String mailDestino, String asunto);
}
