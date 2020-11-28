package cn.hobom.mobile.datacollector.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by WWT on 2019/9/2.
 */

public class ChineseToPinyin {
    public static String getPinyin(String src) {
        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 小写格式
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 有无音标
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        try {
            for (int i = 0; i < t1.length; i++) {
                // 判断是否为汉字字符
                // if(t1[i] >= 32 && t1[i] <= 125)//ASCII码表范围内直接返回
                if (String.valueOf(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);// 转化为拼音
                    t4 += t2[0].substring(0, 1).toUpperCase() + t2[0].substring(1);// 首字母大写
                } else {
                    t4 += String.valueOf(t1[i]);// 不是汉字不处理
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return t4;
    }
}
