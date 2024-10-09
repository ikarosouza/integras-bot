package br.gov.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TwilioMessage {

    @JsonProperty("Body")
    private String body;

    @JsonProperty("From")
    private String from;

    @JsonProperty("To")
    private String to;

    @JsonProperty("MessageSid")
    private String messageSid;

    @JsonProperty("AccountSid")
    private String accountSid;
}