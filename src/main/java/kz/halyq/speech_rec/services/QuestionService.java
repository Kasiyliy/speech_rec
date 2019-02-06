package kz.halyq.speech_rec.services;

import kz.halyq.speech_rec.repositories.QuestionRepository;
import kz.halyq.speech_rec.models.Questions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Assylkhan
 * on 05.02.2019
 * @project speech_rec
 */
@Service
public class QuestionService {

    private QuestionRepository questionRepository;

    public QuestionRepository getQuestionRepository() {
        return questionRepository;
    }

    @Autowired
    public void setQuestionRepository(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public boolean add(Questions question){
        if(question.getId()==null){
            questionRepository.save(question);
            return true;
        }
        return false;
    }

    public Questions getByCode(String code){
        return  questionRepository.findByCode(code);
    }

    public boolean update(Questions question){
        if(question.getId()!=null){
            questionRepository.save(question);
            return true;
        }
        return false;
    }

    public boolean delete(Questions question){
        if(question.getId() != null){
            questionRepository.deleteById(question.getId());
            return true;
        }
        return false;
    }

    public void deleteById(Long id){
        questionRepository.deleteById(id);
    }

    public List<Questions> getAll(){
        List<Questions> questions = new ArrayList<>();
        for (Questions question: questionRepository.findAll()) {
            questions.add(question);
        }
        return questions;
    }

    public Questions getbyId(Long id){
        Optional<Questions> questionsOptional = questionRepository.findById(id);
        if(questionsOptional.isPresent()){
            return  questionsOptional.get();
        }else{
            return null;
        }
    }
}
