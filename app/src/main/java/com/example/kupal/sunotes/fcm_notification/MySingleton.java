package com.example.kupal.sunotes.fcm_notification;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class MySingleton {
    private static MySingleton mySingleton;
    private static Context mcontext;
    private RequestQueue requestQueue;

    private MySingleton(Context context)
    {
        mcontext=context;
        requestQueue= getRequestQueue();

    }

    private RequestQueue getRequestQueue()
    {

        if(requestQueue==null)
        {
            requestQueue= Volley.newRequestQueue(mcontext.getApplicationContext());
        }
        return  requestQueue;
    }

    public static synchronized MySingleton getInstance(Context context){
        if(mySingleton==null)
        {
            mySingleton=new MySingleton(context);

        }
        return mySingleton;
    }
    public<T> void addtoRequestQue(Request<T> request)
    {

        getRequestQueue().add(request);
    }
}
