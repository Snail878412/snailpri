package com.snail.ssip.controllers.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * <p>
 * Title: com.snail.ssip.controllers.common.DefaultController
 * </p>
 * 
 * <p>
 * Description: 默认控制器.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年9月9日
 * 
 * @version 1.0
 * 
 */
@Controller
@RequestMapping("/")
public class DefaultController {

	@RequestMapping("/pages/{fileName}")
	public String privatePages(@PathVariable() String fileName) {
		String[] fileDirs = fileName.split("_");
		StringBuffer modelUrl = new StringBuffer();
		for (String fileDir : fileDirs) {
			modelUrl.append("/");
			modelUrl.append(fileDir);
		}
		return modelUrl.toString();
	}
}
