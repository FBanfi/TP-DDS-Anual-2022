package domain.transporte;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("particular")
public class TransporteParticular extends MedioDeTransporte {

  public TransporteParticular(Vehiculo vehiculo) {
    super(vehiculo);
  }

  public TransporteParticular() {
    
  }

  @Override
  public double getEmision() {
    return 0;
  }

  @Override
  public boolean esIgualAParticualOContratado() {
    return true;
  }
}
