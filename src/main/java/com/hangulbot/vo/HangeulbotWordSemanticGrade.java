package com.hangulbot.vo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Created by jyson on 2016. 8. 31..
 */
@Entity
@ToString(callSuper=true, includeFieldNames=true)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,property = "@idx")
public class HangeulbotWordSemanticGrade {
    @Column(name = "semantic_grade_idx", length = 11)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter
    @Getter
    @Id
    private int semanticGradeIdx;

    @Setter
    @Getter
    @OneToOne(fetch= FetchType.LAZY, cascade= CascadeType.ALL)
    @JoinColumn(name="word_id")
    private HangeulbotWord hangeulbotWord;

    @Setter
    @Getter
    @Column(name="avg_age",length=11)
    private float avgAge;

    @Setter
    @Getter
    @Column(name="test_count",length=11)
    private int testCount;

}
