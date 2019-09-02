package com.x.sdk.test.appserver;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.x.base.vo.BaseResponse;

@Path("/dubboSVssss")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public interface IDubboSV {
	
	@POST
	@Path("/testCall")
	public BaseResponse testCall();
	
}
