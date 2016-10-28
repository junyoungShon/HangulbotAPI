package com.hangulbot.controller;

import com.hangulbot.service.APIService;
import com.hangulbot.vo.HangeulbotChild;
import com.hangulbot.vo.HangeulbotDevice;
import com.hangulbot.vo.HangeulbotUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * Created by jyson on 2016. 7. 6..
 */

@RestController
public class APIController {

    // Define the logger object for this class
    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Qualifier("APIServiceImpl")
    @Autowired
    private APIService apiService;


    @RequestMapping(value="wordPointer",method = RequestMethod.GET)
    public void wordPointer(){
        apiService.wordPointer();
    }

    @RequestMapping(value="defaultDBSetting",method = RequestMethod.GET)
    public void defaultDBSetting(){
        log.debug("defaultSettingOn");
        apiService.defaultDBSetting();
    }

    @RequestMapping(value = "mainResources",method = RequestMethod.POST)
    public ResponseEntity<HashMap<String,Object>> mainResources(@RequestBody HangeulbotChild hangeulbotChild){
        System.out.println("Hangeulbot childId Info " + hangeulbotChild.getChildId());
        HashMap<String,Object> resultMap = apiService.getMainResources(hangeulbotChild.getChildId());
        return new ResponseEntity<HashMap<String,Object>>(resultMap, HttpStatus.OK);
    }

    @RequestMapping(value = "userLog",method = RequestMethod.POST)
    public ResponseEntity<HashMap<String,Object>> userLog(@RequestBody Object logObject){
        System.out.println("넘어오는 로그 자료 " + logObject);
        apiService.insertUserLog(logObject);
        HashMap<String,Object> resultMap = null;
        return new ResponseEntity<HashMap<String,Object>>(resultMap, HttpStatus.OK);
    }
    @RequestMapping(value="wordListForDefaultWordGame",method = RequestMethod.GET)
    public ResponseEntity<HashMap<String,Object>> wordListForDefaultWordGame(@RequestParam String childId, @RequestParam String contentId){
        HashMap<String,Object> resultMap = apiService.getWordListForDefaultWordGame(childId,contentId);
        return new ResponseEntity<HashMap<String, Object>>(resultMap, HttpStatus.OK);
    }
    @RequestMapping(value="childStats",method = RequestMethod.GET)
    public HashMap<String,Object> getChildStats(@RequestParam String childId,
                                                                @PageableDefault(sort = {"startDate"},direction = Sort.Direction.ASC,size = 5)Pageable pageable){
        HashMap<String,Object> resultMap = apiService.getChildStats(childId,pageable);
        log.error("컨트롤러"+resultMap.toString());
        return resultMap;
    }
    @RequestMapping(value="wordLogList",method = RequestMethod.GET)
    public HashMap<String,Object> getWordLogList(@RequestParam String childId, @RequestParam int page){
        Pageable pageable = new PageRequest(page,5,new Sort(Sort.Direction.ASC,"startDate"));
        HashMap<String,Object> resultMap = apiService.getWordLogList(childId,pageable);
        log.error("컨트롤러 wordLogList"+resultMap.toString());
        return resultMap;
    }
    @RequestMapping(value="contentLogList",method = RequestMethod.GET)
    public HashMap<String,Object> getContentLogList(@RequestParam String childId, @RequestParam int page){
        Pageable pageable = new PageRequest(page,5,new Sort(Sort.Direction.ASC,"startDate"));
        HashMap<String,Object> resultMap = apiService.getContentLogList(childId,pageable);
        log.error("컨트롤러 wordLogList"+resultMap.toString());
        return resultMap;
    }
    @RequestMapping(value = "loginByUserId",method = RequestMethod.POST)
    public ResponseEntity<HashMap<String,Object>> loginByUserId(@RequestBody HangeulbotDevice hangeulbotDevice){
        System.out.println("Hangeulbot userId Info " + hangeulbotDevice.getUserId());
        HashMap<String,Object> resultMap = apiService.loginByUserId(hangeulbotDevice.getUserId());
        return new ResponseEntity<HashMap<String,Object>>(resultMap, HttpStatus.OK);
    }
    @RequestMapping(value = "isHangeulbotDevice/{hangeulbotDeviceAddress}",method = RequestMethod.GET)
    public ResponseEntity<HashMap<String,Object>> isHangeulbotDevice(@PathVariable String hangeulbotDeviceAddress){
        log.debug("한글봇 디바이스인지 검증 요청!");
        HashMap<String,Object> resultMap = apiService.isHangeulbotDevice(hangeulbotDeviceAddress);
        return new ResponseEntity<HashMap<String,Object>>(resultMap, HttpStatus.OK);
    }
    @RequestMapping(value = "deviceInfoAndUserInfo",method = RequestMethod.PUT)
    public ResponseEntity putDeviceInfoAndUserInfo(@RequestBody HangeulbotUser hangeulbotUser){
        System.out.println(hangeulbotUser);
        HashMap<String,Object> resultMap = apiService.putHangeulbotDeviceInfoAndHangeulbotUserInfo(hangeulbotUser);
        return new ResponseEntity<HashMap<String,Object>>(resultMap, HttpStatus.OK);
    }

    @RequestMapping(value = "childImage",method = RequestMethod.POST)
    public boolean uploadChildPhoto(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        boolean flag= true;
        String fileName = null;
        Enumeration<String> map =  request.getHeaderNames();
        while(map.hasMoreElements()){
            System.out.println(map.nextElement());
        }
        if (!file.isEmpty()) {
            try {
                //Files.copy(file.getInputStream(), Paths.get("/Users/jyson/IdeaProjects/HangeulbotAPI/src/main/resources/static/img/", fileName));
                System.out.println("file start");
                System.out.println("name :"  + file.getOriginalFilename());
                System.out.println("file Name : "+fileName);
                System.out.println("file size : " + file.getSize());
                String savedProfileUrl = apiService.copyProfilePhoto(file);
                System.out.println(savedProfileUrl);
            } catch (RuntimeException e) {
                flag=false;
                e.printStackTrace();
                System.out.println("여기오냐 에러");
           } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            flag=false;
            System.out.println("여기오냐 그냥 빈파일");
        }
        return flag;
    }

    @RequestMapping(value = "childInfo",method = RequestMethod.POST)
    public HangeulbotChild insertChildInfo(@RequestBody HangeulbotChild hangeulbotChild){
        System.out.println(hangeulbotChild.toString());
        try {
            apiService.insertChildInfo(hangeulbotChild);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hangeulbotChild;
    }

    @RequestMapping(value="isDuplicated/",method = RequestMethod.POST)
    public boolean isDuplicatedByEmailId(@RequestBody String userId){
        System.out.println("현재 API Server 로 넘어오는 이메일 값"+ userId);
        boolean flag=false;
        if(apiService.isDuplicatedByEmailId(userId)){
            System.out.println("중복되는 아이디가 있네");
            flag=true;
        }
        return flag;
    }

}


