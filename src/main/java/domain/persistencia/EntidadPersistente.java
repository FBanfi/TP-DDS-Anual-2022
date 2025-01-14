package domain.persistencia;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class EntidadPersistente {

  @Id
  @GeneratedValue
  private int id;

  public int getId() { return id; }

  public boolean sameId(int id) { return this.id==id; }

  public void setId(int id) {
    this.id = id;
  }

}