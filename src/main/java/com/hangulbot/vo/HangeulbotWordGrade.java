package com.hangulbot.vo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Created by jyson on 2016. 10. 12..
 */
@Entity
@ToString(callSuper=true, includeFieldNames=true)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,property = "@idx")
public class HangeulbotWordGrade {
    @Setter
    @Getter
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name = "word_id", referencedColumnName = "word_id")
    private HangeulbotWord hangeulbotWord;

    @Id
    @Column(name="word_id")
    @Setter
    @Getter
    private int wordId;

    @Column(name="answer_rate")
    @Setter
    @Getter
    private float answerRate;

    @Column(name="log_number")
    @Setter
    @Getter
    private long logNumber;

    @Column(name="mix_grade")
    @Setter
    @Getter
    private int mixGrade;

    @Column(name="semantic_grade")
    @Setter
    @Getter
    private int semanticGrade;

    @Column(name="wordGrade")
    @Setter
    @Getter
    private int wordGrade;

}
