package rpc.framework.transport.socket.client;

import checker.RpcMessageChecker;
import dto.RpcRequest;
import dto.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.ClientTransport;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class SocketClientTransport implements ClientTransport {
    private static final Logger logger = LoggerFactory.getLogger(SocketClientTransport.class);
    
    private String host;
    private int port;
    
    public SocketClientTransport(String host, int port){
        this.host = host;
        this.port = port;
    }
    
    @Override
    public Object sendRPCRequest(RpcRequest request){
        try(Socket socket = new Socket(this.host, this.port)){
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(request);
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            RpcResponse response = (RpcResponse) inputStream.readObject();
            RpcMessageChecker.check(request, response);
            return response; 
        }catch (Exception e){
            logger.error("发生错误: ", e);
        }
        return null;
    }

}
