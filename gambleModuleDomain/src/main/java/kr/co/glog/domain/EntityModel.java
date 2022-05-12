package kr.co.glog.domain;

import kr.co.glog.domain.BaseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class EntityModel extends BaseModel {

    @Column
    Timestamp   insertTime;

    @Column
    Timestamp	updateTime;

    @Column
    Long		insertMemberId;

    @Column
    Long		updateMemberId;

    @Column
    String		remoteIp;

    public void clear() {
        this.insertTime = null;
        this.updateTime = null;
        this.insertMemberId = null;
        this.updateMemberId = null;
        this.remoteIp = null;
    }
}
