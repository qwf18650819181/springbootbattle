package pactera.tf.chat.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;


public class WebFilter extends OncePerRequestFilter{
	
	private static final Logger log = LoggerFactory.getLogger(WebFilter.class);
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		/*
         * 跨域问题的设置
         */
        String option = "OPTIONS";
        if (option.equals(request.getMethod())) {
            log.info("浏览器的预请求的处理..");
            response.setContentType("application/json; charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Access-Control-Allow-Origin",request.getHeader("origin"));
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET,PUT, OPTIONS, DELETE,HEAD");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, Accept, token,Origin, No-Cache, X-Requested-With, If-Modified-Since,authorization,Pragma, Last-Modified, Cache-Control, Expires, Authorization,Token");
            return;
        } else {
            String requestURI = request.getRequestURI();
            log.info("requestURI:{}", requestURI);
        }
	}

}
