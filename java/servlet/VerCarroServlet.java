    package servlet;

    import jakarta.servlet.ServletException;
    import jakarta.servlet.annotation.WebServlet;
    import jakarta.servlet.http.HttpServlet;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import jakarta.servlet.http.HttpSession;
    import modelos.DetalleCarro;
    import modelos.ItemCarro;
    import java.io.IOException;
    import java.text.DecimalFormat;

    @WebServlet("/ver-carro")
    public class VerCarroServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            HttpSession session = req.getSession();
            DetalleCarro detalleCarro = (DetalleCarro) session.getAttribute("carro");

            String accion = req.getParameter("accion");

            // Si el carrito está vacío o no hay acción para generar factura
            if (detalleCarro == null || detalleCarro.getItem().isEmpty()) {
                req.setAttribute("mensaje", "Lo sentimos, no hay productos en el carro de compras.");
                getServletContext().getRequestDispatcher("/carro.jsp").forward(req, resp);
                return;
            }

            // Si no hay PDF a generar, simplemente se muestra el carro
            if (!"generarFactura".equals(accion)) {
                // Cálculos del carro
                DecimalFormat df = new DecimalFormat("#,##0.00");
                double total = detalleCarro.getTotal();
                double iva = detalleCarro.getIva();
                double totalConIva = detalleCarro.getTotalConIva();

                // Pasar los valores de la compra a la vista (JSP)
                req.setAttribute("detalleCarro", detalleCarro);
                req.setAttribute("total", df.format(total));
                req.setAttribute("iva", df.format(iva));
                req.setAttribute("totalConIva", df.format(totalConIva));

                // Redirigir al JSP para mostrar el carro con los productos
                getServletContext().getRequestDispatcher("/carro.jsp").forward(req, resp);
            }
        }
    }


