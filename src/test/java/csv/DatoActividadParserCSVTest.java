package csv;

import domain.csv.DatoActividadParserCSV;
import domain.mediciones.Consumo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class DatoActividadParserCSVTest {
  String pathCSV = "src/main/java/domain/csv/Datos Actividades.csv";
  @Test
  void test() {
    DatoActividadParserCSV parser = new DatoActividadParserCSV();
    List<Consumo> lista = parser.parsearCSV(pathCSV);
    Assertions.assertEquals(2, lista.size());
  }
}