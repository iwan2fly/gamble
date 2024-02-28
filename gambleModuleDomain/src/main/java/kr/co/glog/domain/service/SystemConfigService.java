package kr.co.glog.domain.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.glog.domain.system.code.ConfigCode;
import kr.co.glog.domain.system.dao.SystemConfigDao;
import kr.co.glog.domain.system.model.SystemConfigParam;
import kr.co.glog.domain.system.model.SystemConfigResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 *  주식 관련 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemConfigService {

    private final SystemConfigDao systemConfigDao;


    public Map<String, Object> getConfigFinancialQuarter() throws JsonProcessingException {

        SystemConfigResult systemConfigResult = systemConfigDao.getSystemConfig( ConfigCode.configFinancialQuarter );
        ObjectMapper objectMapper = new ObjectMapper();
        String json = systemConfigResult.getConfigJson();
        log.debug( json );
        Map<String, Object> map = objectMapper.readValue(json, new TypeReference<>() {});

        return map;
    }
}
