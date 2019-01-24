package kz.halyq.speech_rec.rest.controllers;

import kz.halyq.speech_rec.services.RecognizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;

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
        if(!files.equals(MIME_TYPE)){
            return  "{ \"message\" : \"mime type error\" , \"error\" : true  }";
        }
        String result = "";
        try{
            result += recognizerService.recognize(file);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  result;
    }

}