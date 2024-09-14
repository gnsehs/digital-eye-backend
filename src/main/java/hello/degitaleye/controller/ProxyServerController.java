package hello.degitaleye.controller;

import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import com.deepl.api.Translator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProxyServerController {
    @Value("${file.dir}")
    private String fileDir; // 파일 저장
    @Value("${flask.base.url}")
    private String flaskBaseUrl;

    // 번역
    private final Translator translator;
    private final RestClient restClient;
    //temp
    private final String testFlaskUrl = "/test_rest_template_get";

    /**
     * 단일 이미지 파일을 받는 컨트롤러
     * @param file
     * @return
     */
    @PostMapping(value = "/ai-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> aiImage(
            @RequestPart(value = "image") MultipartFile file) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        if (file != null) {
            log.info("===test1 V2 filename = {}===", file.getOriginalFilename());
            body.add("image", file.getResource());
        }

        ResponseEntity<String> entity = restClient.post()
                .uri(flaskBaseUrl + testFlaskUrl)
                .body(body)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .retrieve()
                .toEntity(String.class);

        return entity;

    }

    /**
     *
     * @param text 사용자 요청 문장
     * @param file AI 처리할 이미지
     * @return TODO client return 생각하기, @ExceptionHandler 작성하기
     */
    @PostMapping(value = "/ai-form", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> aiForm(@RequestPart(value = "text") String text,
                                         @RequestPart(value = "image") MultipartFile file) {

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        log.info("===test1 V2 dialogue = {}===", text);

        try { // 번역처리
            TextResult textResult = translator.translateText(text, "KO", "en-US");
            body.add("text", textResult.getText());
            log.info("===test1 V2 dialogueT = {}===", textResult.getText());
        } catch (DeepLException e) {
            log.error("DeepL 오류", e);
            return ResponseEntity.internalServerError().body("Sorry DeepL error");
        } catch (InterruptedException e) {
            return ResponseEntity.internalServerError().body("Sorry Server error");
        }

        if (file != null) {
            log.info("===test1 V2 filename = {}===", file.getOriginalFilename());
            body.add("image", file.getResource());
        }

        ResponseEntity<String> entity = restClient.post()
                .uri(flaskBaseUrl + testFlaskUrl)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .toEntity(String.class); // ResponseEntity로 받기

        return entity;

    }







}

