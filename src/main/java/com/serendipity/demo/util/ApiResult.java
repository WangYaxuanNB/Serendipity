/*
 * Copyright 2025 miyukiWordsworth
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.serendipity.demo.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用 API 响应封装
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResult<T> implements Serializable {

    private int code;
    private String msg;
    private boolean success;
    private T data;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time = new Date();

    // 成功（默认消息）
    public static <T> ApiResult<T> ok(T data) {
        return ApiResult.<T>builder()
                .code(200)
                .msg("操作成功")
                .success(true)
                .data(data)
                .time(new Date())
                .build();
    }

    // 成功（自定义消息）
    public static <T> ApiResult<T> ok(T data, String msg) {
        return ApiResult.<T>builder()
                .code(200)
                .msg(msg)
                .success(true)
                .data(data)
                .time(new Date())
                .build();
    }

    // 失败（默认 500）
    public static <T> ApiResult<T> fail(String msg) {
        return ApiResult.<T>builder()
                .code(500)
                .msg(msg)
                .success(false)
                .data(null)
                .time(new Date())
                .build();
    }

    // 失败（自定义状态码）
    public static <T> ApiResult<T> fail(int code, String msg) {
        return ApiResult.<T>builder()
                .code(code)
                .msg(msg)
                .success(false)
                .data(null)
                .time(new Date())
                .build();
    }

    // 成功返回 Map 数据
    public static <T> ApiResult<T> okMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return ok((T) map);
    }

    // 失败返回 Map 数据
    public static <T> ApiResult<T> failMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return fail(500, "操作失败");
    }
}
