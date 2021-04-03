package com.diary_online.diary_online.util;

import com.diary_online.diary_online.model.pojo.Section;

public class UtilSection {
    public static boolean isVisible(Section section){
        return section.getPrivacy().equals("public");
    }
}
