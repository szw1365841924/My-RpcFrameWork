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
    DESERIALIZE_FAILED("反序列化失败"),
    REQUEST_NOT_MATCH_RESPONSE("返回结果错误！请求和返回的相应不匹配"),
    FAILED_TO_CONNECT_TO_SERVICE_REGISTRY("连接注册中心失败"),
    REGISTER_SERVICE_FAILED("服务注册失败"),
    GET_SERVICE_FAILED("获取服务失败"),
    CLIENT_CONNECT_SERVER_FAILURE("客户端连接服务器失败");
    
    private final String message;
}
