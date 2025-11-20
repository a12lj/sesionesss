package services;

public class Exception extends RuntimeException{
    public Exception(String mensaje){
        super(mensaje);
    }
    public Exception(String mensaje,Throwable cause){
        super(cause);
    }
}
