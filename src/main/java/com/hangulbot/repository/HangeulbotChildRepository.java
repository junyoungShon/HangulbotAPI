package com.hangulbot.repository;

import com.hangulbot.vo.HangeulbotChild;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by jyson on 2016. 7. 15..
 */
public interface HangeulbotChildRepository extends JpaRepository<HangeulbotChild,String> {
    List<HangeulbotChild> getHangeulbotChildByUserId(String userId);
}
