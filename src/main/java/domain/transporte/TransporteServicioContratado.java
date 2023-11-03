package domain.transporte;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("contratado")
public class TransporteServicioContratado extends MedioDeTransporte {

  public TransporteServicioContratado(Vehiculo vehiculo) {
    super(vehiculo);
  }

  public TransporteServicioContratado() {

  }

  @Override
  public double getEmision() {
    return 0;
  }

  @Override
  public boolean esIgualAParticualOContratado() {   // no lo usamos
    return true;
  }
}