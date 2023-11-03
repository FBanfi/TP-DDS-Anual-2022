package domain.transporte;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("no_emisor")
public class TransporteNoEmisor extends MedioDeTransporte {

  public TransporteNoEmisor(Vehiculo vehiculo) {
    super(vehiculo);
  }

  public TransporteNoEmisor() {

  }

  @Override
  public double getEmision() {
    return 0;
  }
}
