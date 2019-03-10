package kz.halyq.speech_rec.rest.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kz.halyq.speech_rec.models.Questions;
import kz.halyq.speech_rec.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Assylkhan
 * on 10.03.2019
 * @project speech_rec
 */
@RestController
@RequestMapping(value = "api")
@CrossOrigin("*")
@Api(description = "Точка входа для манипуляции, модификации данных таблицы вопросов")
public class QuestionRestController {

    @Autowired
    QuestionService questionService;

    @PostMapping(path = "questions")
    @ApiOperation("Добавление вопроса")
    public ResponseEntity index(@RequestBody Questions question) {
        return ResponseEntity.status(questionService.add(question) ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST).build();

    }

    @GetMapping(path = "questions")
    @ApiOperation("Получение вопросов")
    public ResponseEntity getAll() {
        return new ResponseEntity(questionService.getAll(), HttpStatus.FOUND);
    }

    @GetMapping(path = "questions/{id}")
    @ApiOperation("Получение вопроса по ID")
    public ResponseEntity getById(@ApiParam("уникальный ID вопроса. Обязателен.") @PathVariable Long id){
        Questions question = questionService.getbyId(id);
        return question != null ? new ResponseEntity(question, HttpStatus.FOUND) : new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "questions/{q_code}")
    @ApiOperation("Получение вопроса по уникальному коду (q_code)")
    public ResponseEntity getByCode(@ApiParam("q_code  уникальный код вопроса. Обязателен.") @PathVariable String q_code){
        Questions question = questionService.getByCode(q_code);
        return question != null ? new ResponseEntity(question, HttpStatus.FOUND) : new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "questions/{id}", method = {RequestMethod.PUT , RequestMethod.PATCH})
    @ApiOperation("Оновление вопроса")
    public ResponseEntity update(@ApiParam("уникальный ID вопроса. Обязателен.") @PathVariable Long id,@ApiParam("Тело  вопроса с обновленными данными. Обязателен.") @RequestBody Questions question){
        question.setId(id);
        return new ResponseEntity(questionService.update(question) ? HttpStatus.ACCEPTED : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(path = "questions/{id}")
    @ApiOperation("Удаление вопроса")
    public ResponseEntity deleteById(@ApiParam("уникальный ID вопроса. Обязателен.") @PathVariable Long id){
        questionService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }



}
