package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Administrator
 * @Description
 * @create 2023-08-02 15:18
 */

@Api(tags = "通用相关接口")
@Slf4j
@RestController
@RequestMapping("/admin/common")
public class CommonController {


    @Value("${sto.path}")
    private String BasePath;

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    // todo 文件上传功能叠加在添加菜品之中，但添加失败文件依然成功上传，因为分别是两个独立的接口，后续需要改进
    public Result<String> upload(MultipartFile file){

        String originalFilename = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        String fileName = uuid + suffix;

        File dir = new File(BasePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(BasePath + fileName));
            // todo 图片无法显示，前端只接受字符串类型，此方法并未处理类似阿里云oss的解决方案
            return Result.success("https://i.pximg.net/img-original/img/2023/08/01/03/02/18/110420752_p0.jpg");
        } catch (IOException e) {
            log.error("文件上传失败: {}", e);
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
