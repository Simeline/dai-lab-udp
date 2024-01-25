package ch.heig.dai;

public class Message {
    private final String uuid;
    private final String instrument;
    private final long lastActivity;

    public Message(String uuid, String instrument, long lastActivity) {
        this.uuid = uuid;
        this.instrument = instrument;
        this.lastActivity = lastActivity;
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

    public String getSound() {
        switch (instrument) {
            case "piano":
                return "ti-ta-ti";
            case "trumpet":
                return "pouet";
            case "flute":
                return "trulu";
            case "violin":
                return "gzi-gzi";
            case "drum":
                return "boum-boum";
            default:
                return "unknown";
        }
    }
}