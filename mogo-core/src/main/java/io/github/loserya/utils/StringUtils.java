package io.github.loserya.utils;


/**
 * 字符串工具类
 *
 * @author loser
 * @since 1.0.0
 */
public class StringUtils {

    private StringUtils() {
    }

    /**
     * 安全的进行字符串 format
     *
     * @param target 目标字符串
     * @param params format 参数
     * @return format 后的
     */
    public static String format(String target, Object... params) {
        if (target.contains("%s") && ArrayUtils.isNotEmpty(params)) {
            return String.format(target, params);
        }
        return target;
    }

    /**
     * 首字母转换小写
     *
     * @param str 需要转换的字符串
     * @return 转换好的字符串
     */
    public static String firstToLowerCase(final String str) {
        if (null == str || str.length() == 0) {
            return "";
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * @param str 字符串
     * @return 是否不空
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * @param str 字符串
     * @return 是否为空
     */
    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        }
        if (str.equals("null")) {
            return true;
        }
        int len = str.length();
        if (len == 0) {
            return true;
        }
        for (int i = 0; i < len; i++) {
            switch (str.charAt(i)) {
                case ' ':
                case '\t':
                case '\n':
                case '\r':
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
