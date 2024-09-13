package hello.degitaleye.controller;

import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import com.deepl.api.Translator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProxyServerController {
    @Value("${file.dir}")
    private String fileDir; // 파일 저장
    private final String testUrl = "https://80cc-175-195-197-187.ngrok-free.app/send_check"; // flask server url
    // 번역
    private final Translator translator;
    // RestClient test
    private final RestClient restClient;

    /**
     *
     * @param dialogue text/plain 으로 받는 dialogue
     * @return
     */
    @PostMapping(value = "/tempupload_text", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> tempStringUpload(@RequestBody String dialogue) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        log.info("===test0 dialogue = {}===", dialogue);
        body.add("dialogue", dialogue);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<String> stringResponseEntity = restClient.post()
                .uri(testUrl)
                .body(body)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .retrieve()
                .toEntity(String.class);
        return stringResponseEntity;

    }

    /**
     *
     * @param dialogue 사용자 요청 문장
     * @param file AI 처리할 이미지
     * @return TODO client return 생각하기
     */
    @PostMapping(value = "/response-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> responseData(@RequestPart(value = "text") String dialogue,
                                               @RequestPart(value = "image", required = false) MultipartFile file) {

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        log.info("===test1 V2 dialogue = {}===", dialogue);

        try { // 번역처리
            TextResult textResult = translator.translateText(dialogue, "KO", "en-US");
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
                .uri(testUrl)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .toEntity(String.class); // ResponseEntity로 받기

        return entity;

    }




    /**
     * 테스트용 플라스크 서버 역할
     * @param dialogueT
     * @param file
     * @return
     */
    @PostMapping(value = "/test_rest_template_get", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String tempAiServer(@RequestPart(value = "dialogue_T") String dialogueT,
                               @RequestPart(value = "image", required = false) MultipartFile file) {
        log.info("===test2 dialouge_T = {}===", dialogueT);
        saveFile(file);
        return dialogueT;

    }

    /**
     * 파일 저장 메서드
     * @param file
     */
    private void saveFile(MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            String fullPath = fileDir + file.getOriginalFilename();
            log.info("===test2 파일 저장 = {}===", fullPath);
            try {
                file.transferTo(new File(fullPath));
            } catch (IOException e) {
                log.error("test2 파일 저장 실패 ", e);
            }
        }
    }
}
