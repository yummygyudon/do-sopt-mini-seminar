package org.sopt.common.util;


/**
 * 상수 정보만 가지고 있기 때문에 객체 생성을 방지하고자 Abstract Class 로 선언했습니다.
 */
public abstract class ConstName {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_HEADER = "Bearer ";
    public static final String AUTH_USER = "auth_user";
    public static final String INVITATION = "invitation";


    public static final String REFRESH_TOKEN_NAME = "refreshToken";


    /**
     * 혹시나 발생할 Reflection 조작 방지를 위해 생성자 실행 시, Exception을 발생 시킵니다.
     */
    private ConstName() {
        throw new NullPointerException("This method should not be executed.");
    }

}
