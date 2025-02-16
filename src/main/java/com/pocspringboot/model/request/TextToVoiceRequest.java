package com.pocspringboot.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextToVoiceRequest {

    private String model = "tts-1";

    @NotEmpty(message = "input is mandatory")
    private String input;

    private String voice = "nova";

}