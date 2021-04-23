package com.github.pettyfer.caas.framework.system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Pettyfer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserMessage implements Serializable {
    private static final long serialVersionUID = 9053803488097823142L;
    private String message;
    private String time;
    private String deliver;
    private String content;
    private String id;
    private Integer type;
    private Integer level;
    private String avatar;
    private String businessName;
    private String businessId;
    private String businessPageAddress;
    private String editPageAddress;
    private String viewPageAddress;
}
