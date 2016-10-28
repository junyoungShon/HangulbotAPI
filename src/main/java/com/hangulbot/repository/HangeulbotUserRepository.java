package com.hangulbot.repository;

import com.hangulbot.vo.HangeulbotUser;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by jyson on 2016. 7. 6..
 */

public interface HangeulbotUserRepository extends JpaRepository<HangeulbotUser, String> {
    HangeulbotUser findByUserId(String userId);


}
