package serializationtest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AutenticationUserWrapper {

    @JsonProperty("User")
    private AutenticationUser autenticationUser;

    public AutenticationUser getAutenticationUser() {
        return autenticationUser;
    }

    public void setAutenticationUser(AutenticationUser autenticationUser) {
        this.autenticationUser = autenticationUser;
    }

    @Override
    public String toString() {
        return "AutenticationUserWrapper{" +
                "autenticationUser=" + autenticationUser +
                '}';
    }
}
