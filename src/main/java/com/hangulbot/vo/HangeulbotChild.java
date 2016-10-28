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
 * Created by jyson on 2016. 7. 6..
 */
@Entity
@ToString(callSuper=true, includeFieldNames=true)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,property = "@idx")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonSerialize
public class HangeulbotChild {

    @Column(name = "child_idx", length = 11)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter
    @Getter
    private int childIdx;

    @Id
    @Column(name="child_id", length = 30)
    @Setter
    @Getter
    private String childId;

    @Column(name="child_name", length = 30)
    @Setter
    @Getter
    private String childName;

    @Column(name="child_birth")
    @Setter
    @Getter
    private Date childBirth;

    @Column(name="child_num", length = 1)
    @Setter
    @Getter
    private int childNum;

    @Column(name="child_exp", length = 11)
    @Setter
    @Getter
    private int childExp;

    @Column(name="child_photo", length = 50)
    @Setter
    @Getter
    private String childPhoto;

    @Column(name="child_gender", length = 1)
    @Setter
    @Getter
    private int childGender;

    @Setter
    @Getter
    @Column(name="user_id")
    private String userId;

   

    public HangeulbotChild() {}


}
