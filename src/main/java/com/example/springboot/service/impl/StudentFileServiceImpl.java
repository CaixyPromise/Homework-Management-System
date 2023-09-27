package com.example.springboot.service.impl;

import com.example.springboot.entity.StudentFile;
import com.example.springboot.mapper.StudentFileMapper;
import com.example.springboot.service.IStudentFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 */
@Service
public class StudentFileServiceImpl extends ServiceImpl<StudentFileMapper, StudentFile> implements IStudentFileService {

}
