package hello.degitaleye.dto;

import lombok.Getter;

@Getter
public class AiFormDataResponseDto {
    private final String text;
    private final String image;

    public AiFormDataResponseDto(String translateText, String imageName) {
        this.text = translateText;
        this.image = imageName;
    }
}
