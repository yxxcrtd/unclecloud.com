package com.unclecloud.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "t_instance")
public class Instance {

    private String id;

    private String userId;

    private String name;

    private int serverType;

    private int os;

    private String intranet;

    private String internet;

    private String port;

    private String account;

    private String password;

    private String firewallId;

    private int status;

    private Date createTime = new Date();

}
