package com.example.springboot.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.StudentDTO;
import com.example.springboot.entity.Role;
import com.example.springboot.entity.StudentFile;
import com.example.springboot.entity.User;
import com.example.springboot.entity.UserFile;
import com.example.springboot.mapper.StudentFileMapper;
import com.example.springboot.mapper.UserFileMapper;
import com.example.springboot.service.IStudentFileService;
import com.example.springboot.service.IUserFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 */
@RestController
@RequestMapping("/student-file")
public class StudentFileController {
    @Value("D:/files/")
    private String fileUploadPath;

    @Resource
    private IStudentFileService studentFileService;
    @Resource
    private StudentFileMapper studentFileMapper;



    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize
    ) {
        QueryWrapper<StudentFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(studentFileMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @PostMapping("/upload")
    public Result upload(@RequestParam(defaultValue = "") MultipartFile file , @RequestParam String dwc, @RequestParam String title, @RequestParam String an1,@RequestParam String student,@RequestParam String content) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        // 定义一个文件唯一的标识码
        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;

        File uploadFile = new File(fileUploadPath + fileUUID);
        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()) {
            parentFile.mkdirs();
        }

        String url;
        // 获取文件的md5
        String md5 = SecureUtil.md5(file.getInputStream());
        // 从数据库查询是否存在相同的记录
        StudentFile dbFiles = getFileByMd5(md5);
        if (dbFiles != null) {
            url = dbFiles.getUrl();
        } else {
            // 上传文件到磁盘
            file.transferTo(uploadFile);
            // 数据库若不存在重复文件，则不删除刚才上传的文件
            url = "http://localhost:9090/student-file/" + fileUUID;
        }


        // 存储数据库
        StudentFile saveFile = new StudentFile();
        saveFile.setName(originalFilename);
        saveFile.setDwc(dwc);

        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        saveFile.setTitle(title);
        saveFile.setContent(content);
        saveFile.setAn1(an1);
        saveFile.setStudent(student);
        studentFileMapper.insert(saveFile);
        System.out.println(url);

        return Result.success();
    }
    private StudentFile getFileByMd5(String md5) {
        // 查询文件的md5是否存在
        QueryWrapper<StudentFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<StudentFile> studentFileList = studentFileMapper.selectList(queryWrapper);
        return studentFileList.size() == 0 ? null : studentFileList.get(0);
    }
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        StudentFile studentFile = studentFileMapper.selectById(id);
        studentFileService.removeById(studentFile);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        studentFileService.removeByIds(ids);
        return Result.success();
    }
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        // 根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        // 设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");

        // 读取文件的字节流
        try {
            os.write(FileUtil.readBytes(uploadFile));
        } catch (Exception e) {
            System.err.println("文件下载失败，文件不存在");
        }
        os.flush();
        os.close();
    }
    @PostMapping
    public Result save(@RequestBody StudentDTO studentDTO){
        QueryWrapper<StudentFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student", studentDTO.getStudent());
        queryWrapper.eq("dwc",studentDTO.getDwc());
        queryWrapper.eq("title",studentDTO.getTitle());
        List<StudentFile> list=studentFileService.list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            StudentFile studentFile = list.get(0);
            studentFile.setFlag(true);
            studentFileService.updateById(studentFile);
        }
        return Result.success();
    }
}

