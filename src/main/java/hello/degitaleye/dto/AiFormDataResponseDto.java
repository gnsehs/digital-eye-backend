package hello.degitaleye.dto;

import lombok.Getter;

// TODO 임시 dto -> 규칙 정해야함
@Getter
public class AiFormDataResponseDto {
    private final String text;
    private final String image;

    public AiFormDataResponseDto(String translateText, String imageName) {
        this.text = translateText;
        this.image = imageName;
    }
}
