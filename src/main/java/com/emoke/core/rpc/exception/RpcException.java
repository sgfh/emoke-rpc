package com.emoke.core.rpc.exception;

public class RpcException  {
    public RpcException(Type type){
        this.type=type;
    }
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type{
        ConnectionException,ConnectTimeoutException
    }
    private Type type;
}
