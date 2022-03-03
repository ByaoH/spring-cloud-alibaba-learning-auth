// package com.byaoh.cloud.auth.exception;
//
// import com.alibaba.fastjson.JSON;
// import com.l.result.Result;
// import com.l.result.ResultCode;
// import com.l.result.ResultFactory;
// import org.springframework.security.access.AccessDeniedException;
// import org.springframework.security.web.access.AccessDeniedHandler;
//
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.io.IOException;
//
// /**
//  * AccessDeineHandler 用来解决认证过的用户访问无权限资源时的异常
//  *
//  * @author l
//  */
// public class JwtAccessDeniedHandler implements AccessDeniedHandler {
// 	/**
// 	 * 当用户尝试访问需要权限才能的REST资源而权限不足的时候，
// 	 * 将调用此方法发送403响应以及错误信息
// 	 */
// 	@Override
// 	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
// 		Result<String> stringResult = ResultFactory.buildResult(ResultCode.FORBIDDEN, "权限不足"
// 			, accessDeniedException.getMessage());
// 		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
// 		response.setContentType("application/json; charset=UTF-8");
// 		response.getWriter().write(JSON.toJSONString(stringResult));
// 	}
// }
