package com.hangulbot.vo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;


/**
 * Created by jyson on 2016. 7. 6..
 */
@Entity
@ToString(callSuper=true, includeFieldNames=true)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,property = "@idx")
public class HangeulbotDevice {


    @Column(name="device_idx", length=7)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter
    @Getter
    private int deviceIdx;

    @Id
    @Column(name="device_id", length = 100)
    @Setter
    @Getter
    private String deviceId = "hangeulbot_"+deviceIdx;

    @Column(name="device_address", length=30)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter
    @Getter
    private String deviceAddress;

    @Column(name="device_name", length = 30)
    @Setter
    @Getter
    private String deviceName;


    @Setter
    @Getter
    @Column(name="user_id",length = 30)
    private String userId = "notRegistered";

    public HangeulbotDevice() {}



}
