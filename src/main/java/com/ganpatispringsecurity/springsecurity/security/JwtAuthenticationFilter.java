package com.ganpatispringsecurity.springsecurity.security;



import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);
    @Autowired
    private JwtHelper jwtHelper;


    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //Authorization
        String requestHeader = request.getHeader("Authorization");
        //Bearer 2352345235sdfrsfgsdfsdf
        logger.info(" Header :  {}", requestHeader);
        String username = null;
        String token = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            //looking good
            //Bearer 2334444ghhvgvhv
            //substring starts with 7th index we will store in token, Because till Bearer and white space 6 index are there
            token = requestHeader.substring(7);
            try {
                username = this.jwtHelper.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                logger.info("Illegal Argument while fetching the username !!");
                e.printStackTrace();
            } catch (ExpiredJwtException e) {
                logger.info("Given jwt token is expired !!");
                e.printStackTrace();
            } catch (MalformedJwtException e) {
                logger.info("Some changed has done in token !! Invalid Token");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();

            }

        } else {
            logger.info("Invalid Header Value !! ");
        }


        //if username found and Security context is null i.e no-one is logged in
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //before setting Authentication we need to validate token, so we need username from this token to validate
            //fetch user detail from username - for this we need to Autowire UserDetailService
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            Boolean validateToken = this.jwtHelper.validateToken(token, userDetails); // this will validate token by comparing username from token and userDetails

            if (validateToken) {
                //set Authentication in security context holder
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.info("Validation fails !!");
            }
        }

        filterChain.doFilter(request, response);
    }
}
