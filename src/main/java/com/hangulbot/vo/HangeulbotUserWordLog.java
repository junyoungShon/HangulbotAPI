package com.hangulbot.vo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jyson on 2016. 7. 21..
 */
@Entity
@ToString(callSuper=true, includeFieldNames=true)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,property = "@idx")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonSerialize
public class HangeulbotUserWordLog {

    @Column(name = "user_word_log_idx",length = 11)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter
    @Getter
    @Id
    private int userWordLogIdx;


    @Setter
    @Getter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="word_id")
    private HangeulbotWord hangeulbotWord;

    @Setter
    @Getter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="child_id")
    private HangeulbotChild hangeulbotChild;

    @Setter
    @Getter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="content_id")
    private HangeulbotContent hangeulbotContent;

    @Setter
    @Getter
    @Column(name="start_date")
    private Date startDate;

    @Setter
    @Getter
    @Column(name="end_date")
    private Date endDate;

    @Setter
    @Getter
    @Column(name="inserted_Answer")
    private String insertedAnswer;

    @Getter
    @Setter
    @Column(name="guide_shown", length=1)
    private boolean guideShown;

    @Getter
    @Column(name="is_correct", length=1)
    private boolean isCorrect;

    @Getter
    @Setter
    @Column(name="time_required")
    private int timeRequired;

    public void setIsCorrect(Boolean isCorrect){
        this.isCorrect = isCorrect;
    }
}
