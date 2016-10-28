package com.hangulbot.vo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Created by jyson on 2016. 7. 6..
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,property = "@idx")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonSerialize
public class HangeulbotUser {


    @Column(name="idx", length = 11)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter
    @Getter
    private int idx;

    @Id
    @Column(name="user_id",length = 30)
    @Setter
    @Getter
    private String userId;

    @Column(name="password",nullable = false)
    @Setter
    @Getter
    private String password;


    @Column(name="phone_number",length = 20,nullable = false)
    @Setter
    @Getter
    private String phoneNumber;

    @Setter
    @Getter
    @Column(name = "device_id")
    private String deviceId;

    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @Setter
    @Getter
    @JoinColumn(name="child_id")
    private List<HangeulbotChild> hangeulbotChildren;

   /* @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @Setter
    @Getter
    @JoinColumn(name="content_Log_Idx")
    private List<HangeulbotUserContentLog> hangeulbotUserContentLogList;

    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @Setter
    @Getter
    @JoinColumn(name="user_word_log_idx")
    private List<HangeulbotUserWordLog> hangeulbotUserWordLogList;*/

    public HangeulbotUser() {}



}
