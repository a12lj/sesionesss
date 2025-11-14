<%@ page contentType="text/html; charset=UTF-8" language="java" import="modelos.*" %>
<%@ page import="modelos.ItemCarro" %>
<%@ page import="modelos.DetalleCarro" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="com.itextpdf.text.Document" %>
<%@ page import="com.itextpdf.text.DocumentException" %>
<%@ page import="com.itextpdf.text.Paragraph" %>
<%@ page import="com.itextpdf.text.pdf.PdfWriter" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="java.io.IOException" %>

<!-- Estilos CSS -->
<style>
    body {
        font-family: 'Arial', sans-serif;
        background-color: #f3f4f6;
        color: #333;
        margin: 0;
        padding: 0;
        display: flex;
        justify-content: center;
        align-items: flex-start;
        height: 100vh;
        flex-direction: column;
        padding-top: 40px;
    }

    h1 {
        color: #6f42c1;
        font-size: 2rem;
        margin-bottom: 20px;
    }

    table {
        width: 80%;
        margin: 20px 0;
        border-collapse: collapse;
        background-color: #ffffff;
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
        border-radius: 8px;
        overflow: hidden;
    }

    table, th, td {
        border: 1px solid #ddd;
    }

    th, td {
        padding: 12px;
        text-align: left;
    }

    th {
        background-color: #6f42c1;
        color: white;
    }

    td {
        background-color: #f9f9f9;
    }

    tr:hover {
        background-color: #f1f1f1;
    }

    button {
        background-color: #6f42c1;
        color: white;
        border: none;
        padding: 12px 24px;
        font-size: 1rem;
        cursor: pointer;
        border-radius: 5px;
        transition: background-color 0.3s;
        display: inline-block;
    }

    button:hover {
        background-color: #5a32a3;
    }

    p, a {
        font-size: 1rem;
    }

    a {
        color: #6f42c1;
        text-decoration: none;
        margin-top: 10px;
        display: inline-block;
    }

    a:hover {
        text-decoration: underline;
    }

    .back-link {
        margin-top: 20px;
        font-size: 1.2rem;
    }
</style>

<%
    // Definición del formateador de decimales (Ejemplo: 1500.00)
    DecimalFormat df = new DecimalFormat("#,##0.00");

    // 1. Obtener el detalle del carro
    DetalleCarro detalleCarro = (DetalleCarro) session.getAttribute("carro");

    if (detalleCarro == null || detalleCarro.getItem().isEmpty()) {
%>
<p>Lo sentimos, no hay productos en el carro de compras!</p>
<%
} else {
    double total = detalleCarro.getTotal();
    double iva = detalleCarro.getIva();
    double totalConIva = detalleCarro.getTotalConIva();
%>
<h1>Carro de Compras</h1>
<table>
    <tr>
        <th>Id Producto</th>
        <th>Nombre</th>
        <th>Precio</th>
        <th>Cantidad</th>
        <th>Subtotal</th>
    </tr>
    <%
        for (ItemCarro item : detalleCarro.getItem()) {
    %>
    <tr>
        <td><%= item.getProducto().getId() %></td>
        <td><%= item.getProducto().getNombre() %></td>
        <td><%= df.format(item.getProducto().getPrecio()) %></td>
        <td><%= item.getCantidad() %></td>
        <td><%= df.format(item.getSubtotal()) %></td>
    </tr>
    <%
        }
    %>
    <tr>
        <td colspan="4" style="text-align: right; font-weight: bold;">Total:</td>
        <td><%= df.format(total) %></td>
    </tr>
    <tr>
        <td colspan="4" style="text-align: right; font-weight: bold;">IVA (15%):</td>
        <td><%= df.format(iva) %></td>
    </tr>
    <tr>
        <td colspan="4" style="text-align: right; font-weight: bold;">Total con IVA:</td>
        <td><%= df.format(totalConIva) %></td>
    </tr>
</table>

<!-- Botón para generar y descargar el PDF -->
<form action="<%=request.getContextPath()%>/carro.jsp" method="post">
    <button type="submit" name="accion" value="descargar-pdf">Descargar Factura en PDF</button>
</form>

<%
    // Verificar si se debe generar el PDF al hacer clic en el botón
    String accion = request.getParameter("accion");
    if ("descargar-pdf".equals(accion)) {
        // Establecer tipo de contenido como PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=factura_carrito.pdf");

        // Crear el documento PDF
        Document document = new Document();
        OutputStream outputStream = null;
        try {
            // Obtener el flujo de salida
            outputStream = response.getOutputStream();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Agregar contenido al PDF
            document.add(new Paragraph("Factura de Compra"));
            document.add(new Paragraph("Fecha: " + java.time.LocalDate.now()));
            document.add(new Paragraph("--------------------------------------------------"));
            document.add(new Paragraph("Productos comprados:"));

            // Detalles de los productos
            for (ItemCarro item : detalleCarro.getItem()) {
                document.add(new Paragraph(item.getProducto().getNombre() +
                        " - Cantidad: " + item.getCantidad() +
                        " - Subtotal: " + df.format(item.getSubtotal())));
            }

            // Resumen de la compra
            document.add(new Paragraph("--------------------------------------------------"));
            document.add(new Paragraph("Total: " + df.format(total)));
            document.add(new Paragraph("IVA (15%): " + df.format(iva)));
            document.add(new Paragraph("Total con IVA: " + df.format(totalConIva)));
            document.add(new Paragraph("--------------------------------------------------"));
            document.add(new Paragraph("Gracias por su compra!"));

        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            // Asegurarse de cerrar el documento y el flujo de salida
            if (document.isOpen()) {
                document.close();
            }
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
%>

<%
    }
%>

<p class="back-link"><a href="<%=request.getContextPath()%>/productos">SEGUIR COMPRANDO</a></p>
<p class="back-link"><a href="<%=request.getContextPath()%>/Index.html">Volver</a></p>



