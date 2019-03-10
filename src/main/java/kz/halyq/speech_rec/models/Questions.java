package kz.halyq.speech_rec.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * @author Assylkhan
 * on 05.02.2019
 * @project speech_rec
 */
@Entity
@Table(name = "questions")
@ApiModel(description = "Таблица которая описывает модель вопроса")
public class Questions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @ApiModelProperty(notes = "Уникальный идентификатор вопроса", readOnly = true)
    private Long id;

    @Column(name = "code" ,unique = true)
    @NotBlank(message = "code is mandatory")
    @ApiModelProperty(notes = "Уникальный идентификатор вопроса в виде кода")
    private String code;

    @Column(name = "answer")
    @NotBlank(message = "answer is mandatory")
    @ApiModelProperty(notes = "Ответ вопроса")
    private String answer;

    @Column(name = "points")
    @ApiModelProperty(notes = "Баллы за вопрос")
    private Integer points;

    public Questions() {
    }

    public Questions(String code, String answer, Integer points) {
        this.code = code;
        this.answer = answer;
        this.points = points;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
