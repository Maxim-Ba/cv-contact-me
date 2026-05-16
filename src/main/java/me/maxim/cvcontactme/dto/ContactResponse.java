package me.maxim.cvcontactme.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContactResponse {

    private final boolean ok;
    private final String error;

    private ContactResponse(boolean ok, String error) {
        this.ok = ok;
        this.error = error;
    }

    public static ContactResponse success() {
        return new ContactResponse(true, null);
    }

    public static ContactResponse failure(String error) {
        return new ContactResponse(false, error);
    }
}
