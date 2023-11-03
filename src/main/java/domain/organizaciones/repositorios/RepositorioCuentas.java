package domain.organizaciones.repositorios;

import domain.organizaciones.cuenta.Cuenta;
import domain.organizaciones.cuenta.TipoCuenta;

import java.util.ArrayList;
import java.util.List;

public class RepositorioCuentas extends Repositorio<Cuenta> {
  private static final RepositorioCuentas INSTANCE = new RepositorioCuentas();
  private List<Cuenta> cuentas = new ArrayList<>();

  public static RepositorioCuentas instance() {
    return INSTANCE;
  }

  private RepositorioCuentas() {
    super("Cuenta");
  }

  public void agregarCuenta(Cuenta cuenta) {
    entityManager().persist(cuenta);
  }

  public List<Cuenta> obtenerCuentaSegun(TipoCuenta tipo) {
    String param = tipo.toString();
    String query = String.format("from Cuenta where tipoCuenta='%s'", param);
    return entityManager().createQuery(query).getResultList();
  }
  public List<Cuenta> obtenerCuentaPorUsuario(String user) {

    String query = String.format("from Cuenta where user='%s'", user);
    return entityManager().createQuery(query).getResultList();
  }

  public List<Cuenta> buscarPorUsuarioYContrasenia(String user, String pass) {

    String query = String.format("from Cuenta where nombreUsuario='%s' and contrasena='%s'", user,pass);
    return entityManager().createQuery(query).getResultList();
  }

  public Cuenta obtenerCuenta(int id) {

    String query = String.format("from Cuenta where id='%s'", id);
    return (Cuenta) entityManager().createQuery(query).getResultList().get(0);
  }


}
