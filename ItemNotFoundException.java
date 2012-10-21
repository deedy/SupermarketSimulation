package HomeworkFinal;

public class ItemNotFoundException extends RuntimeException {
    String msg;
    public ItemNotFoundException() {
        this(null);
    }
    public ItemNotFoundException(String s) {
        msg = s;
    }
}
