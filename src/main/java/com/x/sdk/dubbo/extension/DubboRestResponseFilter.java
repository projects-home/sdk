package com.x.sdk.dubbo.extension;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import com.alibaba.fastjson.JSONObject;

//import net.sf.json.JSONObject;

import com.x.sdk.constant.ExceptionCodeConstant;

public class DubboRestResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext,
            ContainerResponseContext responseContext) throws IOException {
        int status = responseContext.getStatus();
        Object entity = responseContext.getEntity();
        JSONObject data = new JSONObject();
        if (status == 200) {
            if (entity instanceof DubboRestResponse) {
                DubboRestResponse resp = (DubboRestResponse) entity;
                data.put("resultCode", resp.getResultCode());
                data.put("resultMessage", resp.getResultMessage());
            } else {
                data.put("resultCode", ExceptionCodeConstant.SUCCESS);
                data.put("resultMessage", "请求成功，业务处理返回请查看data节点");
                data.put("data", responseContext.getEntity());
            }
            responseContext.setEntity(data);
        } else if (status == 204) {
            //DUBBOX转REST协议，HTTP.204表示接口返回类型是void的情况，此处必须以正确的异常信息抛出，交给DubboRestExceptionMapper进行包装处理 
            //throw new RPCSystemException(ExceptionCodeConstant.Special.SUCCESS, "请求成功,服务端没有返回信息");
        	//TODO
        }

    }

}