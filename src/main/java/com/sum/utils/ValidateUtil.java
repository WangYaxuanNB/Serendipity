package com.sum.utils;

import cn.hutool.core.util.ObjectUtil;
import com.sum.annotations.Validate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验工具类
 */
@Slf4j
public class ValidateUtil {
    private static final String GET = "get";

    /**
     * 采用反射的方式进行属性非空校验
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String validate(T t) {
        try {
            Field[] fields = t.getClass().getDeclaredFields();
            for (Field field : fields) {
                Validate annotation = field.getAnnotation(Validate.class);
                if (ObjectUtil.isNotNull(annotation)) {
                    String getterName = GET + toProperCase(field.getName());
                    Method getMethod = t.getClass().getDeclaredMethod(getterName);
                    getMethod.setAccessible(true);
                    Object value = getMethod.invoke(t);
                    if (ObjectUtil.isNull(value) || StringUtils.isBlank(String.valueOf(value))) {
                        return annotation.value();
                    }
                }
            }
        } catch (Exception e) {
            log.error("ValidateUtil#validate ERROR :", e);
        }
        return "";
    }

    /**
     * 校验手机号：11位
     * @param mobile
     * @return
     */
    public static boolean validateMobile(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return false;
        }
        String regex = "^1[3-9]\\d{9}$";
        return mobile.matches(regex);
    }

    /**
     * 校验邮箱
     * @param email
     * @return
     */
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * 年龄校验
     * @param age
     * @return
     */
    public static boolean validateAge(String age) {
        if (StringUtils.isBlank(age)) {
            return false;
        }
        String regex = "^[1-9]\\d?|100$";
        return age.matches(regex);
    }

    public static String toProperCase(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
