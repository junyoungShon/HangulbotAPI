package com.hangulbot.vo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by jyson on 2016. 7. 21..
 */
@Entity
@ToString(callSuper=true, includeFieldNames=true)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,property = "@idx")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonSerialize
public class HangeulbotWord implements Serializable {
    @Id
    @Column(name="word_id",length = 30)
    @Setter
    @Getter
    @GeneratedValue
    private int wordId;


    @Column(name="word_korean",length = 30)
    @Setter
    @Getter
    private String wordKorean;

    @Column(name="word_category")
    @Setter
    @Getter
    private String wordCategory;


    @Column(name="block_mix_grade")
    @Setter
    @Getter
    private int blockMixGrade;

    @Column(name="word_guide_url")
    @Setter
    @Getter
    private String wordGuideUrl;

    @Column(name="word_image_url")
    @Setter
    @Getter
    private String wordImageUrl;


}
