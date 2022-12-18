package com.purdue.priceanalysis.security;

import com.purdue.priceanalysis.common.enums.ResponseCode;
import com.purdue.priceanalysis.common.util.ResponseUtil;
import com.purdue.priceanalysis.model.payload.ApiResponse;
import com.purdue.priceanalysis.service.impl.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = JwtTokenProvider.getJwtFromRequest(request);

//            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            if (StringUtils.hasText(jwt)) {
                if(JwtTokenProvider.isBlackList(jwt)) {
                    ResponseUtil.responseJson(response, new ApiResponse(ResponseCode.UNAUTHORIZED_TOKEN.getCode(), "Token blacklisted"));
                    return;
                }

                if(JwtTokenProvider.hasToken(jwt)) {
                    String expiration = JwtTokenProvider.getExpirationByToken(jwt);
                    if (JwtTokenProvider.isExpiration(expiration)) {
                        ResponseUtil.responseJson(response, new ApiResponse(ResponseCode.TOKEN_EXPIRED.getCode(), "Token blacklisted"));
                        return;
                    }
                } else {
                    ResponseUtil.responseJson(response, new ApiResponse(ResponseCode.UNAUTHORIZED_TOKEN.getCode(), "Invalid Token"));
                    return;
                }

                Long userId = Long.parseLong(tokenProvider.getUserIdFromJWT(jwt));

                UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                //throw new AuthenticationException("Unauthorized");
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }
}
