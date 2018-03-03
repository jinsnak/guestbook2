package kr.co.dhflour.guestbook.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.dhflour.guestbook.dao.GuestBookDao;
import kr.co.dhflour.guestbook.vo.GuestBookVo;

@WebServlet("/gb")
public class GuestBookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("a");
		
		if("del_form".equals(action)) {
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/deleteform.jsp"); //분기만 받아옴?
			rd.forward(request, response); //포워딩(servlet에서 jsp로 이동시키는 것)
			
		} else if ("add".equals(action)) {
			
			String name = request.getParameter("name");
			String password = request.getParameter("pass");
			String contents = request.getParameter("content");
			
			GuestBookVo vo = new GuestBookVo();
			vo.setName(name);
			vo.setPassword(password);
			vo.setContents(contents);
			
			GuestBookDao dao = new GuestBookDao();
			boolean result = dao.insertVo(vo);
			
			if (result) {
				response.sendRedirect("/guestbook2/gb");
			}
			
			
			
		} else if ("del".equals(action)) {
			
			String no = request.getParameter("no");
			String password = request.getParameter("password");
			
			GuestBookVo vo = new GuestBookVo();
			vo.setNo(Long.parseLong(no));
			vo.setPassword(password);
			
			GuestBookDao dao = new GuestBookDao();
			boolean result = dao.deleteVo(vo);
			
			if(result) {
				response.sendRedirect("/guestbook2/gb");
			}
			
		} else {
			
			GuestBookDao dao = new GuestBookDao();
			List<GuestBookVo> list = dao.fetchList();
			
			request.setAttribute("list", list); //list 라는 이름으로 object를 던져줌.
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/index.jsp"); //분기만 받아옴?
			rd.forward(request, response); //포워딩(servlet에서 jsp로 이동시키는 것)
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
