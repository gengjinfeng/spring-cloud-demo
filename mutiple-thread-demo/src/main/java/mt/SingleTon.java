package mt;

import lombok.Synchronized;

public class SingleTon {

    private volatile static  SingleTon object = null;

    private SingleTon(){}

    public static SingleTon getInstance(){
        if(object==null){
            synchronized(SingleTon.class){
                if(object==null){
                    return object;
                }
            }
        }
        return object;
    }
}
