package kr.co.glog.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class EntityModel extends BaseModel {

    Timestamp   insertTime;
    Timestamp	updateTime;
    Long		insertMemberId;
    Long		updateMemberId;
    String		remoteIp;

    public void clear() {
        this.insertTime = null;
        this.updateTime = null;
        this.insertMemberId = null;
        this.updateMemberId = null;
        this.remoteIp = null;
    }
}
