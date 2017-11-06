package frankiwewe.masterWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Frankie Yang on 2017/11/3.
 */
public class FutureTaskTry {

    public void doMyWork(String s) throws ExecutionException, InterruptedException {
        //构造FutureTask
        FutureTask<String> futureTask = new FutureTask<String>(new RealData()) {
            @Override
            protected void done() {
                //FutureTask执行完的回调
                doSomething();
                System.out.println("done...");
            }
        };
        //自定义ExecutorService，我会在后面的文章中总结。
        ExecutorService executor = Executors.newFixedThreadPool(1);
        //在这里执行RealData的call内容
        executor.submit(futureTask);
        try {
            /*** 模拟一段耗时操作  **/
            Thread.sleep(1 * 10);
        } catch (InterruptedException e) {
        }

        //阻塞，一直等到RealData的call方法执行完毕,并返回泛型结果。
//        this.doMyWork(futureTask.get());
        String str = futureTask.get();
        System.out.println("Result" + str);
        executor.shutdown();
    }


    public void doMultiMyWork(String s, int count) throws ExecutionException, InterruptedException {
        String rslt = "";
        List<FutureTask> futureTasks = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(count);
        for (int i = 0; i < count; i++){
            FutureTask futureTask = new FutureTask<String>(new RealData("Task" + i)){
                @Override
                protected void done() {
                    doSomething();
                    System.out.println("done...");
                }
            };
            executor.submit(futureTask, rslt);
            futureTasks.add(futureTask);
            System.out.println("Result" + rslt);
        }

        for (FutureTask task: futureTasks) {
            System.out.println("Result" + task.get());
        }

        executor.shutdown();
    }

    private void doSomething() {
    /*执行回调结果*/
    }

    public static void main(String[] args){
        FutureTaskTry fu = new FutureTaskTry();
        try {
            System.out.println("Starting...");
//            fu.doMyWork("frankie");
            fu.doMultiMyWork("frankie", 10);
        } catch (InterruptedException ie){

        } catch (ExecutionException ee){

        }

        /*RealData rd = new RealData();
        try {
            rd.call();
        } catch (Exception e){}*/
    }

}


class RealData implements Callable<String> {

    private String flag = "";

    public RealData() {
        System.out.println("RealData construct");
    }

    public RealData(String flag) {
        this.flag = flag;
        System.out.println(this.flag + "RealData construct");
    }

    @Override
    public String call() throws Exception {
        //这里是真实的业务逻辑，耗时很长
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            System.out.println(String.format(this.flag + "RD call %d...", i) );
            stringBuffer.append(i);
            //模拟一段耗时操作
//            SystemClock.sleep(1 * 1000);
            Thread.sleep(1 * 1000);
        }
        return stringBuffer.toString();
    }
}

class StaticSingleton {
    private StaticSingleton() {
     /*单例对象被创建*/
    }

    private static class SingletonHolder {
        private static StaticSingleton instance = new StaticSingleton();
    }

    public static StaticSingleton getInstance() {
        return SingletonHolder.instance;
    }
}

