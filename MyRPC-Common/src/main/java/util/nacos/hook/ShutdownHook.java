package util.nacos.hook;

import util.concurrent.ThreadPoolUtils;
import util.nacos.NacosUtils;

public class ShutdownHook {

    private static final ShutdownHook hook = new ShutdownHook();
    
    private ShutdownHook(){
        
    }
    
    public static ShutdownHook getShutDownHook(){
        return hook;
    }
    
    public void clearAllHook(){
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NacosUtils.clearRegistry();
            ThreadPoolUtils.shutDownAllThreadPool();
        }));
    }
}
