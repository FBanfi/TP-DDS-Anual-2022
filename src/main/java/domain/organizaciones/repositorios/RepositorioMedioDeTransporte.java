package domain.organizaciones.repositorios;

import domain.transporte.MedioDeTransporte;
import domain.transporte.TransportePublico;

import java.util.List;

public class RepositorioMedioDeTransporte extends Repositorio<MedioDeTransporte> {
    private static final RepositorioMedioDeTransporte INSTANCE = new RepositorioMedioDeTransporte();

    public static RepositorioMedioDeTransporte instance() {
        return INSTANCE;
    }

    private RepositorioMedioDeTransporte() {
        super("MedioDeTransporte");
    }

    public TransportePublico obtenerTransportePublico(int id) {

        String query = String.format("from MedioDeTransporte where tipo='publico' and id='%s'", id);
        return (TransportePublico) entityManager().createQuery(query).getResultList().get(0);
    }

    public List<MedioDeTransporte> obtenerTransporteSegun(String tipo) {

        String query = String.format("from MedioDeTransporte where tipo='%s'", tipo);
        return (List<MedioDeTransporte>) entityManager().createQuery(query).getResultList();
    }

}