package kz.halyq.speech_rec.services;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.decoder.adaptation.Stats;
import edu.cmu.sphinx.decoder.adaptation.Transform;
import edu.cmu.sphinx.result.WordResult;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
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
        File checkFile = ResourceUtils.getFile( "classpath:cmusphinx-kz-5.2/model_parameters/kz.cd_cont_200" ).getAbsoluteFile();
        configuration
                .setAcousticModelPath(checkFile.getAbsolutePath());
        checkFile = ResourceUtils.getFile( "classpath:cmusphinx-kz-5.2/etc/kz.dic" ).getAbsoluteFile();
        configuration
                .setDictionaryPath(checkFile.getAbsolutePath());
        checkFile = ResourceUtils.getFile( "classpath:cmusphinx-kz-5.2/etc/kz.ug.lm" ).getAbsoluteFile();
        configuration
                .setLanguageModelPath(checkFile.getAbsolutePath());
        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
        SpeechResult result;
        InputStream stream = file.getInputStream();
        recognizer.startRecognition(stream);
        stream.skip(44);
        String sentences = "";
        while ((result = recognizer.getResult()) != null) {
            sentences += result.getHypothesis();
        }
        recognizer.stopRecognition();

        return "{ \"message\" : \"" + (sentences.length() > 0 ? sentences : "not recognized") + "\"  , \"error\" : false} ";
    }

}
