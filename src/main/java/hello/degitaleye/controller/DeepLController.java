package hello.degitaleye.controller;

import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import com.deepl.api.Translator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DeepLController {

    @Value("${deepL-admin-key}")
    private String deeplKey;


    @PostMapping("/deepl-test")
    public ResponseEntity<String> deeplTest(@RequestBody String dialogue) throws DeepLException, InterruptedException {
        Translator translator = new Translator(deeplKey);
        TextResult textResult = translator.translateText(dialogue, "EN", "KO");
        return ResponseEntity.ok(textResult.getText());
    }
}
