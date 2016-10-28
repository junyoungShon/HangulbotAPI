package com.hangulbot.repository;

import com.hangulbot.vo.HangeulbotChild;
import com.hangulbot.vo.HangeulbotPhonics;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jyson on 2016. 7. 15..
 */
public interface HangeulbotPhonicsRepository extends JpaRepository<HangeulbotPhonics,Integer> {

    HangeulbotPhonics findByHangeulbotChildAndLetterAndType(HangeulbotChild hangeulbotchild, char letter, String type);
}
