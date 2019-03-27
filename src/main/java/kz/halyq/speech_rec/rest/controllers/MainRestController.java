package kz.halyq.speech_rec.rest.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kz.halyq.speech_rec.models.Questions;
import kz.halyq.speech_rec.services.QuestionService;
import kz.halyq.speech_rec.services.RecognizerService;
import kz.halyq.speech_rec.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * @author Assylkhan
 * on 17.01.2019
 * @project speech_rec
 */
@RestController
@CrossOrigin("*")
@RequestMapping("api")
@Api(description = "Точка входа для распознования")
public class MainRestController {
    private static String MIME_TYPE1 = "audio/wave";
    private static String MIME_TYPE2 = "audio/x-wav";

    @Autowired
    private QuestionService questionService;

    @Autowired
    private RecognizerService recognizerService;

    @PostMapping("/uploadFile")
    @ApiOperation("Распознование текста по файлу")
    public String uploadFile(@ApiParam("Файл для распознования. Обязателен.") @RequestParam("file") MultipartFile file) {
        String error = checkForMimeType(file);
        if(error.length() > 0){
            return error;
        }
        String result = "";
        try {
            result += recognizerService.recognize(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result.length() == 0) {
            result += "{\"message\" : \"not recognized\"  ,\"error\" : true }";
        }
        System.out.println(result);
        return result;
    }

    public String checkForMimeType(MultipartFile file){
//        String files = file.getContentType();
//        if (!files.equals(MIME_TYPE1) || !files.equals(MIME_TYPE2)) {
//            return "{ \"message\" : \"mime type error\" , \"error\" : true  }";
//        }else{
//            return "";
//        }

        return "";
    }

    @PostMapping("/recognize")
    @ApiOperation("Распознование текста по файлу и проверка уникальному коду")
    public String recognize(@ApiParam("Файл для распознования. Обязателен.") @RequestParam("file") MultipartFile file,
                            @ApiParam("Уникальный код вопроса (q_code). Обязателен.") @RequestParam(value = "q_code") String qCode) {

        String error = checkForMimeType(file);
        if(error.length() > 0){
            return error;
        }

        String result = "";
        try {
            result += recognizerService.recognize(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String result2 = "";

        Questions question = questionService.getByCode(qCode);
        result = result.trim();
        if(question!=null){
            if (question.getAnswer().equalsIgnoreCase(result)) {
                result2 += "{\"message\" : \"recognized\"  ,\"error\" : false, \"points\" : " + question.getPoints() + " , \"text\": \""+result+"\" , \"text_to_be_recognized\" : \""+question.getAnswer()+"\" }";
            } else {
                result2 += "{\"message\" : \"fail\"  ,\"error\" : false, \"points\" : " + 0 + " , \"text\": \""+result+"\" , \"text_to_be_recognized\" : \""+question.getAnswer()+"\" }";
            }
        }else{
            result2 += "{\"message\" : \"fail\"  ,\"error\" : true, \"points\" : " + 0 + " , \"text\": \""+result+"\" }";
        }
        System.out.println(result2);

        return result2;
    }

    @PostMapping("/verify")
    @ApiOperation("Валидация")
    public String verify(@ApiParam("Уникальный ID вопроса. Обязателен.")  @RequestParam(value = "q_id") Long qId,
                         @ApiParam("Номер вопроса. Обязателен.")  @RequestParam(value = "number") Long number) {
        String result = "";
        result += "{\"message\" : \"start to recognize\"  ,\"error\" : false }";
        return result;
    }

    @PostMapping("/questions/by/code")
    @ApiOperation("Получить вопрос по уникальному коду")
    public ResponseEntity getByCode(@ApiParam("Уникальный код вопроса. Обязателен.")  @RequestParam(value = "q_code") String qCode) {
        Questions question = questionService.getByCode(qCode);
        if (question == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageUtils.getMessageJSON(HttpStatus.NOT_FOUND.getReasonPhrase()));
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(question);
    }

}