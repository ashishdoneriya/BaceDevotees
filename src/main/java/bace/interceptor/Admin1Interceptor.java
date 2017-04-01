package bace.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class Admin1Interceptor extends HandlerInterceptorAdapter {
	
	 @Override
	    public boolean preHandle(HttpServletRequest request,
	            HttpServletResponse response, Object handler) throws Exception {
		 String password = (String) request.getSession().getAttribute("password");
		 if (password != null) {
			 return true;
		 } else {
			 response.sendRedirect("/");
			 return false;
		 }
		 
	 }

}
