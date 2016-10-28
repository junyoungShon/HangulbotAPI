package com.hangulbot.repository;

import com.hangulbot.vo.HangeulbotChild;
import com.hangulbot.vo.HangeulbotWord;
import com.hangulbot.vo.HangeulbotWordAchievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by jyson on 2016. 9. 30..
 */
public interface HangeulbotWordAchievementRepository extends JpaRepository<HangeulbotWordAchievement,Integer> {
    HangeulbotWordAchievement findByHangeulbotChildAndHangeulbotWord(HangeulbotChild hangeulbotChild, HangeulbotWord hangeulbotWord);

    List<HangeulbotWordAchievement> getByHangeulbotChild(HangeulbotChild hangeulbotChild);

    HangeulbotWordAchievement getByHangeulbotChildAndHangeulbotWord(HangeulbotChild hangeulbotChild, HangeulbotWord hangeulbotWord);
}
