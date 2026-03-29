package com.back.services;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    public void addHttpOnlyCookie(String name, String value,
                                  int maxAge, HttpServletResponse response){

        Cookie cookie = new Cookie(name,value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(maxAge);

        response.addCookie(cookie);

    }

    public void removeHttpOnlyCookie(String name, HttpServletResponse response){

        //Basicamente sobreescribe la actual a null
        Cookie cookie = new Cookie(name,null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);

        response.addCookie(cookie);

    }


}
