package hello.degitaleye.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProxyServerControllerV2 {
    @Value("${file.dir}")
    private String fileDir;
    @Value("${flask.dir}")
    private String testUrl;

    // RestClient test
    private final RestClient restClient;

    @PostMapping(value = "/response-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> responseData(@RequestPart(value = "dialogue") String dialogue,
                                               @RequestPart(value = "image", required = false) MultipartFile file) {

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        log.info("===test1 V2 dialogue = {}===", dialogue);

        body.add("dialogue", dialogue);
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

}
