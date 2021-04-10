package com.github.pettyfer.caas.global.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Petty
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseEntity<T> extends Model<BaseEntity<T>> {

    private static final long serialVersionUID = 1602376530658930002L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableLogic
    private Integer delFlag;

    private String creator;

    private LocalDateTime createTime;

    private String modifier;

    private LocalDateTime modifyTime;

}
