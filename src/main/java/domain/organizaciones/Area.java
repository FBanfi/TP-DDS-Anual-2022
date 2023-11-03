package domain.organizaciones;

import domain.persistencia.EntidadPersistente;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "areas")
public class Area extends EntidadPersistente {
  @Column
  String nombre;

  @OneToMany(cascade = {CascadeType.ALL})
  @JoinColumn(name = "area_id")
  private List<Miembro> miembros = new ArrayList<Miembro>();

  public Area(String nombre) {
    this.nombre = nombre;
  }

  public Area() {

  }

  public void agregarMiembro(Miembro miembro) {
    this.miembros.add(miembro);
  }

  public List<Trayecto> getTrayectosMiembros(){
    return miembros.stream().map(miembro -> miembro.getTrayectos()).flatMap(Collection::stream).collect(Collectors.toList());
  }

  public List<Miembro> getMiembros() {
    return this.miembros;
  }

  public int cantMiembros(){
    return miembros.size();
  }

  public double promedioDeHC() {
    return miembros.stream().mapToDouble(Miembro::obtenerHC).sum() / cantMiembros();
  }

  public String getNombre() {
    return nombre;
  }
}