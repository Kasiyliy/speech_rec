package kz.halyq.speech_rec.services;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.decoder.adaptation.Stats;
import edu.cmu.sphinx.decoder.adaptation.Transform;
import edu.cmu.sphinx.result.WordResult;
import org.springframework.stereotype.Service;
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

        Configuration configuration = new Configuration();


        configuration
                .setAcousticModelPath("src/main/resources/cmusphinx-kz-5.2/model_parameters/kz.cd_cont_200");
        configuration
                .setDictionaryPath("src/main/resources/cmusphinx-kz-5.2/etc/kz.dic");
        configuration
                .setLanguageModelPath("src/main/resources/cmusphinx-kz-5.2/etc/kz.ug.lm");

        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(
                configuration);
        InputStream stream = file.getInputStream();
        stream.skip(44);


        recognizer.startRecognition(stream);
        SpeechResult result;
        while ((result = recognizer.getResult()) != null) {

            System.out.format("Hypothesis: %s\n", result.getHypothesis());

            System.out.println("List of recognized words and their times:");
            for (WordResult r : result.getWords()) {
                System.out.println(r);
            }

            System.out.println("Best 3 hypothesis:");
            for (String s : result.getNbest(3))
                System.out.println(s);

        }
        recognizer.stopRecognition();


        stream = file.getInputStream();
        stream.skip(44);

        Stats stats = recognizer.createStats(1);
        recognizer.startRecognition(stream);
        while ((result = recognizer.getResult()) != null) {
            stats.collect(result);
        }
        recognizer.stopRecognition();

        Transform transform = stats.createTransform();
        recognizer.setTransform(transform);

        // Decode again with updated transform
        stream = file.getInputStream();
        stream.skip(44);
        recognizer.startRecognition(stream);
        String sentences = "";
        while ((result = recognizer.getResult()) != null) {
            sentences += result.getHypothesis();
        }
        recognizer.stopRecognition();

        return "{ \"message\" : \"" + (sentences.length() > 0 ? sentences : "not recognized") + "\"  , \"error\" : false} ";
    }

}
