package dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Builder 建造者模式
 */
@Data
@Builder
public class RpcRequest implements Serializable {

    private String interfacename;
    private String methodname;
    private Object[] parameters;
    private Class<?>[] paramTypes;
}
