package kr.co.glog.domain.system.dao;

import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.system.mapper.SystemConfigMapper;
import kr.co.glog.domain.system.model.SystemConfigParam;
import kr.co.glog.domain.system.model.SystemConfigResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Slf4j
@Repository
@RequiredArgsConstructor
public class SystemConfigDao {

    private final SystemConfigMapper systemConfigMapper;


    public SystemConfigResult getSystemConfig(String configCode ) {
        if ( configCode == null ) throw new ParameterMissingException( "configCode" );

        SystemConfigResult systemConfigResult = null;
        SystemConfigParam systemConfigParam = new SystemConfigParam();
        systemConfigParam.setConfigCode( configCode );
        ArrayList<SystemConfigResult> systemConfigList = systemConfigMapper.selectSystemConfigList( systemConfigParam );
        if ( systemConfigList != null && systemConfigList.size() > 0 ) systemConfigResult = systemConfigList.get(0);
        return systemConfigResult;
    }


}
