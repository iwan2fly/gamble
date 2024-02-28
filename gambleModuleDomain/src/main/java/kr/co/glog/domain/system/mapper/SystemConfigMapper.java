package kr.co.glog.domain.system.mapper;

import kr.co.glog.domain.system.entity.SystemConfig;
import kr.co.glog.domain.system.model.SystemConfigParam;
import kr.co.glog.domain.system.model.SystemConfigResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface SystemConfigMapper {

    public int insertSystemConfig( SystemConfig stock );									    // 등록
    public int insertsSystemConfig( ArrayList<SystemConfig> stockList );		                // 대량등록
    public int updateSystemConfig( SystemConfig stock );									    // 수정
    public int deleteSystemConfig( SystemConfig stock );									    // 삭제
    public ArrayList<SystemConfigResult> selectSystemConfigList(SystemConfigParam stockParam);		// 목록
    public int selectSystemConfigListCount(SystemConfigParam stockParam);					    // 목록건수
}
