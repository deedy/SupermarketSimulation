package HomeworkFinal;
//Empty Queue Exception that is thrown when operations are performed on an empty Queue
public class EmptyQueueException extends RuntimeException { 
	public EmptyQueueException(String err) {
		super(err); 
	}
}