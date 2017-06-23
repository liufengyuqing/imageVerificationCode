package com.tedu.web;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tedu.util.ImageUtil;

/**
 * @author 刘志伟
 * 
 *  2017-6-19
 */
@Controller
public class ActionController {
	// 处理获取验证码请求
	@RequestMapping("/getImage.do")
	//页面请求，生成验证码
	public void getImage(HttpServletRequest request, HttpServletResponse respose) {
		try {
			request.setCharacterEncoding("utf-8");
			respose.setContentType("image/jpeg");
			respose.setHeader("Pragma", "no-cache");// 取消缓存
			respose.setDateHeader("Expires", 0);// 支持http1.1
			respose.setHeader("Cache-Control", "no-cache");
			// 用respose获取一个输出流
			ServletOutputStream sos = respose.getOutputStream();
			//调用工具类获取验证码，然后用输出流给页面写出一个验证码图
			BufferedImage image = ImageUtil.getImage();
			StringBuffer code=ImageUtil.randomCode;//四位验证码
			HttpSession session = request.getSession();         
		    session.setAttribute("validateCode", code.toString());         
		    System.out.println("生成验证码"+code);
			ImageIO.write(image, "jpeg", sos);
			sos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 //@RequestMapping(value="resultServlet/validateCode",method=RequestMethod.POST)
	
	//验证过程
	@RequestMapping("/VerificationServlet.do")
     public void doPost(HttpServletRequest request, HttpServletResponse response)         
             throws ServletException, IOException {         
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		HttpSession session =request.getSession();
		String verificationCode = (String)session.getAttribute("validateCode");
		String checkcode = request.getParameter("op");
		PrintWriter out = response.getWriter();
		if(checkcode.equals(verificationCode)){
			out.println(1);
		}else{
			out.println(0);
		}
		out.flush();
		out.close();
	}
}
