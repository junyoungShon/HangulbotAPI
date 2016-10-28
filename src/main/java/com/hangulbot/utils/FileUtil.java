package com.hangulbot.utils;

import com.hangulbot.common.HangeulbotConsonants;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by jyson on 2016. 7. 18..
 */

    @Component("fileUtils")
    public class FileUtil {
        String filePath = HangeulbotConsonants.filePath;

        public FileUtil() throws UnknownHostException {
        }

        public String parseInsertFileInfo(MultipartFile file) throws Exception{

            MultipartFile multipartFile = file;
            String originalFileName = null;
            String originalFileExtension = null;
            String storedFileName = null;


            File newfile = new File(filePath);
            if(newfile.exists() == false){
                newfile.mkdirs();
            }

                if(multipartFile.isEmpty() == false){
                    originalFileName = multipartFile.getOriginalFilename();
                    storedFileName = originalFileName;

                    newfile = new File(filePath + storedFileName);
                    multipartFile.transferTo(newfile);


                }
            InetAddress Address = InetAddress.getLocalHost();
            String IP = Address.getHostAddress();
            String storedUrl = HangeulbotConsonants.apiUrl+"static/childPhoto/" + storedFileName;
            return storedUrl;
            }

        }
