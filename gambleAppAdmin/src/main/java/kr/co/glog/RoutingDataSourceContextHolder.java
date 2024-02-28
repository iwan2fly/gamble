package kr.co.glog;

import kr.co.glog.common.DataSourceEnum;

public class RoutingDataSourceContextHolder {

    private static ThreadLocal<DataSourceEnum> threadLocal = new ThreadLocal<>();

    public static void set(DataSourceEnum clientDatabase) {
        threadLocal.set(clientDatabase);
    }

    public static DataSourceEnum getClientDatabase() {
        return threadLocal.get();
    }

    public static void clear() {
        threadLocal.remove();
    }
}
