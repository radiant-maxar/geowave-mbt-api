package mil.nga.giat.mbt.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfile {
    private final String dn;
    private final String firstName;
    private final String lastName;
    private final String uid;

    @JsonCreator
    public UserProfile(
            @JsonProperty("DN") String dn,
            @JsonProperty("uid") String uid,
            @JsonProperty("firstname") String firstName,
            @JsonProperty("lastname") String lastName) {
        this.dn = dn;
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getDN() {
        return dn;
    }

    public String getUID() {
        return uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
