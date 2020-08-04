package rpc;


import dto.RpcRequest;

public interface RpcClient {

    Object sendRPCRequest(RpcRequest rpcRequest);
}
