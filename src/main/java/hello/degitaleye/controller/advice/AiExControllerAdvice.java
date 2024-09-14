package hello.degitaleye.controller.advice;

import com.deepl.api.DeepLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class AiExControllerAdvice {

    @ExceptionHandler(DeepLException.class)
    public ResponseEntity<ErrorResult> deepLExHandle(DeepLException e) {
        log.error("[exception-handle] ex", e);
        return ResponseEntity
                .internalServerError()
                .body(new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "번역 오류 발생!"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResult> exHandle(Exception e) {
        log.error("[exception-handle] ex", e);
        return ResponseEntity
                .internalServerError()
                .body(new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "알 수 없는 오류 발생"));
    }
}
