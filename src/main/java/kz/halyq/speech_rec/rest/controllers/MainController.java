package kz.halyq.speech_rec.rest.controllers;

import kz.halyq.speech_rec.services.RecognizerService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * @author Assylkhan
 * on 17.01.2019
 * @project speech_rec
 */
@RestController
@RequestMapping("api")
public class MainController {
    private static String MIME_TYPE = "audio/wave";

    @Autowired
    private RecognizerService recognizerService;

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        String files = file.getContentType();
        if (!files.equals(MIME_TYPE)) {
            return "{ \"message\" : \"mime type error\" , \"error\" : true  }";
        }
        String result = "";
        try {
            result += recognizerService.recognize(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping("set_questions")
    public String setQuestions(String data) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/speech",
                    "root", "sbeezzs02");

            JSONObject jsonData = new JSONObject(data);
            String code = jsonData.get("code").toString();
            String answer = jsonData.get("answer").toString();
            int points = Integer.parseInt(jsonData.get("points").toString());

            String sql = "insert into questions (code, answer, points)\n" +
                    "values (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, code);
            preparedStatement.setString(2, answer);
            preparedStatement.setInt(3, points);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{ \"message\" : \"successful\" , \"error\" : false  }";
    }

}