package checker;

import dto.RpcRequest;
import dto.RpcResponse;
import enumeration.RpcError;
import enumeration.RpcResponseCode;
import exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcMessageChecker {
    
    private static final Logger logger = LoggerFactory.getLogger(RpcMessageChecker.class);
    private static final String INTERFACE_NAME = "interfaceName";
    
    private RpcMessageChecker(){
        
    }
    
    public static void check(RpcRequest request, RpcResponse response){
        if(response == null){
            logger.error("调用服务失败,serviceName:{}", request.getInterfacename());
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + request.getInterfacename());
        }
        
        if (!request.getRequestId().equals(response.getRequestId())) {
            throw new RpcException(RpcError.REQUEST_NOT_MATCH_RESPONSE, INTERFACE_NAME + ":" + request.getInterfacename());
        }

        if (response.getStatuscode() == null || !response.getStatuscode().equals(RpcResponseCode.SUCCESS.getCode())) {
            logger.error("调用服务失败,serviceName:{},RpcResponse:{}", request.getInterfacename(), response);
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + request.getInterfacename());
        }
    }
    
}
