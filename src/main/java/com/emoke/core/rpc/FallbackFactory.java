package com.emoke.core.rpc;

import com.emoke.core.rpc.exception.RpcException;

public interface FallbackFactory<T> {
    T onException(RpcException e);
}
