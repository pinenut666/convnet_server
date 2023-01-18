package net.convnet.server.mybatis.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2023-01-17
 */
@TableName("HIBERNATE_SEQUENCES")
public class HibernateSequences implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sequenceName;

    private Integer sequenceNextHiValue;

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public Integer getSequenceNextHiValue() {
        return sequenceNextHiValue;
    }

    public void setSequenceNextHiValue(Integer sequenceNextHiValue) {
        this.sequenceNextHiValue = sequenceNextHiValue;
    }

    @Override
    public String toString() {
        return "HibernateSequences{" +
            "sequenceName = " + sequenceName +
            ", sequenceNextHiValue = " + sequenceNextHiValue +
        "}";
    }
}
