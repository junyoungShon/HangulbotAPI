package com.hangulbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hangulbot.common.HangeulbotConsonants;
import com.hangulbot.dao.APIDao;
import com.hangulbot.utils.FileUtil;
import com.hangulbot.utils.HangeulDisassembler;
import com.hangulbot.vo.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hangulbot.common.HangeulbotConsonants.apiUrl;

/**
 * Created by junyoung on 2016-07-07.
 */
@Service

public class APIServiceImpl implements APIService {
    // Define the logger object for this class
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private HangeulDisassembler hangeulDisassembler;

    @Autowired
    private APIDao apiDao;
    @Autowired
    ObjectMapper mapper;

    @Override
    public void defaultDBSetting() {
        /*HangeulbotDevice hangeulbotDevice = new HangeulbotDevice();
        hangeulbotDevice.setDeviceId("22:22:22:22");
        hangeulbotDevice.setDeviceName("ec-2");

        HangeulbotUser hangeulbotUser = new HangeulbotUser();
        hangeulbotUser.setUserId("imvestt@hanmail.net");
        hangeulbotUser.setPassword("12341234");
        hangeulbotUser.setPhoneNumber("01026789441");
        hangeulbotUser.setDeviceId(hangeulbotDevice.getDeviceId());

        HangeulbotChild hangeulbotChild = new HangeulbotChild();
        hangeulbotChild.setUserId("imvestt@hanmail.net");
        Calendar cal = Calendar.getInstance();
        cal.set(2008, 4, 30, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        hangeulbotChild.setChildBirth(cal.getTime());
        hangeulbotChild.setChildName("손똘기");
        hangeulbotChild.setChildNum(0);*/
        //hangeulbotChild.setChildPhoto("http://192.168.0.146:8888/static/childPhoto/imvestt@hanmail.net_1.jpg");
        //hangeulbotDeviceRepository.save(hangeulbotDevice);
        //hangeulbotUserRepository.save(hangeulbotUser);
        //hangeulbotChildRepository.save(hangeulbotChild);
    }


    @Override
    public boolean isDuplicatedByEmailId(String userId) {
        boolean flag = false;
        if (apiDao.isDuplicatedByEmailId(userId) != null) {
            flag = true;
        }
        return flag;
    }


    @Override
    public HashMap<String, Object> putHangeulbotDeviceInfoAndHangeulbotUserInfo(HangeulbotUser hangeulbotUser) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String encodedPassword = bCryptPasswordEncoder.encode(hangeulbotUser.getPassword());
        hangeulbotUser.setPassword(encodedPassword);
        hangeulbotUser = apiDao.putHangeulbotDeviceInfoAndHangeulbotUserInfo(hangeulbotUser);
        hangeulbotUser.setPassword("protectedSafely");
        resultMap.put("hangeulbotUser", hangeulbotUser);
        resultMap.put("childRegistered", false);
        resultMap.put("token", Jwts.builder().setSubject(hangeulbotUser.getUserId()).claim("roles", "parent").
                setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, "secretkey").compact());
        return resultMap;
    }

    @Override
    public HashMap<String, Object> loginByUserId(String userId) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        log.debug(userId);
        //기기 있음
        HangeulbotUser hangeulbotUser = apiDao.loginByUserId(userId);
        List<HangeulbotChild> hangeulbotChildren = apiDao.getHangeulbotChildByUserId(userId);
        //아이 있음
        if (hangeulbotUser.getHangeulbotChildren() != null&&hangeulbotChildren.size()!=0) {

            resultMap.put("childRegistered", true);
            resultMap.put("token", Jwts.builder().setSubject(hangeulbotUser.getUserId()).claim("roles", "parent").
                    setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, "secretkey").compact());

            //todo 아이 프로필 사진 경로 맞춰주자!! (Refac)
            for(int i=0;i<hangeulbotChildren.size();i++){
                hangeulbotChildren.get(i).setChildPhoto(apiUrl+hangeulbotChildren.get(i).getChildPhoto());
            }
            hangeulbotUser.setHangeulbotChildren(hangeulbotChildren);

        //아이 없음
        } else {
            resultMap.put("childRegistered", false);
            resultMap.put("token", Jwts.builder().setSubject(hangeulbotUser.getUserId()).claim("roles", "parent").
                    setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, "secretkey").compact());
        }
        hangeulbotUser.setPassword("protectedSafely");
        resultMap.put("hangeulbotUser", hangeulbotUser);
        return resultMap;
    }

    @Override
    public String copyProfilePhoto(MultipartFile file) throws Exception {
        return fileUtil.parseInsertFileInfo(file);
    }

    @Override
    public void insertChildInfo(HangeulbotChild hangeulbotChild) throws ParseException {
        //날짜 형식 변경하여 생일 입력
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        Date childBirth = format.parse(format.format(hangeulbotChild.getChildBirth()));
        hangeulbotChild.setChildBirth(childBirth);

        //아기 아이디 입력
        String childId = hangeulbotChild.getUserId()+"_"+hangeulbotChild.getChildNum();
        hangeulbotChild.setChildId(childId);

        //아기 레벨 설정
        hangeulbotChild.setChildExp(0);


        //아이 프로필사진 입력
        if(hangeulbotChild.getChildPhoto()==""){
            System.out.println("초ㅏ일드 포토 널이다");
            hangeulbotChild.setChildPhoto("/static/defaultImage/defaultProfile.jpg");
            System.out.println(hangeulbotChild.getChildPhoto());
        }else{
            String fileName = hangeulbotChild.getChildPhoto();
            String newFileName = fileName.substring(fileName.lastIndexOf("/")+1,fileName.length());
            System.out.println(newFileName);
            hangeulbotChild.setChildPhoto("/static/childPhoto/"+newFileName);
        }
        System.out.println(hangeulbotChild.toString());
        apiDao.insertChildInfo(hangeulbotChild);

    }

    @Override
    public HashMap<String, Object> isHangeulbotDevice(String hangeulbotDeviceAddress) {
        HashMap<String,Object> resultMap = new HashMap<String,Object>();
        HangeulbotDevice hangeulbotDevice = apiDao.isHangeulbotDevice(hangeulbotDeviceAddress);

        //한글봇 기기가 아니면
        if(hangeulbotDevice==null){
            resultMap.put("isHangeulbotDevice",false);
        //한글봇 기기가 맞으면
        }else{
            resultMap.put("isHangeulbotDevice",true);
            resultMap.put("hangeulbotDevice",hangeulbotDevice);
            if(hangeulbotDevice.getUserId().equals("notRegistered")){
                resultMap.put("isNotRegistered",true);
            }else{
                resultMap.put("isNotRegistered",false);
            }
        }
        return resultMap;
    }

    @Override
    public HashMap<String, Object> getMainResources(String childId) {
        //콘텐츠 리스트 담아오자
        HashMap<String,Object> resultMap = new HashMap<String,Object>();
        List<HangeulbotContent> contentList= apiDao.getContentList();
        //todo 콘텐츠 사진 경로 맞춰주자!! (Refac)
        for(int i=0;i<contentList.size();i++){
            contentList.get(i).setContentPopup(apiUrl+contentList.get(i).getContentPopup());
            contentList.get(i).setContentThumb(apiUrl+contentList.get(i).getContentThumb());
        }
        resultMap.put("contentList",contentList);
        return resultMap;
    }

    @Override
    public HashMap<String, Object> getWordListForDefaultWordGame(String childId, String contentId) {
        //1.hangeulbot_user_word_log를 수색하여 사용자의 로그 목록을 가져온다. (한단어를 여러번 학습했으면 가장 최신 로그만 가져옴)
        List<HangeulbotUserWordLog> logList = apiDao.getWordLogListByChildId(childId);

        ArrayList<HangeulbotWord> wordList = new ArrayList<HangeulbotWord>();
        Boolean [] guideShown = new Boolean [10];
        Boolean [] isTest = new Boolean [10];
        //2.기존의 단어로그에서 오답이 있을 경우 이를 먼저 집어넣는다. (2~3개)
        for(int i=0;i<logList.size();i++){
            //오답일 경우 단어를 집어넣는다.
            if(!logList.get(i).isCorrect()){
                //오답인 경우 무조건 가이드 넣어줌
                guideShown[wordList.size()] = true;
                isTest[wordList.size()] = false;
                wordList.add(logList.get(i).getHangeulbotWord());
            }else{
                //정답이고 가이드가 보였을 때(가이드 빼준다.)
                if(logList.get(i).isGuideShown()){
                    guideShown[wordList.size()] = false;
                    isTest[wordList.size()] = false;
                    wordList.add(logList.get(i).getHangeulbotWord());
                }
            }
            //10개가 채워지면 브레이크
            if(wordList.size()>9){
                break;
            }
        }
        //아직 단어가 10개 이하일 때 부족한 숫자 만큼
        if(wordList.size()<10){
            //아래의 쿼리에서는 이미 뽑힌 단어는 제거하기 위해 wordList를 넘김
            List<HangeulbotWord> newWordList = apiDao.getNewWordByRandom(10-wordList.size(),wordList);
            for(int i=0;i<newWordList.size();i++){
                Random r = new Random();
                //10%는 가이드를 안보여주는 난이도 측정형 테스트 문제 , 90%는 가이드를 보여주는 학습형 문제
                if(r.nextInt(11)>9){
                    guideShown[wordList.size()] = false;
                    isTest[wordList.size()] = true;
                }else{
                    guideShown[wordList.size()] = true;
                    isTest[wordList.size()] = false;
                }
                wordList.add(newWordList.get(i));
            }
        }

        //3.정답이 있을 경우 guide_shown 을 특정하여 집어 넣는다(2~3개)

        //4. 기록이 모자라거나 없을 경우 테스트를 위한 단어를 가이드 없이 삽입한다.

        HashMap<String,Object> resultMap = new HashMap<String,Object>();
        for(int i=0;i<wordList.size();i++){
            System.out.println("단어가 뭐뭐 뽑혔나 : "+i+" "+wordList.get(i).getWordKorean()+" 가이드 보이냐?"+guideShown[i]+" 이거 테스트냐"+" "+isTest[i]);
        }
        resultMap.put("wordList",wordList);

        resultMap.put("isTest",isTest);
        resultMap.put("guideShown",guideShown);
        return resultMap;
    }

    @Override
    public void insertUserLog(Object logObject) {
        LinkedHashMap logObjectMap = (LinkedHashMap) logObject;

        log.error("로그 타입"+logObjectMap.get("logType").equals("userWordLog"));


        HangeulbotChild hangeulbotchild  =apiDao.getChildByChildId(logObjectMap.get("childId").toString());
        HangeulbotContent hangeulbotContent =apiDao.getContentByContentId(logObjectMap.get("contentId").toString());

        if(logObjectMap.get("logType").equals("userWordLog")){
            HangeulbotUserWordLog hangeulbotUserWordLog = mapper.convertValue(logObjectMap,HangeulbotUserWordLog.class);

            HangeulbotWord hangeulbotWord = apiDao.getWordByWordId(Integer.parseInt(logObjectMap.get("wordId").toString()));

            //이렇게 디비에서 빼와서 넣는게 맞냐
            hangeulbotUserWordLog.setHangeulbotWord(hangeulbotWord);
            hangeulbotUserWordLog.setHangeulbotContent(hangeulbotContent);
            hangeulbotUserWordLog.setHangeulbotChild(hangeulbotchild);
            if((Boolean)logObjectMap.get("isCorrect")){
                hangeulbotUserWordLog.setIsCorrect(true);
            }else{
                hangeulbotUserWordLog.setIsCorrect(false);
            }
            log.error("워드 로그 "+hangeulbotUserWordLog);
            apiDao.insertUserWordLog(hangeulbotUserWordLog);
            //유저의 단어 학습로그 삽입완료

            //파닉스 및 단어 성취도 기록하기
            //1. 파닉스 및 단어 성취도 기록이 이미 있는지를 찾아온다.
            insertPhoenixInfo(hangeulbotchild,hangeulbotWord,hangeulbotUserWordLog);
            insertWordAchievementInfo(hangeulbotchild,hangeulbotWord,hangeulbotUserWordLog);

            //// TODO: 2016. 9. 30. semantic grade 측정
            if((Boolean)logObjectMap.get("isTest")){

                HangeulbotWordSemanticGrade hangeulbotWordSemanticGrade = apiDao.getHangeulbotWordSemanticGradeByWord(hangeulbotWord);

                Date nowDate = new Date();
                int monthAge = (int) ((nowDate.getTime() - hangeulbotchild.getChildBirth().getTime()/(1000*60*60*24)/30));
                log.error("아이의 월령"+monthAge);

                //이미 기록이 있는 경우
                if(hangeulbotWordSemanticGrade!=null){
                    float avgAge = hangeulbotWordSemanticGrade.getAvgAge();
                    int testCount = hangeulbotWordSemanticGrade.getTestCount()+1;

                    hangeulbotWordSemanticGrade.setTestCount(testCount);
                    hangeulbotWordSemanticGrade.setAvgAge(((avgAge*(testCount-1)+monthAge)/testCount));
                    log.error("시맨틱 그레이드 수정"+hangeulbotWordSemanticGrade);

                //아직 기록이 없는 경우
                }else{
                    hangeulbotWordSemanticGrade = new HangeulbotWordSemanticGrade();
                    hangeulbotWordSemanticGrade.setHangeulbotWord(hangeulbotWord);
                    hangeulbotWordSemanticGrade.setAvgAge(monthAge);
                    hangeulbotWordSemanticGrade.setTestCount(1);
                    log.error("시맨틱 그레이드 최초 기록"+hangeulbotWordSemanticGrade);

                }
                apiDao.saveWordSemanticGrade(hangeulbotWordSemanticGrade);

            }

        }else if(logObjectMap.get("logType").equals("userContentLog")){
            HangeulbotUserContentLog hangeulbotUserContentLog = mapper.convertValue(logObjectMap,HangeulbotUserContentLog.class);
            hangeulbotUserContentLog.setHangeulbotChild(hangeulbotchild);
            hangeulbotUserContentLog.setHangeulbotContent(hangeulbotContent);
            //콘텐츠 종료 로그
            try {
                if((Boolean)logObjectMap.get("isFinished")){
                    log.error("콘텐츠 종료 로그 삽입"+hangeulbotUserContentLog);
                    apiDao.saveUserContentLog(hangeulbotUserContentLog);
                    //콘텐츠 시작 로그
                }else{
                    log.error("콘텐츠 시작 로그 삽입"+hangeulbotUserContentLog);
                    apiDao.saveUserContentLog(hangeulbotUserContentLog);
                }
            }catch (Exception e){

            }finally {
                hangeulbotUserContentLog.setFinished(false);
                apiDao.saveUserContentLog(hangeulbotUserContentLog);
            }

        }
    }

    private void insertWordAchievementInfo
            (HangeulbotChild hangeulbotchild, HangeulbotWord hangeulbotWord, HangeulbotUserWordLog hangeulbotUserWordLog)
    {
        HangeulbotWordAchievement hangeulbotWordAchievement =
            apiDao.getWordAchievementInfoByChildIdAndWordId(hangeulbotchild,hangeulbotWord);
            if(hangeulbotWordAchievement!=null){
                log.error("처음엔 널이니까 여기 안오는게 맞다.",hangeulbotWordAchievement);
            }
            if(hangeulbotWordAchievement!=null){
                log.error("기록이있기때문에 여기에온다.");
                int testCount = hangeulbotWordAchievement.getTestCount();
                int correctCount = hangeulbotWordAchievement.getRightCount();
                if(hangeulbotUserWordLog.isCorrect()){
                    hangeulbotWordAchievement.setRightCount(correctCount+1);
                    hangeulbotWordAchievement.setTestCount(testCount+1);
                    if(hangeulbotUserWordLog.isGuideShown()){
                        hangeulbotWordAchievement.setCorrectWithoutGuide(true);
                    }else{
                        hangeulbotWordAchievement.setCorrectWithoutGuide(false);
                    }
                }else{
                    hangeulbotWordAchievement.setTestCount(testCount+1);
                    hangeulbotWordAchievement.setCorrectWithoutGuide(false);
                }

            }else{
                hangeulbotWordAchievement = new HangeulbotWordAchievement();
                hangeulbotWordAchievement.setHangeulbotChild(hangeulbotchild);
                hangeulbotWordAchievement.setHangeulbotWord(hangeulbotWord);
                log.error("기록이없기때문에 여기에온다.");
                hangeulbotWordAchievement.setTestCount(1);
                if(hangeulbotUserWordLog.isCorrect()){
                    hangeulbotWordAchievement.setRightCount(1);
                    if(hangeulbotUserWordLog.isGuideShown()){
                        hangeulbotWordAchievement.setCorrectWithoutGuide(true);
                    }else{
                        hangeulbotWordAchievement.setCorrectWithoutGuide(false);
                    }
                }else{
                    hangeulbotWordAchievement.setRightCount(0);
                    hangeulbotWordAchievement.setCorrectWithoutGuide(false);
                    hangeulbotWordAchievement.setCorrectWithoutGuide(false);
                }
            }
        apiDao.saveWordAchievement(hangeulbotWordAchievement);
    }

    private void insertPhoenixInfo
            (HangeulbotChild hangeulbotchild, HangeulbotWord hangeulbotWord, HangeulbotUserWordLog hangeulbotUserWordLog) {
        //정답과 입력한 오답의 길이가 같을때만 파닉스에 파악해줌
        if(hangeulbotWord.getWordKorean().length()==hangeulbotUserWordLog.getInsertedAnswer().length()){

            //정답과 오답을 분해한다
            List<Map<String, Character>> answerList =
                    hangeulDisassembler.hangeuldisassemble(hangeulbotWord.getWordKorean());
            List<Map<String, Character>> insertedList =
                    hangeulDisassembler.hangeuldisassemble(hangeulbotUserWordLog.getInsertedAnswer());

            //정답의 초중종성을 기준으로 파닉스 기록이 있는지 가져온다.
            for(int i=0;i<answerList.size();i++){

                char cho = answerList.get(i).get("cho");
                char jun = answerList.get(i).get("jun");
                char jon = answerList.get(i).get("jon");
                char insertedCho = insertedList.get(i).get("cho");
                char insertedJun = insertedList.get(i).get("jun");
                char insertedJon = insertedList.get(i).get("jon");
                log.error("초성이 "+cho+"일때"+apiDao.getPhonicsByTypeAndHangeulbotChildAndLetter(cho,hangeulbotchild,"cho"));
                //파닉스 기록을 가져와서 기록
                insertedPhonics
                        (hangeulbotchild,cho,insertedCho,"cho",
                                apiDao.getPhonicsByTypeAndHangeulbotChildAndLetter(cho,hangeulbotchild,"cho"));
                insertedPhonics
                        (hangeulbotchild,jun,insertedJun,"jun",
                                apiDao.getPhonicsByTypeAndHangeulbotChildAndLetter(jun,hangeulbotchild,"jun"));
                if(jon!='a'){
                    insertedPhonics
                            (hangeulbotchild,jon,insertedJon,"jon",
                                    apiDao.getPhonicsByTypeAndHangeulbotChildAndLetter(jon,hangeulbotchild,"jon"));
                }
            }
        }

    }

    public void insertedPhonics(HangeulbotChild hangeulbotChild,
                                char letter ,char insertedText,String type ,HangeulbotPhonics hangeulbotPhonics){
        if(hangeulbotPhonics!=null){
            hangeulbotPhonics.setTestCount(hangeulbotPhonics.getRightCount()+1);
            if(letter==insertedText){
                hangeulbotPhonics.setRightCount(hangeulbotPhonics.getRightCount()+1);
            }
        }else{
            hangeulbotPhonics = new HangeulbotPhonics();
            hangeulbotPhonics.setLetter(letter);
            hangeulbotPhonics.setHangeulbotChild(hangeulbotChild);
            hangeulbotPhonics.setTestCount(1);
            hangeulbotPhonics.setType(type);
            if(letter==insertedText){
                hangeulbotPhonics.setRightCount(1);
            }else{
                hangeulbotPhonics.setRightCount(0);
            }
        }
        apiDao.savePhonics(hangeulbotPhonics);
    }

    @Override
    public HashMap<String, Object> getChildStats(String childId,Pageable pageable) {
        HashMap<String,Object> resultMap = new HashMap<String,Object>();
        HangeulbotChild hangeulbotChild= apiDao.getChildByChildId(childId);

        //통계 시점 생성
        Date nowDate = new Date();
        //월령 추출
        Long nowTime = nowDate.getTime();
        Long childTime = hangeulbotChild.getChildBirth().getTime();
        int childMonthAge = (int) ((nowTime - childTime)/30);

        //1. 콘텐츠로그와 단어 학습 로그를 검색해서 가져오자!
        List<HangeulbotUserWordLog> hangeulbotUserWordLog = apiDao.getChildWordLog(hangeulbotChild,pageable);
        List<HangeulbotUserContentLog> hangeulbotUserContentLog = apiDao.getChildContentLog(hangeulbotChild,pageable);
        log.error(hangeulbotUserWordLog.toString());
        log.error(hangeulbotUserContentLog.toString());


        resultMap.put("wordLog",hangeulbotUserWordLog);
        resultMap.put("contentLog",hangeulbotUserContentLog);
        /*//모든 로그 추출 (중복단어 제거 안함) - 단어 (파닉스용)
        List<HangeulbotUserWordLog> totalWordLogList = apiDao.getChildWordLog(hangeulbotChild);
        //동일 월령의 아이들의 단어 학습 로그를 모두 추출

        //모든 로그 추출  콘텐츠
        List<HangeulbotUserContentLog> totalContentLog = apiDao.getChildContentLog(hangeulbotChild);*/

        //중복 단어 제거 한 로그 추출
        List<HangeulbotUserWordLog> wordLogListNotDuplicated = apiDao.getWordLogListByChildId(childId);
        //학업 성취도 평가 (1. DB내 단어의 난이도 총합 - 분모 / 중복 단어를 제거한 최신 로그의 정답 난이도의 총합 - 분자 )
        //동일 월령의 아이들의

        //총 학습 단어 수 ( 중복 단어 제거 )
        resultMap.put("totalWordNumber",wordLogListNotDuplicated.size());

        // 주간 정답율 정답율 ( 최근 5주 표시해줌 )
        HashMap <Integer,Float> answerRateByWeeks = new HashMap<>();
        // 주간 정답율 ( 같은 월령의 다른 아이들 종합 최근 5주 )
        HashMap <Integer,Float> anotherChildAnswerRateByWeeks = new HashMap<>();
        //아이의 난이도에 따른 정답율 (1단계 ~ 5단계)
        HashMap <Integer,Float> answerRateByWordGrade = new HashMap<>();
        //또래아이의 난이도에 따른 정답율 (1단계 ~ 5단계)
        HashMap <Integer,Float> anotherAnswerRateByWordGrade = new HashMap<>();
        //한글봇 종합 학원 성취도 ( 주간에 출제된 문제들의 평균 난이도로 학업 성취도를 측정 0~100)
        HashMap <Integer,Float> studyAchievement = new HashMap<>();
        HashMap <Integer,Float> anotherStudyAchievement = new HashMap<>();

        //주에 따른 정답율,,종합학습난이도. ( 0주전~4주전 /)
        for(int i=0;i<5;i++){
            answerRateByWeeks.put(i,apiDao.getWeekAnswerRateByChildId(i,childId));
            anotherChildAnswerRateByWeeks.put(i,apiDao.getAnotherChildAnswerRateByChildId(i,childId));

            studyAchievement.put(i,apiDao.getStudyAchivement(i,childId));
            anotherStudyAchievement.put(i,apiDao.getAnotherStudyAchievement(i,childId));
        }
        //난이도별 정답율 (1단계~5단계)
        for(int i=1;i<=5;i++){
            answerRateByWordGrade.put(i,apiDao.getAnswerRateByChildIdAndGrade(i,childId));
            anotherAnswerRateByWordGrade.put(i,apiDao.getAnotherChildAnswerRateByChildIdAndWordGrade(i,childId));
        }
        //한글봇 종합 학업 성취도 (

        //파닉스 숙련도 ( 초, 중, 종성 )
        ArrayList<HashMap<String,String>> phoenixList = getPhonicsByChild(hangeulbotChild);


        //단어 숙련도 표시 todo : 기록없음 로그
        List<HashMap<String,String>> wordAchievementMapList = new ArrayList<>();
        List<HangeulbotWord> allWordList = apiDao.getAllWordOrderByBlockMixGrade();


        for(int i=0;i<allWordList.size();i++){
            HashMap<String,String> wordAchievementMap= new HashMap<>();
            HangeulbotWordAchievement wordAchievement =
                    apiDao.getWordAchievementInfoByHangeulbotChildAndWord(hangeulbotChild,allWordList.get(i));

            HangeulbotWordAchievement tempAchievement =
                    apiDao.getWordAchievementInfoByHangeulbotChildAndWord(hangeulbotChild,allWordList.get(i));
            String tempWord = allWordList.get(i).getWordKorean();

            wordAchievementMap.put("word",tempWord);

            if(tempAchievement!=null){
                float tempWordAnswerRate =
                        ((float)tempAchievement.getRightCount() / (float)tempAchievement.getTestCount())*100;
                if(tempAchievement.getTestCount()>10){
                    if(tempWordAnswerRate>=70.0){

                        wordAchievementMap.put("grade","a");
                    }else if(tempWordAnswerRate<70.0&&tempWordAnswerRate>=50.0){
                        wordAchievementMap.put("grade","b");
                    }else{
                        wordAchievementMap.put("grade","c");
                    }
                }else{
                    if(tempWordAnswerRate>=80.0){
                        wordAchievementMap.put("grade","a");
                    }else if(tempWordAnswerRate<80.0&&tempWordAnswerRate>=60.0){
                        wordAchievementMap.put("grade","b");
                    }else{
                        wordAchievementMap.put("grade","c");
                    }
                }
            }else{
                wordAchievementMap.put("grade","d");
            }
            wordAchievementMapList.add(wordAchievementMap);
        }
        //종합 학습 성취도
        resultMap.put("studyAchievement",studyAchievement);
        resultMap.put("wordAchievementMapList",wordAchievementMapList);
        //파닉스 정답율
        resultMap.put("phoenixList",phoenixList);

        //난이도에 따른 정답율
        resultMap.put("answerRateByWordGrade",answerRateByWordGrade);
        resultMap.put("anotherAnswerRateByWordGrade",anotherAnswerRateByWordGrade);

        //주간 정답율 추이 (내 아이와 또래아이)
        resultMap.put("answerRateByWeeks",answerRateByWeeks);
        resultMap.put("anotherChildAnswerRateByWeeks",anotherChildAnswerRateByWeeks);
        //일주일간 정답율
        Float answerRateWeek = answerRateByWeeks.get(0);
        resultMap.put("answerRateWeek",answerRateWeek);

        //누적 학습시간 (단위 분)
        int totalStudytime = apiDao.getTotalStudyTime(hangeulbotChild);
        //피니쉬한 콘텐츠완료한 횟수
        int finishedContentsNumber = apiDao.getFinishedContentsNumber(hangeulbotChild);

        resultMap.put("totalStudytime",Math.round(totalStudytime/60));
        resultMap.put("finishedContentsNumber",finishedContentsNumber);

        //난이도별 정답율
        return resultMap;
    }

    private ArrayList<HashMap<String,String>> getPhonicsByChild(HangeulbotChild hangeulbotChild) {
        ArrayList<HashMap<String,String>> phonicsListForstats = new ArrayList<>();
        for(int i=0;i<HangeulbotConsonants.CHO.length;i++){
            HashMap<String,String> tempHashMap = new HashMap<>();
            HangeulbotPhonics hangeulbotPhonics =
                    apiDao.getPhonicsByTypeAndHangeulbotChildAndLetter
                            (HangeulbotConsonants.CHO[i],hangeulbotChild,"cho");
            tempHashMap.put("type","cho");
            tempHashMap.put("letter",HangeulbotConsonants.CHO[i]+"");
            phonicsListForstats.add(makeMapForPhonicsStats(tempHashMap,hangeulbotPhonics));
        }
        for(int i=0;i<HangeulbotConsonants.JUN.length;i++){
            HashMap<String,String> tempHashMap = new HashMap<>();
            HangeulbotPhonics hangeulbotPhonics =
                    apiDao.getPhonicsByTypeAndHangeulbotChildAndLetter
                            (HangeulbotConsonants.JUN[i],hangeulbotChild,"jun");
            tempHashMap.put("type","jung");
            tempHashMap.put("letter",HangeulbotConsonants.JUN[i]+"");
            phonicsListForstats.add(makeMapForPhonicsStats(tempHashMap,hangeulbotPhonics));
        }
        for(int i=0;i<HangeulbotConsonants.JON.length;i++){
            HashMap<String,String> tempHashMap = new HashMap<>();
            HangeulbotPhonics hangeulbotPhonics =
                    apiDao.getPhonicsByTypeAndHangeulbotChildAndLetter
                            (HangeulbotConsonants.JON[i],hangeulbotChild,"jon");
            tempHashMap.put("type","jong");
            tempHashMap.put("letter",HangeulbotConsonants.JON[i]+"");
            phonicsListForstats.add(makeMapForPhonicsStats(tempHashMap,hangeulbotPhonics));
        }
        return phonicsListForstats;
    }
    private HashMap<String,String> makeMapForPhonicsStats(HashMap<String,String> tempHashMap,HangeulbotPhonics hangeulbotPhonics){
        if(hangeulbotPhonics!=null){
            float answerRate = (hangeulbotPhonics.getRightCount()/hangeulbotPhonics.getTestCount())*100;
            if(hangeulbotPhonics.getTestCount()>10){
                if(answerRate>=70.0){
                    tempHashMap.put("rate","a");
                }else if(answerRate<70.0&&answerRate>=50.0){
                    tempHashMap.put("rate","b");
                }else{
                    tempHashMap.put("rate","c");
                }
            }else{
                if(answerRate>=80.0){
                    tempHashMap.put("rate","a");
                }else if(answerRate<80.0&&answerRate>=60.0){
                    tempHashMap.put("rate","b");
                }else{
                    tempHashMap.put("rate","c");
                }
            }
        }else{
            tempHashMap.put("rate","d");
        }
        return tempHashMap;
    }


    @Override
    public HashMap<String, Object> getWordLogList(String childId, Pageable pageable) {
        HashMap<String,Object> resultMap = new HashMap<String,Object>();
        HangeulbotChild hangeulbotChild= apiDao.getChildByChildId(childId);
        resultMap.put("wordLogList",apiDao.getChildWordLog(hangeulbotChild,pageable));
        return resultMap;
    }

    @Override
    public HashMap<String, Object> getContentLogList(String childId, Pageable pageable) {
        HashMap<String,Object> resultMap = new HashMap<String,Object>();
        HangeulbotChild hangeulbotChild= apiDao.getChildByChildId(childId);
        resultMap.put("contentLogList",apiDao.getChildContentLog(hangeulbotChild,pageable));
        return resultMap;
    }

    @Override
    public void wordPointer() {
        //추후 조합 난이도 및 정답율을 합쳐서 의미적 난이도에 포함 시킨다.

        //1. 우선 단어의 조합 난이도만을 등급화 한다.
        List<HangeulbotWord> wordList = apiDao.getAllWordOrderByBlockMixGrade();
        log.error(wordList.toString());
        //조합 난이도는 10단계로 구분된다.

        //조합 난이도 단계 마다 단어의 갯수는 총 단어수를 5으로 나눈정도 (나머지 생략)
        int wordNumForGrade = wordList.size()/5;
        int currentGrade = 0;
        int currentGradeNum = 0;
        ArrayList<HangeulbotWordGrade> wordGrades = new ArrayList<>();
        for(int i=0;i<wordList.size();i++){
            HangeulbotWordGrade currentWordObject = new HangeulbotWordGrade();

            // 단어정보를 삽입해줌
            currentWordObject.setHangeulbotWord(wordList.get(i));

            //todo 의미 난이도 관련 정보는 우선 디폴트 값으로 넣어줌 toto
            currentWordObject.setAnswerRate(0.0f);
            currentWordObject.setLogNumber(0);
            currentWordObject.setSemanticGrade(0);
            currentWordObject.setWordId(wordList.get(i).getWordId());
            //시작
            if(currentGrade==0){
                currentWordObject.setMixGrade(1);
                currentGrade=1;
                currentGradeNum = 1;
            }else if(currentGrade!=0){
                currentGrade = wordGrades.get(wordGrades.size()-1).getMixGrade();
                //이전 원소의 조합난이도가 현재 원소의 조합난이도와 작거나 같으면 같은 등급을 부여
                if(wordList.get(i).getBlockMixGrade()<wordList.get(i-1).getBlockMixGrade()){
                    currentGrade++;
                    currentWordObject.setMixGrade(currentGrade);

                //이전 원소보다 현재 원소가 클경우
                }else{
                    //현재 단계의 단어수가 wordNumForGrade를 넘은 경우에는 다음 단어로 돌려주고 아닐 경우 한단게 올려줌
                    if(currentGradeNum>=wordNumForGrade){
                        //근데 이미 5단계라면 그냥 5단계에 계속넣어줌
                        if (currentGrade != 5) {

                            currentGrade += 1;
                            currentGradeNum=0;
                            currentWordObject.setMixGrade(currentGrade);


                        }else{
                            currentWordObject.setMixGrade(currentGrade);
                            currentGradeNum++;

                        }

                    }else{
                        currentWordObject.setMixGrade(currentGrade);
                        currentGradeNum++;
                    }
                }
            }
            //종합 등급은 mixGrade + semanticGrade
            currentWordObject.setWordGrade(currentWordObject.getMixGrade()+currentWordObject.getSemanticGrade());
            wordGrades.add(currentWordObject);
        }
        apiDao.saveWordGrades(wordGrades);
    }
}
