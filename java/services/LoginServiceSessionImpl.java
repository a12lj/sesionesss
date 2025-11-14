package services;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

public class LoginServiceSessionImpl implements LoginService {

    @Override
    public Optional<String> getUsername(HttpServletRequest request) {
        // Obtiene la sesión. Si no existe, no la crea (false).
        HttpSession session = request.getSession();

        String username = (String) session.getAttribute("username");

        if (username != null) {
            return Optional.of(username);
        }
        return Optional.empty();
    }
}