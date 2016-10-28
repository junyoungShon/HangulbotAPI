package com.hangulbot.utils;

import com.hangulbot.common.HangeulbotConsonants;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by jyson on 2016. 10. 14..
 */
@Component("hangeulDisassembler")
public class HangeulDisassembler {



    public List<Map<String, Character>> hangeuldisassemble(String text){

        List<Map<String, Character>> list = new ArrayList<Map<String, Character>>();
        String lastStr = "";
        System.out.println(text);
        for(int i = 0 ; i < text.length();i++)
        {
            Map<String, Character> map = new HashMap<String, Character>();
            char test = text.charAt(i);

            if(test >= 0xAC00)
            {
                char uniVal = (char) (test - 0xAC00);

                char cho = (char) (((uniVal - (uniVal % 28))/28)/21);
                char jun = (char) (((uniVal - (uniVal % 28))/28)%21);
                char jon = (char) (uniVal %28);

                map.put("cho", HangeulbotConsonants.CHO[cho]);
                map.put("jun", HangeulbotConsonants.JUN[jun]);
                if((char)jon != 0x0000){
                    map.put("jon", HangeulbotConsonants.JON[jon]);
                }else{
                    map.put("jon", 'a');
                }
                list.add(map);
            }
        }
        return list;
    }

}
