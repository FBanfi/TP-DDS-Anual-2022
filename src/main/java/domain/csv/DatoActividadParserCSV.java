package domain.csv;

import domain.exceptions.ExcepcionParseoCSV;
import domain.mediciones.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatoActividadParserCSV {
  Periodicidad periodicidad;

  public List<Consumo> parsearCSV(String csvPath) {
    File csvFile = new File(csvPath);
    List<Consumo> consumos = new ArrayList<>();
    try {
      InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(csvFile));
      CSVParser csvParser = CSVFormat.DEFAULT.parse(inputStreamReader);
      boolean validarHeader = true ;
      for (CSVRecord csvRecord : csvParser) {
        if (!validarHeader) {
          double valor = Double.parseDouble(csvRecord.get(1));
          String tipoConsumoString = csvRecord.get(0);
          Map<String, TipoConsumo> mapa = getMapConConsumos();
          Consumo nuevoConsumo = new  Consumo(valor,
              mapa.get(tipoConsumoString),
              obtnerPeriodicidadValidada(csvRecord.get(2), csvRecord.get(3)),
              new FactorEmision(mapa.get(tipoConsumoString).getUnidad())
          );
          consumos.add(nuevoConsumo);
        }
        else {
          validarHeader = false;
          validarEstructuraArchivo(csvRecord.get(0), csvRecord.get(1), csvRecord.get(2),  csvRecord.get(3));
        }
      }
    } catch (Exception e) {
      throw new ExcepcionParseoCSV("Error in Parsing CSV File" + e);
    }
    return consumos;
  }

  public void validarEstructuraArchivo(String campo1, String campo2, String campo3, String campo4 ){
    if(!campo1.equalsIgnoreCase("CONSUMO_TIPO") || !campo2.equalsIgnoreCase("CONSUMO_VALOR") || !campo3.equalsIgnoreCase("CONSUMO_PERIODICIDAD") || !campo4.equalsIgnoreCase("PERIODO_IMPUTACION")) {
      throw new RuntimeException("Estructura de archivo invalida");
    }
  }

  private Map<String, TipoConsumo> getMapConConsumos() {
      Map<String, TipoConsumo> mapa = new HashMap<String, TipoConsumo>();
      mapa.put("Gas Natural", TipoConsumo.GAS_NATURAL);
      mapa.put("Diesel", TipoConsumo.DIESEL);
      mapa.put("Gasoil", TipoConsumo.GASOIL);
      mapa.put("Nafta", TipoConsumo.NAFTA);
      mapa.put("Carbon", TipoConsumo.CARBON);
      mapa.put("Combustible consumido Gasoil", TipoConsumo.COMBUSTIBLE_CONSUMIDO_GASOIL);
      mapa.put("Combustible consumido Nafta", TipoConsumo.COMBUSTIBLE_CONSUMIDO_NAFTA);
      mapa.put("Electricidad", TipoConsumo.ELECTRICIDAD);
      mapa.put("Camion de Carga", TipoConsumo.MEDIO_TRANSPORTE_CAMION);
      mapa.put("Utilitario Liviano", TipoConsumo.MEDIO_TRANSPORTE_UTILITARIO_LIVIANO);
      mapa.put("Distancia media recorrida", TipoConsumo.DISTANCIA_MEDIA_RECORRIDA);
      return mapa;
  }

  private Periodicidad obtnerPeriodicidadValidada(String tipoPeriodicidad, String periodicidadString) {
    Periodicidad periodicidad;
    Pattern paternMesAnio = Pattern.compile("[0-3][0-9]/[0-9][0-9][0-9][0-9]$", Pattern.CASE_INSENSITIVE);
    Pattern paternAnio = Pattern.compile("^([0-9][0-9][0-9][0-9])$", Pattern.CASE_INSENSITIVE);
    Matcher matcherMesAnio = paternMesAnio.matcher(periodicidadString);
    Matcher matcherAnio = paternAnio.matcher(periodicidadString);

    if (matcherMesAnio.find() && TipoPeriodicidad.valueOf(tipoPeriodicidad.toUpperCase())==TipoPeriodicidad.MENSUAL) {
      periodicidad = new Periodicidad(TipoPeriodicidad.MENSUAL, periodicidadString);
    } else if (matcherAnio.find() && TipoPeriodicidad.valueOf(tipoPeriodicidad.toUpperCase())==TipoPeriodicidad.ANUAL) {
      periodicidad = new Periodicidad(TipoPeriodicidad.ANUAL, periodicidadString);
    } else {
      throw new ExcepcionParseoCSV("TipoPeriodicidad no contemplada");
    }
    return periodicidad;
  }
  public Periodicidad getPeriodicidad() {
    return periodicidad;
  }
}