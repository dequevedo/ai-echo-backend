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

    private String input = "Today is a wonderful day to build something people love!";

    private String voice = "alloy";

}