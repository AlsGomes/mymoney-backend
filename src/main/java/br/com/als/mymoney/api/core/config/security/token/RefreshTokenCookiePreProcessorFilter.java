package br.com.als.mymoney.api.core.config.security.token;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RefreshTokenCookiePreProcessorFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
                HttpServletRequest req = (HttpServletRequest) request;
                HttpServletResponse res = (HttpServletResponse) response;

                if ("/oauth/token".equalsIgnoreCase(req.getRequestURI())) {              
                	 res.setHeader("Access-Control-Allow-Origin", "*");
                     res.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
                     res.setHeader("Access-Control-Allow-Headers", "*");
//                     res.setHeader("Access-Control-Max-Age", "3600");
                     if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) req).getMethod())) {
                         res.setStatus(HttpServletResponse.SC_OK);
                     }
                }
                
                if ("/oauth/token".equalsIgnoreCase(req.getRequestURI())
                		&& "refresh_token".equals(req.getParameter("grant_type"))
                		&& req.getCookies() != null) {
                	
                	String refreshToken = 
                			Stream.of(req.getCookies())
                			.filter(cookie -> "refreshToken".equals(cookie.getName()))
                			.findFirst()
                			.map(cookie -> cookie.getValue())
                			.orElse(null);
                	
                	req = new MyServletRequestWrapper(req, refreshToken);
                }
                              
                chain.doFilter(req, res);        
    }
    
    static class MyServletRequestWrapper extends HttpServletRequestWrapper {

		private String refreshToken;
		
		public MyServletRequestWrapper(HttpServletRequest request, String refreshToken) {
			super(request);
			this.refreshToken = refreshToken;
		}
		
		@Override
		public Map<String, String[]> getParameterMap() {
			ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
			map.put("refresh_token", new String[] { refreshToken });
			map.setLocked(true);
			return map;
		}
		
	}
}