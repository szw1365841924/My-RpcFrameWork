package rpc.framework.transport.socket.client;

import dto.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.RpcClient;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class RpcSocketClient implements RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(RpcSocketClient.class);
    
    private String host;
    private int port;
    
    public RpcSocketClient(String host, int port){
        this.host = host;
        this.port = port;
    }
    
    @Override
    public Object sendRPCRequest(RpcRequest request){
        try(Socket socket = new Socket(this.host, this.port)){
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(request);
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            return inputStream.readObject();
        }catch (Exception e){
            logger.error("发生错误: ", e);
        }
        return null;
    }

}
