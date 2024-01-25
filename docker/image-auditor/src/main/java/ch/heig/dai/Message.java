package ch.heig.dai;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    private final String uuid;
    private final String instrument;
    private final long lastActivity;



    @JsonCreator
    public Message(String uuid,String sound) {
        this.uuid = uuid;
        this.lastActivity = System.currentTimeMillis();
        this.instrument = getInstrumentFromSound(sound);
    }




    public String getUuid() {
        return uuid;
    }

    public String getInstrument() {
        return instrument;
    }

    public long getLastActivity() {
        return lastActivity;
    }

    private String getInstrumentFromSound(String sound) {
        // Implémentez la logique pour obtenir l'instrument à partir du son
        // Ici, nous faisons une simple correspondance inverse du son à l'instrument
        switch (sound) {
            case "ti-ta-ti":
                return "piano";
            case "pouet":
                return "trumpet";
            case "trulu":
                return "flute";
            case "gzi-gzi":
                return "violin";
            case "boum-boum":
                return "drum";
            default:
                return "unknown";
        }
    }
}