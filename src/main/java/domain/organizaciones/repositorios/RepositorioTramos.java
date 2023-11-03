package domain.organizaciones.repositorios;

import domain.organizaciones.Tramo;

public class RepositorioTramos extends Repositorio<Tramo> {
    private static final RepositorioTramos INSTANCE = new RepositorioTramos();

    public static RepositorioTramos instance(){
        return INSTANCE;
    }

    private RepositorioTramos(){
        super("Tramo");
    }
}
