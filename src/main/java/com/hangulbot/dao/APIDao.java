package com.hangulbot.dao;

import com.hangulbot.vo.*;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jyson on 2016. 9. 2..
 */

public interface APIDao{
    HangeulbotUser isDuplicatedByEmailId(String userId) ;

    HangeulbotUser putHangeulbotDeviceInfoAndHangeulbotUserInfo(HangeulbotUser hangeulbotUser);

    HangeulbotUser loginByUserId(String userId);

    void insertChildInfo(HangeulbotChild hangeulbotChild);

    HangeulbotDevice isHangeulbotDevice(String hangeulbotDeviceAddress);

    List<HangeulbotContent> getContentList();

    List<HangeulbotUserWordLog> getWordLogListByChildId(String childId);

    List<HangeulbotWord> getNewWordByRandom(int needWordNum, ArrayList<HangeulbotWord> wordList);


    void insertUserWordLog(HangeulbotUserWordLog hangeulbotUserWordLog);

    HangeulbotWord getWordByWordId(int wordId);

    HangeulbotContent getContentByContentId(String contentId);

    HangeulbotChild getChildByChildId(String childId);

    HangeulbotWordSemanticGrade getHangeulbotWordSemanticGradeByWord(HangeulbotWord hangeulbotWord);

    void saveWordSemanticGrade(HangeulbotWordSemanticGrade hangeulbotWordSemanticGrade);

    void saveUserContentLog(HangeulbotUserContentLog hangeulbotUserContentLog);

    List<HangeulbotUserWordLog> getChildWordLog(HangeulbotChild hangeulbotChild, Pageable pageable);

    List<HangeulbotUserContentLog> getChildContentLog(HangeulbotChild hangeulbotChild, Pageable pageable);

    List<HangeulbotUserWordLog> getChildWordLog(HangeulbotChild hangeulbotChild);

    List<HangeulbotUserContentLog> getChildContentLog(HangeulbotChild hangeulbotChild);

    Float getWeekAnswerRateByChildId(int beforeWeek, String childId);

    Float getAnotherChildAnswerRateByChildId(int beforeWeek, String childId);

    List<HangeulbotWord> getAllWordOrderByBlockMixGrade();

    void saveWordGrades(ArrayList<HangeulbotWordGrade> wordGrades);

    Float getAnswerRateByChildIdAndGrade(int i, String childId);

    Float getAnotherChildAnswerRateByChildIdAndWordGrade(int i, String childId);

    HangeulbotWordAchievement getWordAchievementInfoByChildIdAndWordId(HangeulbotChild hangeulbotChild, HangeulbotWord hangeulbotWord);

    void saveWordAchievement(HangeulbotWordAchievement hangeulbotWordAchievement);





    Float getStudyAchivement(int i, String childId);

    Float getAnotherStudyAchievement(int i, String childId);

    HangeulbotWordAchievement getWordAchievementInfoByHangeulbotChildAndWord(HangeulbotChild hangeulbotChild, HangeulbotWord hangeulbotWord);

    HangeulbotPhonics getPhonicsByTypeAndHangeulbotChildAndLetter(char letter, HangeulbotChild hangeulbotchild, String type);

    void savePhonics(HangeulbotPhonics hangeulbotPhonics);

    int getTotalStudyTime(HangeulbotChild hangeulbotChild);

    int getFinishedContentsNumber(HangeulbotChild hangeulbotChild);

    List<HangeulbotChild> getHangeulbotChildByUserId(String userId);
}
