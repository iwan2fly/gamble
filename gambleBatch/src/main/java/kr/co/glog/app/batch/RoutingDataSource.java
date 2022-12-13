package kr.co.glog.app.batch;

import kr.co.glog.common.DataSourceEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Configuration
@EnableTransactionManagement
public class RoutingDataSource extends AbstractRoutingDataSource  {

    @Override
    protected Object determineCurrentLookupKey() {
        // return RoutingDataSourceContextHolder.getClientDatabase();
        return TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? DataSourceEnum.slave : DataSourceEnum.master;

    }

}
