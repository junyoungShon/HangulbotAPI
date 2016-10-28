package com.hangulbot.repository;

import com.hangulbot.vo.HangeulbotWord;
import com.hangulbot.vo.HangeulbotWordSemanticGrade;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jyson on 2016. 9. 30..
 */
public interface HangeulbotWordSemanticGradeRepository extends JpaRepository<HangeulbotWordSemanticGrade,Integer> {
    HangeulbotWordSemanticGrade findByHangeulbotWord(HangeulbotWord hangeulbotWord);
}
