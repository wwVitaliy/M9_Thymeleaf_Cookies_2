package org.example;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.ZoneId;

import static org.example.TimeServlet.TIME_ZONE_PARAM_NAME;

@WebFilter("/time")
public class TimezoneValidateFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (!req.getParameterMap().containsKey(TIME_ZONE_PARAM_NAME)
                || isValid(req.getParameter(TIME_ZONE_PARAM_NAME))) {
            chain.doFilter(req, res);
        } else {
            res.setStatus(400);
            res.setContentType("text/html");
            res.getWriter().write("<h1>Invalid timezone</h1>");
        }
    }

    private boolean isValid(String zoneFromQueryParam) {
        try {
            //Replace is needed to read "+"-sign as a part of query parameter, because it is read as space (" ")
            ZoneId.of(zoneFromQueryParam.replace(" ", "+"));
        } catch (DateTimeException e){
            return false;
        }
        return true;
    }
}
