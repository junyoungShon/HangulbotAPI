package com.hangulbot.vo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/**
 * Created by jyson on 2016. 7. 21..
 */
@Entity
@ToString(callSuper=true, includeFieldNames=true)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,property = "@idx")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonSerialize
public class HangeulbotContent {

    @Column(name = "content_idx")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter
    @Getter
    private int contentIdx;

    @Id
    @Column(name="content_id",length = 30)
    @Setter
    @Getter
    private String contentId;

    @Column(name="content_title",nullable = false)
    @Setter
    @Getter
    private String contentTitle;

    @Column(name="content_category",nullable = false)
    @Setter
    @Getter
    private String contentCategory;

    @Column(name="content_inst",nullable = false)
    @Setter
    @Getter
    private String contentInst;

    @Column(name="content_thumb",nullable = false)
    @Setter
    @Getter
    private String contentThumb;

    @Column(name="content_popup",nullable = false)
    @Setter
    @Getter
    private String contentPopup;

    @Column(name="content_link",nullable = false)
    @Setter
    @Getter
    private String contentLink;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Setter
    @Getter
    private List<HangeulbotWord> HangeulbotWordList;

}
