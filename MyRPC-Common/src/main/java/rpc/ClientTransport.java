package rpc;


import dto.RpcRequest;

public interface ClientTransport {

    Object sendRPCRequest(RpcRequest rpcRequest);
}
