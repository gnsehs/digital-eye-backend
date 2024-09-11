package hello.degitaleye.controller;

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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProxyServerController {
    @Value("${file.dir}")
    private String fileDir;
    private final String testUrl = "http://localhost:8080/test_rest_template_get";
    private final RestTemplate restTemplate;

    /**
     *
     * @param dialogue text/plain 으로 받는 dialogue
     * @return
     */
    @PostMapping("/tempupload_text")
    public ResponseEntity<String> tempStringUpload(@RequestBody String dialogue) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        log.info("===test0 dialogue = {}===", dialogue);
        body.add("dialogue", dialogue);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(testUrl, requestEntity, String.class);
        return stringResponseEntity;

    }

    /**
     *
     * @param dialogue 사용자 요청
     * @param file 이미지 파일
     * @return TODO 프론트엔드에 어떻게 리턴할지 생각하기
     */
    @PostMapping("/tempupload")
    public ResponseEntity<String> tempFileStringUpload(@RequestPart(value = "dialogue") String dialogue,
                                                       @RequestPart(value = "image", required = false) MultipartFile file) {

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        log.info("===test1 dialogue = {}===", dialogue);
        log.info("===test1 filename = {}===", file.getOriginalFilename());
        body.add("dialogue", dialogue);
        if (file != null) {
            body.add("image", file.getResource());
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(testUrl, requestEntity, String.class);



        return stringResponseEntity;


    }

    /**
     * 테스트용 플라스크 서버 역할
     * @param dialogue
     * @param file
     * @return
     */
    @PostMapping("/test_rest_template_get")
    public String tempAiServer(@RequestPart(value = "dialogue") String dialogue,
                               @RequestPart(value = "image", required = false) MultipartFile file) {
        log.info("===test2 dia = {}===", dialogue);
        if (file != null && !file.isEmpty()) {
            String fullPath = fileDir + file.getOriginalFilename();
            log.info("===test2 파일 저장 = {}===", fullPath);
            try {
                file.transferTo(new File(fullPath));
            } catch (IOException e) {
                log.error("test2 파일 저장 실패 ", e);
            }
        }

        return "good test2";

    }
}
