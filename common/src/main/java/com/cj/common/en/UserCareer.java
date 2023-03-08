package com.cj.common.en;

/**
 * 用户职业
 *
 * @author 杰瑞
 * @date 2023/03/06
 */
public enum UserCareer {

    /**
     * 超级管理员
     */
    SUPER_ADMIN("超级管理员"),
    /**
     * 管理员
     */
    ADMIN("管理员"),
    /**
     * 收银员
     */
    CASHIER("收银员");

    private final String career;
    UserCareer(String career) {
        this.career = career;
    }
    public String getCareer() {
        return career;
    }

    /**
     * 转换为对应的枚举类
     *
     * @param career 职业生涯
     * @return {@link UserCareer}
     */
    public static UserCareer fromCareer(String career) {
        for (UserCareer userCareer : UserCareer.values()) {
            if (userCareer.getCareer().equalsIgnoreCase(career)) {
                return userCareer;
            }
        }
        throw new IllegalArgumentException("无效的用户职业: " + career);
    }
}
