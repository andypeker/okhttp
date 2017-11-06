package frankiwewe.masterWorker;

import java.util.Map;

/**
 * Created by Frankie Yang on 2017/11/3.
 */
public class Frankie {

    /**
     * 计算立方和1~100的立方和
     */
    public void executeCubic() {

        //创建三个Worker线程，执行任务
        Master master = new Master(new Worker(), 5);

        for (int i = 0; i < 100; i++) {
            master.submit(i);
        }

        //执行任务
        master.execute();
        //计算结果，初始化
        int result = 0;

        //计算结果集
        Map<String, Object> resultMap = master.getResultMap();

        while (resultMap.size() > 0 || !master.isComplete()) {
//            System.out.println("Result-While");
            String key = null;
            Integer i = null;
            String rslt = null;

            for (String k : resultMap.keySet()) {
                key = k;
                break;
            }

            if (key != null) {
//                i = resultMap.get(key);
                rslt = String.valueOf(resultMap.get(key));
                //从结果集中移除倍计算过的key
                resultMap.remove(key);
                System.out.println("Result: " + rslt);
            }

            if (i != null) {
                result += i;
            }
        }
    }


    public static void main(String[] args){
        Frankie  fr = new Frankie();
        fr.executeCubic();
    }
}
