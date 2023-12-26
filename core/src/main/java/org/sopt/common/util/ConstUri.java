package org.sopt.common.util;

import org.springframework.util.PatternMatchUtils;

/**
 * 상수 정보만 가지고 있기 때문에 객체 생성을 방지하고자 Abstract Class 로 선언했습니다.
 */
public abstract class ConstUri {
    public static final String DEFAULT_API_PATH = "/api/v2";


    public static final String DASHBOARD_API_PATH = DEFAULT_API_PATH + "/dashboard/**";
    public static final String VIEW_URI = DEFAULT_API_PATH + "/view/**";

    public static final String AUTH_URI = DEFAULT_API_PATH + "/auth/**";
    public static final String REISSUE_URI = DEFAULT_API_PATH + "/auth/token/reissue";

    public static final String HEALTH_CHECK_URI = DEFAULT_API_PATH + "/health";

    // /api/v2/invite/link?code=...
    // /api/v2/invite/mail?code=...
    public static final String INVITE_LINK_URI = DEFAULT_API_PATH + "/invite/link";
    public static final String INVITE_MAIL_URI = DEFAULT_API_PATH + "/invite/mail";

    public static final String SES_URI = DEFAULT_API_PATH + "/ses/**";


    public static final String[] WHITE_URIS_IN_FILTERS = new String[]{
            VIEW_URI, HEALTH_CHECK_URI, AUTH_URI
    };

    public static boolean isWhiteUri(String[] whiteUris, String verifyUri) {
        return PatternMatchUtils.simpleMatch(whiteUris, verifyUri);
    }

    /**
     * 혹시나 발생할 Reflection 조작 방지를 위해 생성자 실행 시, Exception을 발생 시킵니다.
     */
    private ConstUri() {
        throw new NullPointerException("This method should not be executed.");
    }
}
