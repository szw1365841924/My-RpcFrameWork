package dto;

import enumeration.RpcResponseCode;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RpcResponse<T> implements Serializable {
    private Integer statuscode;
    private String message;
    private T result;
    
    public static <T> RpcResponse success(T result){
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatuscode(RpcResponseCode.SUCCESS.getCode());
        response.setMessage(RpcResponseCode.SUCCESS.getMessage());
        response.setResult(result);
        return response;
    }
    
    public static <T> RpcResponse fail(T result){
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatuscode(RpcResponseCode.FAIL.getCode());
        response.setMessage(RpcResponseCode.FAIL.getMessage());
        response.setResult(result);
        return response;
    }
}
