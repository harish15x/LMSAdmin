package com.bridgelabz.lmsadmin.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseClass {

    private int statusCode;
    private String statusMessage;
    private Object object;

}
