package kz.halyq.speech_rec.services;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

/**
 * @author Assylkhan
 * on 17.01.2019
 * @project speech_rec
 */
@Service
public class RecognizerService {

    public String recognize(MultipartFile file) throws Exception {
        System.out.println("Loading models...");
        ApplicationHome home = new ApplicationHome(this.getClass());
        Configuration configuration = new Configuration();
        File checkFile = ResourceUtils.getFile("classpath:kz_model/model_parameters/kz_model.ci_cont").getAbsoluteFile();
        configuration
                .setAcousticModelPath(checkFile.getAbsolutePath());
        checkFile = ResourceUtils.getFile("classpath:kz_model/etc/kz_model.dic").getAbsoluteFile();
        configuration
                .setDictionaryPath(checkFile.getAbsolutePath());
        checkFile = ResourceUtils.getFile("classpath:kz_model/etc/kz_model.lm").getAbsoluteFile();
        configuration
                .setLanguageModelPath(checkFile.getAbsolutePath());
        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
        SpeechResult result;
        InputStream stream = file.getInputStream();
        recognizer.startRecognition(stream);
        stream.skip(44);
        String sentences = "";
        while ((result = recognizer.getResult()) != null) {
            if(!result.getHypothesis().equalsIgnoreCase("<sil>")){
                sentences += result.getHypothesis() + " ";
            }
        }
        recognizer.stopRecognition();

        return sentences;
    }

}
