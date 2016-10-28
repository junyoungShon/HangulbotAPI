package com.hangulbot.vo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by jyson on 2016. 10. 13..
 */
@Entity
@ToString(callSuper=true, includeFieldNames=true)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,property = "@idx")
public class HangeulbotPhonics implements Serializable{
    @Column(name="letter")
    @Setter
    @Getter
    private char letter;

    @Column(name="type",length = 10)
    @Setter
    @Getter
    private String type;

    @Id
    @GeneratedValue
    @Column(name="idx")
    private int idx;

    @Setter
    @Getter
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "child_id", referencedColumnName = "child_id")
    private HangeulbotChild hangeulbotChild;

    @Column(name="test_count")
    @Setter
    @Getter
    private int testCount;

    @Column(name="right_count")
    @Setter
    @Getter
    private int rightCount;
}
