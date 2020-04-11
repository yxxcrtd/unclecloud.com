package com.unclecloud.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "t_instance")
public class Instance implements Serializable {

    @Id
    private String id;
    
    private String userId;
    
    private String name;
    
    private Integer serverType;
    
    private Integer os;
    
    private String intranet;
    
    private String internet;
    
    private String port;
    
    private String account;
    
    private String password;
    
    private String firewallId;
    
    private Integer status;
    
    private Date createTime = new Date();

}
