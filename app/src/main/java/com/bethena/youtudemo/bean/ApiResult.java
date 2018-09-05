package com.bethena.youtudemo.bean;

import java.util.List;

public class ApiResult<T> {
    public int errorcode;
    public String errormsg;
    public String seq;
    public List<T> tags;
}
