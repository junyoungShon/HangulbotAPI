package com.hangulbot.repository;

import com.hangulbot.vo.HangeulbotWordGrade;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jyson on 2016. 9. 30..
 */
public interface HangeulbotWordGradeRepository extends JpaRepository<HangeulbotWordGrade,Integer> {
}
