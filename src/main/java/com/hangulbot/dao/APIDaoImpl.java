package com.hangulbot.dao;

import com.hangulbot.repository.*;
import com.hangulbot.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jyson on 2016. 9. 2..
 */
@PersistenceContext
@Repository
public class APIDaoImpl implements APIDao{
    // Define the logger object for this class
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private HangeulbotUserRepository hangeulbotUserRepository;
    @Autowired
    private HangeulbotDeviceRepository hangeulbotDeviceRepository;
    @Autowired
    private HangeulbotChildRepository hangeulbotChildRepository;
    @Autowired
    private HangeulbotContentRepository hangeulbotContentRepository;
    @Autowired
    private HangeulbotUserWordLogRepository hangeulbotUserWordLogRepository;
    @Autowired
    private HangeulbotWordRepository hangeulbotWordRepository;
    @Autowired
    private HangeulbotWordSemanticGradeRepository hangeulbotWordSemanticGradeRepository;
    @Autowired
    private HangeulbotUserContentRepository hangeulbotUserContentRepository;
    @Autowired
    private HangeulbotWordGradeRepository hangeulbotWordGradeRepository;
    @Autowired
    private HangeulbotWordAchievementRepository hangeulbotWordAchievementRepository;
    @Autowired
    private HangeulbotPhonicsRepository hangeulbotPhonicsRepository;


    @Override
    public HangeulbotUser isDuplicatedByEmailId(String userId) {
        return hangeulbotUserRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public HangeulbotUser putHangeulbotDeviceInfoAndHangeulbotUserInfo(HangeulbotUser hangeulbotUser) {

        hangeulbotUser = hangeulbotUserRepository.save(hangeulbotUser);
        hangeulbotUser.getDeviceId();
        log.debug("한글봇유저가 가진 디바이스 아이디"+hangeulbotUser.getDeviceId());
        HangeulbotDevice hangeulbotDevice = em.find(HangeulbotDevice.class,hangeulbotUser.getDeviceId());
        log.debug("엔티티 매니저"+em.toString());
        hangeulbotDevice.setUserId(hangeulbotUser.getUserId());
        em.merge(hangeulbotDevice);

        return hangeulbotUser;
    }

    @Override
    public HangeulbotUser loginByUserId(String userId) {
        return hangeulbotUserRepository.findByUserId(userId);
    }

    @Override
    public void insertChildInfo(HangeulbotChild hangeulbotChild) {
        hangeulbotChildRepository.save(hangeulbotChild);
    }

    @Override
    public HangeulbotDevice isHangeulbotDevice(String hangeulbotDeviceAddress) {
        return hangeulbotDeviceRepository.findByDeviceAddress(hangeulbotDeviceAddress);
    }

    @Override
    public List<HangeulbotContent> getContentList() {
        return hangeulbotContentRepository.findAll();
    }

    @Override
    public List<HangeulbotUserWordLog> getWordLogListByChildId(String childId) {
        String sql = "select * from hangeulbot_user_word_log where user_word_log_idx in( "+
                "select MAX(user_word_log_idx) as idx " +
                "from hangeulbot_user_word_log where child_id = ?1 " +
                "GROUP BY word_id)";
        Query query = em.createNativeQuery(sql,HangeulbotUserWordLog.class);
        query.setParameter(1,childId);
        List<HangeulbotUserWordLog> list = query.getResultList();
        System.out.println(list.toString());
        return list;
    }

    @Override
    public List<HangeulbotWord> getNewWordByRandom(int needWordNum, ArrayList<HangeulbotWord> wordList) {
        String forwardSql = "select * from hangeulbot_word where word_Id not in (";
        String plusSql ="";
        for(int i=0;i<wordList.size();i++){
            if(wordList.size()==i+1){
                plusSql += "'"+wordList.get(i).getWordId()+"') order by rand() limit ?1 ";
            }else{
                plusSql += "'"+wordList.get(i).getWordId()+"',";
            }
        }
        String sql = "";
        //아무 단어도 추가되지 않았을 때
        if(wordList.size()==0){
            sql = "select * from hangeulbot_word order by rand() limit ?1";
        }else{
            sql = forwardSql+plusSql;
        }
        System.out.println("현재의 sql"+sql);
        Query query = em.createNativeQuery(sql,HangeulbotWord.class);
        query.setParameter(1,needWordNum);
        List<HangeulbotWord> list = query.getResultList();
        return list;
    }




    @Override
    public void insertUserWordLog(HangeulbotUserWordLog hangeulbotUserWordLog) {
        hangeulbotUserWordLogRepository.save(hangeulbotUserWordLog);
    }

    @Override
    public HangeulbotWord getWordByWordId(int wordId) {
        return hangeulbotWordRepository.getOne(wordId);
    }

    @Override
    public HangeulbotContent getContentByContentId(String contentId) {
        return hangeulbotContentRepository.getOne(contentId);
    }

    @Override
    public HangeulbotChild getChildByChildId(String childId) {
        return hangeulbotChildRepository.getOne(childId);
    }

    @Override
    public HangeulbotWordSemanticGrade getHangeulbotWordSemanticGradeByWord(HangeulbotWord hangeulbotWord) {
        return hangeulbotWordSemanticGradeRepository.findByHangeulbotWord(hangeulbotWord);
    }

    @Override
    public void saveWordSemanticGrade(HangeulbotWordSemanticGrade hangeulbotWordSemanticGrade) {
        hangeulbotWordSemanticGradeRepository.save(hangeulbotWordSemanticGrade);
    }

    @Override
    public void saveUserContentLog(HangeulbotUserContentLog hangeulbotUserContentLog) {
        hangeulbotUserContentRepository.save(hangeulbotUserContentLog);
    }

    @Override
    public List<HangeulbotUserWordLog> getChildWordLog(HangeulbotChild hangeulbotChild, Pageable pageable) {
        return hangeulbotUserWordLogRepository.findByHangeulbotChild(hangeulbotChild,pageable);
    }

    @Override
    public List<HangeulbotUserContentLog> getChildContentLog(HangeulbotChild hangeulbotChild, Pageable pageable) {
        return hangeulbotUserContentRepository.findByHangeulbotChild(hangeulbotChild,pageable);
    }


    @Override
    public List<HangeulbotUserWordLog> getChildWordLog(HangeulbotChild hangeulbotChild) {
        return hangeulbotUserWordLogRepository.findByHangeulbotChildOrderByStartDateAsc(hangeulbotChild);
    }

    @Override
    public List<HangeulbotUserContentLog> getChildContentLog(HangeulbotChild hangeulbotChild) {
        return hangeulbotUserContentRepository.findByHangeulbotChildOrderByStartDateAsc(hangeulbotChild);
    }

    /*
    * 주간 정답율 구하는 메서드 - 최근 기준으로 주별로 알려줌 해당아이디의 아이의 성적
    */
    @Override
    public Float getWeekAnswerRateByChildId(int beforeWeek, String childId) {
        log.error(childId);
        String forwardSql = "SELECT ifnull(answerRate,0.0) from("+
                "select sum(ifnull(iscorrect,0))/count(@Rownum) as answerRate FROM (select hwl.child_id,hw.word_id,@Rownum \\:=@Rownum+1"+
                ",hwl.is_correct as iscorrect from hangeulbot_child hc , HANGEULBOT_User_WORD_LOG hwl,hangeulbot_word hw,(select @Rownum \\:=0)TMP "+
                "where hw.word_id = hwl.word_id and hwl.child_id ='" + childId +"' "+
                "and date(hwl.start_date) >= date_add(now(), interval -"+(beforeWeek+1)+" WEEK)"+
                "and date(hwl.start_date) <= date_add(now(), interval -"+(beforeWeek)+" WEEK)"+
                ") as b )as c";
        Query query = em.createNativeQuery(forwardSql);
        BigDecimal result = (BigDecimal) query.getSingleResult();
        if(result==null)result= BigDecimal.valueOf(0.0f);
        log.error("결과값"+result.toString());

        return result.floatValue();
    }
    /*
    * 주간 정답율 구하는 메서드 - 최근 기준으로 주별로 알려줌 해당아이디의 아이와 동일 나이대의 아이들 성적
    */
    @Override
    public Float getAnotherChildAnswerRateByChildId(int beforeWeek, String childId) {
        String sql =
                "SELECT ifnull(answerRate,0.0) from( " +
                        "select sum(ifnull(iscorrect,0))/count(@Rownum) as answerRate FROM (" +
                        "select hwl.child_id,hw.word_id,@Rownum\\:=@Rownum+1,hwl.is_correct as iscorrect " +
                        "from hangeulbot_child hc , HANGEULBOT_User_WORD_LOG hwl,hangeulbot_word hw," +
                        "(select @Rownum\\:=0)TMP where " +
                        "hw.word_id = hwl.word_id and hwl.child_id in " +
                        "(select child_id from hangeulbot_child " +
                        "where date(child_birth) >= date_add((SELECT child_birth from hangeulbot_child " +
                        "where child_id = '"+childId+"'), INTERVAL -1 MONTH) " +
                        "AND " +
                        "date(child_birth) <= date_add((SELECT child_birth from hangeulbot_child " +
                        "where child_id = '"+childId+"'), INTERVAL 0 MONTH) "+
                        "and date(hwl.start_date) >= date_add(now(), interval -"+(beforeWeek+1)+" WEEK)" +
                        "and date(hwl.start_date) <= date_add(now(), INTERVAL -"+(beforeWeek)+" WEEK)" +
                        ")) as b" +
                        ")as c";
        log.error("현재 SQL"+sql);
        Query query = em.createNativeQuery(sql);
        BigDecimal result = (BigDecimal) query.getSingleResult();
        if(result==null)result= BigDecimal.valueOf(0.0f);
        log.error("결과값"+result.toString());
        return result.floatValue();
    }

    @Override
    public List<HangeulbotWord> getAllWordOrderByBlockMixGrade() {
        return hangeulbotWordRepository.findAll(new Sort(Sort.Direction.ASC,"blockMixGrade"));
    }

    @Override
    public void saveWordGrades(ArrayList<HangeulbotWordGrade> wordGrades) {
        hangeulbotWordGradeRepository.save(wordGrades);
    }

    @Override
    public Float getAnswerRateByChildIdAndGrade(int i, String childId) {
        String sql = "select sum(ifnull(iscorrect,0))/count(@Rownum) as answerRate from (\n" +
                "  select huwl.child_id, hw.word_id, hwg.word_grade,huwl.is_correct as iscorrect,@Rownum\\:=@Rownum+1\n" +
                "  from hangeulbot_child hc, hangeulbot_word hw, " +
                "   hangeulbot_user_word_log huwl, hangeulbot_word_grade hwg,(select @Rownum\\:=0) TMP\n" +
                "  where hc.child_id = huwl.child_id\n" +
                "        and hw.word_id = huwl.word_id\n" +
                "        and hwg.word_id = hw.word_id\n" +
                "        and hwg.word_grade = "+i+"\n" +
                "        and huwl.child_id = '"+childId+"'\n" +
                ")as a";
        Query query = em.createNativeQuery(sql);
        BigDecimal result = (BigDecimal) query.getSingleResult();
        if(result==null)result= BigDecimal.valueOf(0.0f);
        log.error("결과값"+result.toString());
        return result.floatValue();
    }

    @Override
    public Float getAnotherChildAnswerRateByChildIdAndWordGrade(int i, String childId) {
        String sql = "select sum(ifnull(iscorrect,0))/count(@Rownum) as answerRate from (\n" +
                "  select huwl.child_id, hw.word_id, hwg.word_grade,huwl.is_correct as iscorrect,@Rownum\\:=@Rownum+1\n" +
                "  from hangeulbot_child hc, hangeulbot_word hw, hangeulbot_user_word_log huwl, hangeulbot_word_grade hwg,(select @Rownum\\:=0) TMP\n" +
                "  where hw.word_id = huwl.word_id\n" +
                "        and hwg.word_id = hw.word_id\n" +
                "        and hwg.word_grade = "+i+"\n" +
                "\n" +
                "        and huwl.child_id in\n" +
                "            (SELECT child_id\n" +
                "             FROM hangeulbot_child\n" +
                "             WHERE date(child_birth) >= date_add(\n" +
                "                 (SELECT child_birth\n" +
                "                  FROM hangeulbot_child\n" +
                "                  WHERE child_id ='"+childId+"'), INTERVAL -1 MONTH)\n" +
                "                   AND\n" +
                "                   date(child_birth) <= date_add(\n" +
                "                       (SELECT child_birth\n" +
                "                        FROM hangeulbot_child\n" +
                "                        WHERE child_id = '"+childId+"'), INTERVAL 0 MONTH)\n" +
                "))as a";
        Query query = em.createNativeQuery(sql);
        BigDecimal result = (BigDecimal) query.getSingleResult();
        if(result==null)result= BigDecimal.valueOf(0.0f);
        log.error("결과값"+result.toString());
        return result.floatValue();
    }

    @Override
    public HangeulbotWordAchievement getWordAchievementInfoByChildIdAndWordId(HangeulbotChild hangeulbotChild, HangeulbotWord hangeulbotWord) {
        return hangeulbotWordAchievementRepository.findByHangeulbotChildAndHangeulbotWord(hangeulbotChild, hangeulbotWord);
    }

    @Override
    public void saveWordAchievement(HangeulbotWordAchievement hangeulbotWordAchievement) {
        hangeulbotWordAchievementRepository.save(hangeulbotWordAchievement);
    }

    @Override
    public Float getStudyAchivement(int i, String childId) {
        String sql =
                "select sum(hwg.word_grade)/count(*)\n" +
                        "  from\n" +
                        "    (\n" +
                        "    select\n" +
                        "        DISTINCT hwg.word_id\n" +
                        "      FROM\n" +
                        "        hangeulbot_child hc,\n" +
                        "        hangeulbot_user_word_log huwl,\n" +
                        "        hangeulbot_word hw,\n" +
                        "        hangeulbot_word_grade hwg\n" +
                        "      where\n" +
                        "          hc.child_id =  '"+childId+"'\n" +
                        "        AND\n" +
                        "          huwl.word_id = hwg.word_id\n" +
                        "        AND\n" +
                        "          date(huwl.start_date) >= date_add(now(), interval -"+i+1+" WEEK)\n" +
                        "        AND\n" +
                        "          date(huwl.start_date) <= date_add(now(), INTERVAL -"+i+" WEEK)\n" +
                        "    ) as a,\n" +
                        "      hangeulbot_word_grade hwg\n" +
                        "  WHERE\n" +
                        "    a.word_id = hwg.word_grade";
        Query query = em.createNativeQuery(sql);
        BigDecimal result = (BigDecimal) query.getSingleResult();

        if(result==null)result= BigDecimal.valueOf(0.0f);
        log.error("결과값"+result.toString());

        return (result.floatValue())*10;
    }

    @Override
    public Float getAnotherStudyAchievement(int i, String childId) {
        String sql = "select sum(hwg.word_grade)/count(*)\n" +
                "from\n" +
                "  (\n" +
                "    select\n" +
                "      DISTINCT hwg.word_id\n" +
                "    FROM\n" +
                "      hangeulbot_child hc,\n" +
                "      hangeulbot_user_word_log huwl,\n" +
                "      hangeulbot_word hw,\n" +
                "      hangeulbot_word_grade hwg\n" +
                "    where\n" +
                "      huwl.child_id in\n" +
                "        (SELECT child_id\n" +
                "         FROM hangeulbot_child\n" +
                "         WHERE\n" +
                "           date(child_birth) >= date_add(\n" +
                "             (SELECT child_birth\n" +
                "              FROM hangeulbot_child\n" +
                "              WHERE child_id = '"+childId+"'), INTERVAL -1 MONTH)\n" +
                "         AND\n" +
                "           date(child_birth) < date_add(\n" +
                "              (SELECT child_birth\n" +
                "               FROM hangeulbot_child\n" +
                "               WHERE child_id = '"+childId+"'), INTERVAL -0 MONTH)\n" +
                "\n" +
                "        )\n" +
                "      AND\n" +
                "      huwl.word_id = hwg.word_id\n" +
                "      AND\n" +
                "      date(huwl.start_date) >= date_add(now(), interval -"+i+1+" WEEK)\n" +
                "      AND\n" +
                "      date(huwl.start_date) < date_add(now(), INTERVAL -"+i+" WEEK)\n" +
                "  ) as a,\n" +
                "  hangeulbot_word_grade hwg\n" +
                "WHERE\n" +
                "  a.word_id = hwg.word_grade\n";
        Query query = em.createNativeQuery(sql);
        BigDecimal result = (BigDecimal) query.getSingleResult();

        if(result==null)result= BigDecimal.valueOf(0.0f);
        log.error("결과값"+result.toString());

        return (result.floatValue())*10;
    }

    @Override
    public HangeulbotWordAchievement getWordAchievementInfoByHangeulbotChildAndWord
            (HangeulbotChild hangeulbotChild, HangeulbotWord hangeulbotWord)
    {
        return hangeulbotWordAchievementRepository.getByHangeulbotChildAndHangeulbotWord(hangeulbotChild,hangeulbotWord);
    }

    @Override
    public HangeulbotPhonics getPhonicsByTypeAndHangeulbotChildAndLetter
            (char letter, HangeulbotChild hangeulbotchild, String type)
    {
        /*String sql = " select letter,type,idx,child_id,test_count,right_count from hangeulbot_phonics where type='"+type+"' and letter = '"+letter+"' and child_id='"+hangeulbotchild.getChildId()+"'";
        Query query = em.createNativeQuery(sql);
        HangeulbotPhonics hangeulbotPhonics = (HangeulbotPhonics) query.getSingleResult();*/
        return hangeulbotPhonicsRepository.findByHangeulbotChildAndLetterAndType(hangeulbotchild,letter,type);
    }

    @Override
    public void savePhonics(HangeulbotPhonics hangeulbotPhonics) {
        hangeulbotPhonicsRepository.save(hangeulbotPhonics);
    }

    @Override
    public int getTotalStudyTime(HangeulbotChild hangeulbotChild) {
        String sql =
                "select sum(time_required) from hangeulbot_user_word_log where child_id = '"+hangeulbotChild.getChildId()+"'";
        Query query = em.createNativeQuery(sql);
        BigDecimal result = (BigDecimal) query.getSingleResult();
        if(result==null)result= BigDecimal.valueOf(0);
        return result.intValue();
    }

    @Override
    public int getFinishedContentsNumber(HangeulbotChild hangeulbotChild) {
        String sql =
                "select sum(finished) from hangeulbot_user_content_log where child_id = '"+hangeulbotChild.getChildId()+"'";
        Query query = em.createNativeQuery(sql);
        BigDecimal result = (BigDecimal) query.getSingleResult();
        if(result==null)result= BigDecimal.valueOf(0);
        return result.intValue();
    }

    @Override
    public List<HangeulbotChild> getHangeulbotChildByUserId(String userId) {
        return hangeulbotChildRepository.getHangeulbotChildByUserId(userId);
    }


}
