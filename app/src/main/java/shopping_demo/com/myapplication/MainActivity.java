package shopping_demo.com.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "Main_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.simple_id:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        simpleSendRetrofitRequest();
                    }
                }).start();
                break;
            case R.id.rx_id:
                SendRxJavaRetrofitRequest();
                break;
            default:
                break;
        }

    }

    private class JavaDemo<T> {
        public JavaDemo() {
        }


        public List<T> list1;
        public List<T> list2;


        private List<?> getList1() {
            return new ArrayList();
        }

        private List<?> getList2() {
            return new ArrayList<Object>();
        }
    }

    private void simpleSendRetrofitRequest() {
        Retrofit retrofit = new Retrofit.Builder()
            //这里建议：- Base URL: 总是以/结尾；- @Url: 不要以/开头
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        SimpleApiStores apiStores = retrofit.create(SimpleApiStores.class);
        Call<GitLoginData> call = apiStores.getLoginName("simonws");

        try {
            Response<GitLoginData> bodyResponse = call.execute();
            GitLoginData body = bodyResponse.body();//获取返回体的字符串
            Log.d(TAG, "retrofit body=" + body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void SendRxJavaRetrofitRequest() {
        Retrofit retrofit = new Retrofit.Builder()
            //这里建议：- Base URL: 总是以/结尾；- @Url: 不要以/开头
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
        RxJavaApiStores apiStores = retrofit.create(RxJavaApiStores.class);
        Observable<GitLoginData> observable = apiStores.getLoginName("simonws");

        observable.subscribeOn(Schedulers.io())//请求数据的事件发生在io线程
            .observeOn(AndroidSchedulers.mainThread())//请求完成后在主线程更显UI
            .subscribe(new Observer<GitLoginData>() {//订阅
                @Override
                public void onCompleted() {
                    //所有事件都完成，可以做些操作。。。
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace(); //请求过程中发生错误
                }

                @Override
                public void onNext(GitLoginData data) { //这里的book就是我们请求接口返回的实体类
                    Log.d(TAG, "onNext " + data);
                }
            });

    }
}
