package kz.halyq.speech_rec.controllers;

import kz.halyq.speech_rec.models.Questions;
import kz.halyq.speech_rec.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * @author Assylkhan
 * on 05.02.2019
 * @project speech_rec
 */
@Controller
public class QuestionController {


    private QuestionService questionService;


    public QuestionService getQuestionService() {
        return questionService;
    }

    @Autowired
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String add(@RequestParam String code, @RequestParam String answer, @RequestParam Integer points){
        String resp = "redirect:/";
        if(questionService.getByCode(code) != null){
             resp +="?error=1";
        }else{
            Questions question = new Questions(code, answer, points);
            questionService.add(question);
        }
        return resp;
    }

    @PostMapping(value = "/edit/{id}")
    public String edit(@PathVariable Long id , @RequestParam String code, @RequestParam String answer, @RequestParam Integer points){
        String resp = "redirect:/";
        Questions temp = questionService.getByCode(code);
        if(temp != null && !temp.getId().equals(id)){
            resp +="?error=1";
        }else{
            Questions question = questionService.getbyId(id);
            question.setAnswer(answer);
            question.setCode(code);
            question.setPoints(points);
            questionService.update(question);
        }
        return resp;
    }

    @GetMapping(value = "/edit/{id}")
    public ModelAndView update(@PathVariable Long id){
        ModelAndView mw = new ModelAndView("edit");
        mw.addObject("question", questionService.getbyId(id));
        return mw;
    }

    @PostMapping(value = "/delete/{id}")
    public String delete(@PathVariable Long id){
        questionService.deleteById(id);
        return "redirect: /";
    }

    @GetMapping(value = "/questions/{id}")
    public ModelAndView single(@PathVariable Long id){
        ModelAndView mw = new ModelAndView("index");
        Questions question = questionService.getbyId(id);
        mw.addObject("question", question);
        return mw;
    }

    @GetMapping("/")
    public ModelAndView index(){
        ModelAndView mw  = new ModelAndView("index");
        mw.addObject("questions" , questionService.getAll());
        return mw;
    }

}
