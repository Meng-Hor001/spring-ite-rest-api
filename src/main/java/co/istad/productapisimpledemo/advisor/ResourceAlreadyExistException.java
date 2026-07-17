package co.istad.productapisimpledemo.advisor;

// this , this()
// super, super()..

// exception used when resource is already exists
public class ResourceAlreadyExistException extends RuntimeException {
    public ResourceAlreadyExistException(String message) {
        super(message);
    }
}
