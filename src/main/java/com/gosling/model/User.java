package com.gosling.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "用户表实体类")
public class User implements Serializable{
    private Long id;

    @NotNull(message = "姓名不能为空")
    @ApiModelProperty(value = "姓名 必填项", position = 1)
    private String name;

    @NotNull(message = "年龄不能为空")
    @Length(min = 0, max = 100)
    @ApiModelProperty(value = "年龄 必填项", position = 1)
    private Long age;

    @NotNull(message = "性别不能为空")
    @ApiModelProperty(value = "性别 必填项 男:0 女:1", example = "1",position = 1)
    private Long sex;

    @ApiModelProperty(value = "创建时间 非必填项", example = "1",position = 1)
    private Date creatorDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public Long getSex() {
        return sex;
    }

    public void setSex(Long sex) {
        this.sex = sex;
    }

    public Date getCreatorDate() {
        return creatorDate;
    }

    public void setCreatorDate(Date creatorDate) {
        this.creatorDate = creatorDate;
    }
}
