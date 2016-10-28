package com.hangulbot.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AppInterceptor extends HandlerInterceptorAdapter {
    @Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
		Boolean flag = true;
        System.out.println("AppInterceptor.preHandle : "+req.getRequestURI());
        String userRequestUri = req.getRequestURI();

        /*if(userRequestUri.equals("/userInfo")||userRequestUri.startsWith("/userInfoByDeviceId")||
                userRequestUri.equals("/error")||userRequestUri.startsWith("/isDuplicated")||
                userRequestUri.equals("/deviceInfoAndUserInfo")||userRequestUri.startsWith("/childImage")){
            return flag;
        }else{
            final HttpServletRequest request = (HttpServletRequest) req;
            System.out.println();
            Enumeration eHeader = request.getHeaderNames();

            while (eHeader.hasMoreElements()) {
                String hName = (String)eHeader.nextElement();
                String hValue = request.getHeader(hName);

                System.out.println(hName + " : " + hValue);
            }
            final String authHeader = request.getHeader("authorization");
            if (authHeader == null) {
                throw new ServletException("Missing or invalid Authorization header.");
            }

            final String token = authHeader;

            try {
                final Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
                request.setAttribute("claims", claims);
            }
            catch (final SignatureException e) {
                throw new ServletException("Invalid token.");
            }

        }*/
        return flag;
	}
}