package bus.trip.service.dtos;

public class ApiResponse<T> {

    private String message;
    private boolean success;
    private T data;

    public ApiResponse(String message, boolean success, T data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(message, true, data);
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(message, false, data);
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { success = success; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
