package hello.degitaleye.service;

import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import com.deepl.api.Translator;
import hello.degitaleye.dto.AiFormDataResponseDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProxyServerService {


    @Value("${flask.base.url}")
    private String flaskBaseUrl;
    //temp
    private final String testFlaskUrl = "/test_rest_template_get";
    // flask url
    private String flaskUrl;



    private final Translator translator;
    private final RestClient restClient;


    @PostConstruct
    private void urlInit() {
        flaskUrl = flaskBaseUrl + testFlaskUrl;
    }

    public String getAiImageDataResponse(MultipartFile file) {

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        if (file != null) {
            log.info("===test1 V2 filename = {}===", file.getOriginalFilename());
            body.add("image", file.getResource());
        }

        return restClient.post()
                .uri(flaskUrl)
                .body(body)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .retrieve()
                .body(String.class);

    }


    public AiFormDataResponseDto  getAiFormDataResponse(String text, MultipartFile file) throws DeepLException, InterruptedException {

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        log.info("===test1 V2 dialogue = {}===", text);

        try { // 번역처리
            TextResult textResult = translator.translateText(text, "KO", "en-US");
            body.add("text", textResult.getText());
            log.info("===test1 V2 dialogueT = {}===", textResult.getText());
        } catch (DeepLException e) {
            log.error("DeepL 번역 오류", e);
            throw e;
        } catch (InterruptedException e) {
            throw e;
        }

        if (file != null) {
            log.info("===test1 V2 filename = {}===", file.getOriginalFilename());
            body.add("image", file.getResource());
        }

         return restClient.post()
                .uri(flaskUrl)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .body(AiFormDataResponseDto.class); // 이때 response가 json임이 명시 되어야 함

    }

}
