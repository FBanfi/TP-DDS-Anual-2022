package domain.mediciones;

import domain.exceptions.ExcepcionParseoCSV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public enum TipoPeriodicidad {
  MENSUAL,
  ANUAL;



  public String getNombre() {
    return this.toString();
  }

  public static TipoPeriodicidad obtnerPeriodicidadValidada(String periodicidadString) {
    TipoPeriodicidad periodicidad;
    Pattern paternMesAnio = Pattern.compile("[0-3][0-9]/[0-9][0-9][0-9][0-9]$", Pattern.CASE_INSENSITIVE);
    Pattern paternAnio = Pattern.compile("^([0-9][0-9][0-9][0-9])$", Pattern.CASE_INSENSITIVE);
    Matcher matcherMesAnio = paternMesAnio.matcher(periodicidadString);
    Matcher matcherAnio = paternAnio.matcher(periodicidadString);

    if (matcherMesAnio.find()) {
      periodicidad = TipoPeriodicidad.MENSUAL;
    } else if (matcherAnio.find()) {
      periodicidad = TipoPeriodicidad.ANUAL;
    } else {
      throw new ExcepcionParseoCSV("TipoPeriodicidad no contemplada");
    }
    return periodicidad;
  }
}
