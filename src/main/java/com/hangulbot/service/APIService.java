package com.hangulbot.service;

import com.hangulbot.vo.HangeulbotChild;
import com.hangulbot.vo.HangeulbotUser;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.HashMap;

/**
 * Created by jyson on 2016. 7. 6..
 */
@Service
public interface APIService {
    public void defaultDBSetting();

    boolean isDuplicatedByEmailId(String userId);

    HashMap<String, Object> putHangeulbotDeviceInfoAndHangeulbotUserInfo(HangeulbotUser hangeulbotUser);

    HashMap<String,Object> loginByUserId(String userId);

    String copyProfilePhoto(MultipartFile file) throws Exception;

    void insertChildInfo(HangeulbotChild hangeulbotChild) throws ParseException;

    HashMap<String,Object> isHangeulbotDevice(String hangeulbotDeviceAddress);

    HashMap<String,Object> getMainResources(String childId);

    HashMap<String,Object> getWordListForDefaultWordGame(String childId, String contentId);

    void insertUserLog(Object logObject);

    HashMap<String,Object> getChildStats(String childId, Pageable pageable);

    HashMap<String,Object> getWordLogList(String childId, Pageable pageable);

    HashMap<String,Object> getContentLogList(String childId, Pageable pageable);

    void wordPointer();
}
