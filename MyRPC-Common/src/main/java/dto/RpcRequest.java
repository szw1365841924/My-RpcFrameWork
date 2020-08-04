package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Builder 建造者模式
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RpcRequest implements Serializable {

    private String interfacename;
    private String methodname;
    private Object[] parameters;
    private Class<?>[] paramTypes;
}
