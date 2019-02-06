package kz.halyq.speech_rec.repositories;

import kz.halyq.speech_rec.models.Questions;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Assylkhan
 * on 05.02.2019
 * @project speech_rec
 */
@Repository
public interface QuestionRepository extends CrudRepository<Questions, Long>{

    public Questions findByCode(@Param("code") String code);

}
