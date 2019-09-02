package com.x.sdk.constant;

/**
 * 特殊异常编码定义 <br>
 * Date: 2017年4月20日 <br>
 * Copyright (c) 2017 x.com <br>
 * 
 * @author mayt
 */
public final class ExceptionCodeConstant {

    private ExceptionCodeConstant() {
    }

    /**
     * 处理成功
     */
    public static final String SUCCESS = "000000";
    
    /**
     * 处理中
     */
    public static final String PROCESSING = "333333";

    /**
     * 系统级异常
     */
    public static final String SYSTEM_ERROR = "999999";

    /**
     * 传入的参数为空
     */
    public static final String PARAM_IS_NULL = "888888";

    /**
     * 查询无记录
     */
    public static final String NO_RESULT = "000001";

    /**
     * 参数类型不正确
     */
    public static final String PARAM_TYPE_NOT_RIGHT = "000002";

    /**
     * 未配置系统参数或未刷新缓存
     */
    public static final String NO_DATA_OR_CACAE_ERROR = "000003";
    
    
    

}
