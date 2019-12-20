package com.flower.sea.commonservice.utils;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

/**
 * list集合排序工具类
 *
 * @author zhangLei
 * @serial 2019/12/20 13:39
 */
public class ListSortUtils {

    /**
     * 对list的元素按照多个属性名称排序,
     * list元素的属性可以是数字（byte、short、int、long、float、double等，支持正数、负数、0）、char、String、java.util.Date
     *
     * @param list        源list集合
     * @param sortNameArr list元素的属性名称
     * @param isAsc       true升序，false降序
     */
    public static <E> void sort(List<E> list, final boolean isAsc, final String... sortNameArr) {
        list.sort((a, b) -> {
            int ret = 0;
            try {
                for (String s : sortNameArr) {
                    ret = ListSortUtils.compareObject(s, isAsc, a, b);
                    if (0 != ret) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ret;
        });
    }

    /**
     * 给list的每个属性都指定是升序还是降序
     *
     * @param list        源list集合
     * @param sortNameArr 参数数组
     * @param typeArr     每个属性对应的升降序数组， true升序，false降序
     */

    public static <E> void sort(List<E> list, final String[] sortNameArr, final boolean[] typeArr) {
        if (sortNameArr.length != typeArr.length) {
            throw new RuntimeException("属性数组元素个数和升降序数组元素个数不相等");
        }
        list.sort((a, b) -> {
            int ret = 0;
            try {
                for (int i = 0; i < sortNameArr.length; i++) {
                    ret = ListSortUtils.compareObject(sortNameArr[i], typeArr[i], a, b);
                    if (0 != ret) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ret;
        });
    }

    /**
     * 对2个对象按照指定属性名称进行排序
     *
     * @param sortName 属性名称
     * @param isAsc    true升序，false降序
     * @param a        对象a
     * @param b        对象b
     * @return <E>
     * @throws Exception Exception
     */
    private static <E> int compareObject(final String sortName, final boolean isAsc, E a, E b) throws Exception {
        int ret;
        Object value1 = ListSortUtils.forceGetFieldValue(a, sortName);
        Object value2 = ListSortUtils.forceGetFieldValue(b, sortName);
        String str1 = value1.toString();
        String str2 = value2.toString();
        if (value1 instanceof Number && value2 instanceof Number) {
            int maxLen = Math.max(str1.length(), str2.length());
            str1 = ListSortUtils.addZero2Str((Number) value1, maxLen);
            str2 = ListSortUtils.addZero2Str((Number) value2, maxLen);
        } else if (value1 instanceof Date && value2 instanceof Date) {
            long time1 = ((Date) value1).getTime();
            long time2 = ((Date) value2).getTime();
            int maxLen = Long.toString(Math.max(time1, time2)).length();
            str1 = ListSortUtils.addZero2Str(time1, maxLen);
            str2 = ListSortUtils.addZero2Str(time2, maxLen);
        }
        if (isAsc) {
            ret = str1.compareTo(str2);
        } else {
            ret = str2.compareTo(str1);
        }
        return ret;
    }

    /**
     * 给数字对象按照指定长度在左侧补0.
     * <p>
     * 使用案例: addZero2Str(11,4) 返回 "0011", addZero2Str(-18,6)返回 "-000018"
     *
     * @param numObj 数字对象
     * @param length 指定的长度
     * @return String
     */
    private static String addZero2Str(Number numObj, int length) {
        NumberFormat nf = NumberFormat.getInstance();
        // 设置是否使用分组
        nf.setGroupingUsed(false);
        // 设置最大整数位数
        nf.setMaximumIntegerDigits(length);
        // 设置最小整数位数
        nf.setMinimumIntegerDigits(length);
        return nf.format(numObj);
    }

    /**
     * 获取指定对象的指定属性值（去除private,protected的限制）
     *
     * @param obj       属性名称所在的对象
     * @param fieldName 属性名称
     * @return Object
     * @throws Exception Exception
     */
    private static Object forceGetFieldValue(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        Object object;
        boolean isAccessible = field.isAccessible();
        if (!isAccessible) {
            // 如果是private,protected修饰的属性，需要修改为可以访问的
            field.setAccessible(true);
            object = field.get(obj);
            // 还原private,protected属性的访问性质
            field.setAccessible(false);
            return object;
        }
        object = field.get(obj);
        return object;
    }
}
