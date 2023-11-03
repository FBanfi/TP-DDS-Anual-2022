package domain.sugerenciasYnotificaciones;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import domain.utils.TokenPropertiesUtil;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("celular")
public class NotificacionTwilio extends Notificacion {
   public NotificacionTwilio() {

    }

    public void notificar(String mensaje, String numero, String mailDestino, String asunto){
        this.enviarMensaje(numero, mensaje);
    }

    private void enviarMensaje(String numero, String mensaje) {
        configuruarTwillio();
        Message message = Message.creator(
                new PhoneNumber("whatsapp:"+numero),
                new PhoneNumber("whatsapp:"+TokenPropertiesUtil.getString("t.wappNumber")),
                mensaje)
            .create();
    }
    private void configuruarTwillio() {
        Twilio.init(
            TokenPropertiesUtil.getString("t.wappSid"),
            TokenPropertiesUtil.getString("t.wappAuth")
        );
    }

}
