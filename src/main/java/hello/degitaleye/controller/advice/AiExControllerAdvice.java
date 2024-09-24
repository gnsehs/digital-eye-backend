package hello.degitaleye.controller.advice;

import com.deepl.api.DeepLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class AiExControllerAdvice {

    @ExceptionHandler(DeepLException.class)
    public ResponseEntity<ErrorResult> deepLExHandle(DeepLException e) {
        log.error("[exception-handle] ex", e);
        return ResponseEntity
                .internalServerError()
                .body(new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "번역 오류 발생!",""));
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResult> missingRequestValueExHandle(MissingServletRequestPartException e) {
        log.error("[exception-handle] ex", e);
        return ResponseEntity
                .badRequest()
                .body(new ErrorResult(HttpStatus.BAD_REQUEST.value(), "잘못된 요청 값", "요청 값을 다시 말씀 해 주세요"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResult> exHandle(Exception e) {
        log.error("[exception-handle] ex", e);
        return ResponseEntity
                .internalServerError()
                .body(new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "알 수 없는 오류 발생","요청을 다시 시도해 주세요."));
    }
}
