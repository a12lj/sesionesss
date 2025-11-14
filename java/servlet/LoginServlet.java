package servlet;

/*
 * Servlet para el manejo del login y el conteo
 * de cuántas veces el usuario ha iniciado sesión
 * correctamente, aun cuando se cierre la sesión (logout).
 */

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import services.LoginService;
import services.LoginServiceSessionImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet({"/login", "/login.html"})
public class LoginServlet extends HttpServlet {

    // Credenciales estáticas de ejemplo para el login
    final static String USERNAME = "admin";
    final static String PASSWORD = "1234";

    /*
     * Método auxiliar para obtener el contador desde la cookie "loginCounter".
     * Si no existe la cookie, devuelve 0.
     */
    private int obtenerContadorDesdeCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("loginCounter".equals(c.getName())) {
                    try {
                        return Integer.parseInt(c.getValue());
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                }
            }
        }
        return 0;
    }

    /*
     * Método GET:
     * - Si el usuario ya está autenticado (tiene "username" en sesión),
     *   se muestra el mensaje de bienvenida y el número de veces que
     *   ha iniciado sesión (tomado de la cookie).
     * - Si NO está autenticado, se redirige a la página Login.jsp.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Servicio que obtiene el username desde la sesión
        LoginService auth = new LoginServiceSessionImpl();
        Optional<String> usernameOptional = auth.getUsername(req);

        if (usernameOptional.isPresent()) {
            // Obtenemos el valor del contador desde la cookie
            int counter = obtenerContadorDesdeCookie(req);
            String counterText = String.valueOf(counter);

            resp.setContentType("text/html;charset=UTF-8");

            try (PrintWriter out = resp.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("  <meta charset=\"UTF-8\">");
                out.println("  <title>Hola " + usernameOptional.get() + "</title>");
                out.println("<link rel=\"stylesheet\" href=\"" + req.getContextPath() + "/estilos.css\">");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Hola " + usernameOptional.get() + " has iniciado sesión con éxito!</h1>");
                out.println("<p>Veces que has iniciado sesión en este navegador: <strong>" + counterText + "</strong></p>");
                out.println("<p><a href='" + req.getContextPath() + "/Index.html'>Volver</a></p>");
                out.println("<p><a href='" + req.getContextPath() + "/logout'>Cerrar sesión</a></p>");
                out.println("</body>");
                out.println("</html>");
            }
        } else {
            // Si no hay usuario en sesión, enviamos al formulario de Login.jsp
            getServletContext().getRequestDispatcher("/Login.jsp").forward(req, resp);
        }
    }

    /*
     * Método POST:
     * - Procesa el formulario de login.
     * - Valida el usuario y contraseña.
     * - Si son correctos:
     *      -> guarda el username en sesión
     *      -> incrementa el contador en la COOKIE "loginCounter"
     *      -> redirige a /login.html (que cae en el doGet)
     * - Si son incorrectos:
     *      -> devuelve error 401 No autorizado
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Obtenemos los parámetros enviados en el formulario
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // Validamos credenciales simples contra las constantes
        if (USERNAME.equals(username) && PASSWORD.equals(password)) {

            // Obtenemos o creamos la sesión
            HttpSession session = req.getSession();
            // Guardamos el nombre de usuario en la sesión
            session.setAttribute("username", username);

            // =========================================
            // MANEJO DEL CONTADOR EN COOKIE
            // =========================================
            // Leemos el valor actual de la cookie
            int counter = obtenerContadorDesdeCookie(req);

            // Aumentamos el contador porque acaba de iniciar sesión correctamente
            counter++;

            // Creamos/actualizamos la cookie con el nuevo valor
            Cookie loginCounterCookie = new Cookie("loginCounter", String.valueOf(counter));
            // Tiempo de vida de la cookie: 30 días (en segundos)
            loginCounterCookie.setMaxAge(60 * 60 * 24 * 30);
            // Importante: que aplique a todo el contexto de la app
            loginCounterCookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());

            // Agregamos la cookie a la respuesta
            resp.addCookie(loginCounterCookie);

            // Redirigimos al recurso /login.html (método GET)
            resp.sendRedirect(req.getContextPath() + "/login.html");

        } else {
            // Si las credenciales no son válidas, devolvemos un error 401 (No autorizado)
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Lo sentimos, no está autorizado para ingresar a esta página!");
        }
    }
}




