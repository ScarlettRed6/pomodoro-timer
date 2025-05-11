package com.example.pomodoro_timer.utils;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

public class SingleLiveEvent<T> extends MutableLiveData<T> {

    //Fields
    private final AtomicBoolean pending = new AtomicBoolean(false);

    @MainThread
    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull final Observer<? super T> observer){
        super.observe(owner, t ->{
            if (pending.compareAndSet(true, false)){
                observer.onChanged(t);
            }
        });
    }//End of observe method

    @MainThread
    @Override
    public void setValue(@Nullable T t){
        pending.set(true);
        super.setValue(t);
    }//End of setValue method

    @MainThread
    public void call(){
        setValue(null);
    }//End of call method

}
