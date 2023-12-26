package org.sopt.common.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.exception.base.ErrorEnum;
import org.sopt.exception.base.SuccessEnum;


@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {
    private final int code;
    private final String message;
    private T data;

    public static ApiResponse success(SuccessEnum success) {
        return new ApiResponse<>(success.getStatusCode(), success.getSuccessMessage());
    }

    public static <T> ApiResponse<T> success(SuccessEnum success, T data) {
        return new ApiResponse<T>(success.getStatusCode(), success.getSuccessMessage(), data);
    }

    public static ApiResponse error(ErrorEnum error) {
        return new ApiResponse<>(error.getStatusCode(), error.getErrorMessage());
    }

    public static <T> ApiResponse <T> error(ErrorEnum error, String message) {
        return new ApiResponse<>(error.getStatusCode(), message);
    }

    public static <T> ApiResponse <T> error(ErrorEnum error, String message, T stackTraceElements) {
        return new ApiResponse<>(error.getStatusCode(), message, stackTraceElements);
    }
}
