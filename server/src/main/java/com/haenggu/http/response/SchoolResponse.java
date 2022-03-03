package com.haenggu.http.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SchoolResponse {
    private String schoolName;
    private String deptName;
}
