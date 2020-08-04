package enumeration;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RpcError {

    SERVICE_INVOCATION_FAILURE("服务调用失败"),
    SERVICE_CAN_NOT_BE_NULL("注册的服务不能为空"),
    SERVICE_NOT_FOUND("找不到对应服务"),
    SERVICE_NOT_IMPLAMENT_ANY_INTERFACE("注册的服务没有实现任何接口"),
    SERIALIZE_FAILED("序列化失败"),
    DESERIALIZE_FAILED("反序列化失败");
    
    private final String message;
}
