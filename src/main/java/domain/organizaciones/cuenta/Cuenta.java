package domain.organizaciones.cuenta;

import domain.exceptions.ExcepcionContrasenia;
import domain.organizaciones.Miembro;
import domain.organizaciones.Organizacion;
import domain.organizaciones.SectorTerritorial;
import domain.organizaciones.cuenta.validacioncontrasena.*;
import domain.persistencia.EntidadPersistente;

import javax.persistence.*;

@Entity
@Table(name="Cuentas")
public class Cuenta extends EntidadPersistente {
  @Column
  private String nombreUsuario;
  @Column
  private String contrasena;
  @Transient
  private ValidadorContrasenas validador;

  //private Boolean esAdmin; creo que ahora no iría
  @Enumerated(EnumType.STRING)
  private TipoCuenta tipoCuenta;
  @OneToOne(cascade = CascadeType.ALL)
  private Miembro miembro;
  @OneToOne(cascade = CascadeType.ALL)
  private Organizacion orgnizacion;
  @OneToOne(cascade = CascadeType.ALL)
  private SectorTerritorial sectorTerritorial;

  public Cuenta(String nombreUsuario, String contrasena, TipoCuenta tipoCuenta, Miembro miembro) {
    //this.validador = this.validadorBase();
    //this.validarCreacion(nombreUsuario, contrasena, tipoCuenta);
    //this.validarSeguridad(contrasena, nombreUsuario);
    this.nombreUsuario = nombreUsuario;
    this.contrasena = contrasena;
    this.tipoCuenta = tipoCuenta;
    this.miembro = miembro;
  }

  public Cuenta(String nombreUsuario, String contrasena, TipoCuenta tipoCuenta, Organizacion org) {
    //this.validador = this.validadorBase();
    //this.validarCreacion(nombreUsuario, contrasena, tipoCuenta);
    //this.validarSeguridad(contrasena, nombreUsuario);
    this.nombreUsuario = nombreUsuario;
    this.contrasena = contrasena;
    this.tipoCuenta = tipoCuenta;
    this.orgnizacion = org;
  }

  public Cuenta(String nombreUsuario, String contrasena, TipoCuenta tipoCuenta, SectorTerritorial sectorTerritorial) {
    //this.validador = this.validadorBase();
    //this.validarCreacion(nombreUsuario, contrasena, tipoCuenta);
    //this.validarSeguridad(contrasena, nombreUsuario);
    this.nombreUsuario = nombreUsuario;
    this.contrasena = contrasena;
    this.tipoCuenta = tipoCuenta;
    this.sectorTerritorial = sectorTerritorial;
  }

  public Cuenta(String nombreUsuario, String contrasena, TipoCuenta tipoCuenta) {
    //this.validador = this.validadorBase();
    //this.validarCreacion(nombreUsuario, contrasena, tipoCuenta);
    //this.validarSeguridad(contrasena, nombreUsuario);
    this.nombreUsuario = nombreUsuario;
    this.contrasena = contrasena;
    this.tipoCuenta = tipoCuenta;
  }

  public Cuenta() {
  }

  private ValidadorContrasenas validadorBase() {
    ValidadorContrasenas validadorContrasenas = new ValidadorContrasenas();
    validadorContrasenas.agregarValidador(new ValidadorCaracterEspecial());
    validadorContrasenas.agregarValidador(new ValidadorLongitud());
    validadorContrasenas.agregarValidador(new ValidadorMayusculas());
    validadorContrasenas.agregarValidador(new ValidadorMinusculas());
    validadorContrasenas.agregarValidador(new ValidadorNumero());
    validadorContrasenas.agregarValidador(new ValidadorListaContrasenas());
    return validadorContrasenas;
  }

  private void validarCreacion(String nombreUsuario, String contrasena, TipoCuenta tipoCuenta) {
    validarNombreUsuario(nombreUsuario);
    validarContrasenia(contrasena);
    validarTipoUsuario(tipoCuenta);
  }

  public void validarSeguridad(String contrasena, String nombreUsuario) {
    validador.validar(contrasena, nombreUsuario);

  }

  public void validarNombreUsuario(String nombreUsuario) {
    if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
      throw new ExcepcionContrasenia("Debe ingresar un usuario");
    }
  }

  public void validarTipoUsuario(TipoCuenta tipoCuenta) {
    if (tipoCuenta == null) {
      throw new ExcepcionContrasenia("Debe ingresar un tipo de cuenta");
    }
  }

  public void validarContrasenia(String contrasenia) {
    if (contrasenia == null || contrasenia.trim().isEmpty()) {
      throw new ExcepcionContrasenia("Debe ingresar una contrasenia");
    }
  }

  public TipoCuenta getTipoCuenta() {
    return tipoCuenta;
  }

  public Miembro getMiembro() {
    return miembro;
  }

  public Organizacion getOrganizacion() {
    return orgnizacion;
  }

  public SectorTerritorial getSectorTerritorial() {
    return sectorTerritorial;
  }

  public String getNombreUsuario() {
    return nombreUsuario;
  }

  public String getContrasena() {
    return contrasena;
  }

  public boolean esTipoCuenta(TipoCuenta tipo) {
    return this.tipoCuenta.equals(tipo);
  }
}
