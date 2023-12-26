package org.sopt.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.sopt.common.model.ApiResponse;
import org.sopt.exception.base.ErrorEnum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.sopt.common.util.ConstName.BEARER_HEADER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


public abstract class RequestUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static boolean isContainsAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION);
        return authorization != null
                && authorization.startsWith(BEARER_HEADER);
    }

    // 유효한 Authorization Bearer Token에서 AccessToken 만 뽑아오기
    public static String getAuthorizationAccessToken(HttpServletRequest request) {
        // "Bearer " 문자열 제외하고 뽑아오기
        return request.getHeader(AUTHORIZATION).substring(7);
    }

    public static void setErrorBodyOnResponse(HttpServletResponse response, ErrorEnum error){

        response.setStatus(error.getStatusCode());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try {
            String body = objectMapper.writeValueAsString(ApiResponse.error(error));
            response.getWriter().write(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
