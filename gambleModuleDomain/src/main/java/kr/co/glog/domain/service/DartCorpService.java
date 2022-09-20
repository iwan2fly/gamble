package kr.co.glog.domain.service;


import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stock.dao.DartCorpDao;
import kr.co.glog.domain.stock.entity.DartCorp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 *  주식 관련 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DartCorpService {

    private final DartCorpDao dartCorpDao;


}
