package com.nano.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ImageController {
	@RequestMapping(value = "/image", method = RequestMethod.GET)
	public void image(@RequestParam String path, HttpServletResponse response)
			throws Exception {
		File file = new File(path);
		if (!(file.exists() && file.canRead())) {
			throw new Exception("未找到图片");
		}

		FileInputStream inputStream = new FileInputStream(file);

		response.setContentType("image/png;image/jpeg");

		OutputStream stream = response.getOutputStream();
		byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
        	stream.write(buffer, 0, len);
        }
		inputStream.close();
		stream.flush();
		stream.close();
	}
}
