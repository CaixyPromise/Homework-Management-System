package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 */
@Getter
@Setter
@TableName("student_file")
@ApiModel(value = "StudentFile对象", description = "")
public class StudentFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("学生姓名")
    private String student;

    @ApiModelProperty("老师姓名")
    private String dwc;

    @ApiModelProperty("作业标题")
    private String title;

    @ApiModelProperty("作答内容")
    private String an1;

    @ApiModelProperty("未批阅/已批阅")
    private Boolean flag;

    @ApiModelProperty("文件链接")
    private String url;

    @ApiModelProperty("md5")
    private String md5;

    @ApiModelProperty("文件名称")
    private String name;
    @ApiModelProperty("文件内容")
    private String content;

}
