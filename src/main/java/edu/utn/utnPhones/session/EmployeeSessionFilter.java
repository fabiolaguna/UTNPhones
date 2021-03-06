package edu.utn.utnPhones.session;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static edu.utn.utnPhones.utils.Constants.EMPLOYEE_SESSION;

@Service
@AllArgsConstructor
public class EmployeeSessionFilter extends OncePerRequestFilter {

    @Autowired
    private final SessionManager sessionManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
                                    throws ServletException, IOException {

        String sessionToken = request.getHeader("Authorization");
        Session session = null;
        session = sessionManager.getSession(sessionToken);

        if (null != session) {
            if(EMPLOYEE_SESSION.equals(session.getLoggedUser().getUserType().name()))
                filterChain.doFilter(request, response);
            else
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else
            response.setStatus(HttpStatus.FORBIDDEN.value());
    }
}
