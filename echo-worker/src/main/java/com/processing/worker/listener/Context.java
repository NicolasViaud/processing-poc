package com.processing.worker.listener;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Context {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final static ThreadLocal<Context> threadLocal = ThreadLocal.withInitial(Context::new);

    private Integer totalWorkers;

    private Context() {
    }

    public static Context getContext() {
        return threadLocal.get();
    }

}
