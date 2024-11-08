package hello.degitaleye.controller;


import hello.degitaleye.dto.LinkResponseDto;
import hello.degitaleye.service.LinkTestService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LinkTestController {

    private final LinkTestService linkTestService;

    @GetMapping(value = "/init-test", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LinkResponseDto> initTest() throws UnsupportedEncodingException {
        LinkResponseDto weather = linkTestService.getWeather();
        return ResponseEntity.ok().body(weather);
    }
}
