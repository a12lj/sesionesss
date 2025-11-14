
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="estilos.css">
    <link rel="stylesheet" href="Loginestilos.css">
</head>
<body>
<div class="container">
    <div class="header">
        <h2>Iniciar sesión</h2>
        <span class="badge">Acceso</span>
    </div>
    <form method="post" action="login">
        <label>Usuario
            <input type="text" name="username" required autocomplete="username">
        </label>
        <label>Contraseña
            <input type="password" name="password" required autocomplete="current-password">
        </label>
        <div class="actions">
            <button type="submit">Entrar</button>
            <a class="button secondary" href="Index.html">Volver</a>
        </div>
    </form>
    <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
    <p class="alert"><%= error %></p>
    <%
        }
    %>
</div>
</body>
</html>