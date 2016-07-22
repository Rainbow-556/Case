package com.rainbow556.carlli.rainbow556.annotation;

import java.util.List;

/**
 * Created by Carl.li on 2016/7/21.
 */
public class ParamBean<T>{
    @Param(method = "get", value = "value1")
    private String value;

    private List<T> list;

    public static class Ha{

    }
}
