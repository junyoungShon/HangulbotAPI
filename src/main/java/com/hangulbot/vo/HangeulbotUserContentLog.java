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
public class HangeulbotUserContentLog {

    @Column(name = "content_Log_Idx")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter
    @Getter
    @Id
    private int contentLogIdx;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @Setter
    @Getter
    @JoinColumn(name = "child_id")
    private HangeulbotChild hangeulbotChild;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @Setter
    @Getter
    @JoinColumn(name = "content_id")
    private HangeulbotContent hangeulbotContent;

    @Setter
    @Getter
    @Column(name = "start_date")
    private Date startDate;

    @Setter
    @Getter
    @Column(name = "end_date")
    private Date endDate;

    @Setter
    @Getter
    @Column(name = "point")
    private int point;

    @Getter
    @Setter
    @Column(name="time_required")
    private int timeRequired;

    @Getter
    @Setter
    @Column(name="finished")
    private boolean finished;
}
