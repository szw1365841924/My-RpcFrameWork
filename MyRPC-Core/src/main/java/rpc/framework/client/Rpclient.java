package rpc.framework.client;

import dto.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Rpclient {
    private static final Logger logger = LoggerFactory.getLogger(Rpclient.class);

    public Object sendRPCRequest(RpcRequest request, String host, int port){
        try(Socket socket = new Socket(host, port)){
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
