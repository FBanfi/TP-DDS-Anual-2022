package domain.mediciones;

import java.util.HashMap;
import java.util.Map;

public enum TipoConsumo {
  GAS_NATURAL(Actividad.COMBUSTION_FIJA, Alcance.EMISIONES_DIRECTAS),
  DIESEL(Actividad.COMBUSTION_FIJA, Alcance.EMISIONES_DIRECTAS),
  GASOIL(Actividad.COMBUSTION_FIJA, Alcance.EMISIONES_DIRECTAS),
  NAFTA(Actividad.COMBUSTION_FIJA, Alcance.EMISIONES_DIRECTAS),
  CARBON(Actividad.COMBUSTION_FIJA, Alcance.EMISIONES_DIRECTAS),
  COMBUSTIBLE_CONSUMIDO_GASOIL(Actividad.COMBUSTION_MOVIL, Alcance.EMISIONES_DIRECTAS),
  COMBUSTIBLE_CONSUMIDO_NAFTA(Actividad.COMBUSTION_MOVIL, Alcance.EMISIONES_DIRECTAS),
  ELECTRICIDAD(Actividad.ELECTRICIDAD_CONSUMIDA, Alcance.EMISION_INDICRECTAS_ELECTRICIDAD),
  MEDIO_TRANSPORTE_CAMION(Actividad.ELECTRICIDAD_CONSUMIDA, Alcance.EMISION_INDICRECTAS_ELECTRICIDAD),
  MEDIO_TRANSPORTE_UTILITARIO_LIVIANO(Actividad.LOGISTICA_DE_PRODUCTO, Alcance.EMISION_INDIRECTAS_NO_CONTROLADAS),
  DISTANCIA_MEDIA_RECORRIDA(Actividad.LOGISTICA_DE_PRODUCTO, Alcance.EMISION_INDIRECTAS_NO_CONTROLADAS);

  private static Map<TipoConsumo, Unidad> unidades = crearMapaUnidades();
  private Map<TipoConsumo, Actividad> actividades = new HashMap<>();
  private Map<TipoConsumo, Alcance> alcances = new HashMap<>();
  private TipoConsumo(Actividad a, Alcance aa) {
  }
  TipoConsumo() {
  }


  public Unidad getUnidad() {
    return unidades.get(this);
  }

  private static Map<TipoConsumo, Unidad> crearMapaUnidades() {
    Map<TipoConsumo, Unidad> unidades = new HashMap<>();
    unidades.put(GAS_NATURAL, Unidad.M3);
    unidades.put(DIESEL, Unidad.LT);
    unidades.put(GASOIL, Unidad.LT);
    unidades.put(NAFTA, Unidad.LT);
    unidades.put(CARBON, Unidad.KG);
    unidades.put(COMBUSTIBLE_CONSUMIDO_GASOIL, Unidad.LTS);
    unidades.put(COMBUSTIBLE_CONSUMIDO_NAFTA, Unidad.LTS);
    unidades.put(ELECTRICIDAD, Unidad.KWH);
    unidades.put(MEDIO_TRANSPORTE_CAMION, Unidad.KWH);
    unidades.put(MEDIO_TRANSPORTE_UTILITARIO_LIVIANO, Unidad.SIN_UNIDAD);
    unidades.put(DISTANCIA_MEDIA_RECORRIDA, Unidad.KM);
    return unidades;
  }

  public String getNombre() {
    return this.toString();
  }

}
