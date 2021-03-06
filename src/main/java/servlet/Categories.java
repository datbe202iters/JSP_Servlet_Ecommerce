package servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CategoryDAOImpl;
import dao.ProductDAOImpl;
import entity.Category;
import entity.Pagination;
import entity.Product;

@WebServlet("/Categories")
public class Categories extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Categories() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		String action = request.getParameter("action");
		CategoryDAOImpl cdao = new CategoryDAOImpl();
		String view ="not-found.jsp";
		switch (action) {
		case "index":
			String key = request.getParameter("key");
			Pagination<Category> pagination;
			String pageNumber = request.getParameter("page");
			int page = pageNumber == null ? 1 : Integer.parseInt(pageNumber);
			int pageSize = 4;
			if(key != null && ! key.isEmpty()) {
				pagination = cdao.getByName(page, pageSize,key);
				request.setAttribute("pagination", pagination);
				request.setAttribute("total", pagination.getTotalPages());
				request.setAttribute("key",key);
			}else {
				pagination = cdao.toPagination(page, pageSize);
				request.setAttribute("pagination", pagination);
				request.setAttribute("total", pagination.getTotalPages());
			}
			view = "admin_view/category/listCat.jsp";
			break;
		case "findId":
			String id = request.getParameter("id");
			Category c = cdao.findId(id);
			List<Category> lstCat= new CategoryDAOImpl().getAll();
			request.setAttribute("lstCat", lstCat);
			request.setAttribute("c", c);
			view = "admin_view/category/edit.jsp";
			break;
		case "viewAdd":
			view = "admin_view/category/add.jsp";
			break;
		case "create":
			boolean check = false;
			String name = null ;
			
			 Pattern ptName = Pattern.compile("^[.[^$(\\)\\[\\]\\{\\}]]{5,32}");
			 Matcher mName = ptName.matcher(request.getParameter("name"));
			 if(request.getParameter("name").isEmpty()) {
				 check = true;
				 request.setAttribute("errName","T??n danh m???c kh??ng ???????c ????? tr???ng !");
			 }else if(!mName.matches()) {
				 check = true;
				 request.setAttribute("errName","T??n danh m???c  ph???i t??? 5-32 k?? t??? !");
			 }else {
				 name = request.getParameter("name");
			 }
			 
			 String Pstatus = request.getParameter("status") ;
			 int status = 0;
			 if(Pstatus == null || Pstatus.length() == 0 || Pstatus.equals(" ")) {
				 check = true;
				 request.setAttribute("errStatus", "Tr???ng th??i danh m???c  kh??ng ???????c ????? tr???ng !");
			 }else {
				  status =  Integer.parseInt(Pstatus);
			 }
			 
			 int parentId = 0;
			 if(request.getParameter("parentId").isEmpty()) {
				 check = true;
				 request.setAttribute("errParentId", "Danh m???c cha kh??ng ???????c ????? tr???ng !");
			 }else {
				 try {
					 int tmpParentId =  Integer.parseInt(request.getParameter("parentId"));
					 if(tmpParentId > 0) {
						 parentId = tmpParentId;
					 }else{
						 request.setAttribute("errParentId", "Danh m???c cha ph???i l???n h??n 0 !");
					 }
				} catch (NumberFormatException e) {
					request.setAttribute("errParentId", "Danh m???c sch ph???i l?? s??? !");
				}
			 }
			 Category cate = new Category();
			 cate.setName(name);
			 cate.setStatus(status);
			 cate.setParentId(parentId);
			 
			 if(cdao.add(cate) != null) {
				 cdao.add(cate);
				 view = "Categories?action=index";
			 }else {
				 request.setAttribute("c", cate); 
				 request.setAttribute("err", "Insert failed. Try Again !");
				 view = "admin_view/category/add.jsp";
			 }
			
			break;
		case "update":
			 int id1 = Integer.parseInt(request.getParameter("id"));
			 
			 boolean _check = false;
			 String _name = null ;
			 Pattern _ptName = Pattern.compile("^[.[^$(\\)\\[\\]\\{\\}]]{5,32}");
			 Matcher _mName = _ptName.matcher(request.getParameter("name"));
			 if(request.getParameter("name").isEmpty()) {
				 _check = true;
				 request.setAttribute("errName","T??n s???n ph???m kh??ng ???????c ????? tr???ng !");
			 }else if(!_mName.matches()) {
				 _check = true;
				 request.setAttribute("errName","T??n s???n ph???m ph???i t??? 5-32 k?? t??? !");
			 }else {
				 _name = request.getParameter("name");
			 }
			 
			 String _Pstatus = request.getParameter("status") ;
			 int _status = 0;
			 if(_Pstatus == null || _Pstatus.length() == 0 || _Pstatus.equals(" ")) {
				 _check = true;
				 request.setAttribute("errStatus", "Tr???ng th??i s???n ph???m kh??ng ???????c ????? tr???ng !");
			 }else {
				  _status =  Integer.parseInt(_Pstatus);
			 }
			 
			 int _parentId = 0;
			 if(request.getParameter("parentId").isEmpty()) {
				 check = true;
				 request.setAttribute("errParentId", "Danh m???c cha kh??ng ???????c ????? tr???ng !");
			 }else {
				 try {
					 int _tmpParentId =  Integer.parseInt(request.getParameter("parentId"));
					 if(_tmpParentId > 0) {
						 _parentId = _tmpParentId;
					 }else{
						 request.setAttribute("errParentId", "Danh m???c cha ph???i l???n h??n 0 !");
					 }
				} catch (NumberFormatException e) {
					request.setAttribute("errParentId", "Danh m???c cha ph???i l?? s??? !");
				}
			 }

			 
			 Category _cat = new Category();
			 _cat.setId(id1);
			 _cat.setName(_name);
			 _cat.setStatus(_status);
			 _cat.setParentId(_parentId);
			 
			 
			 if(_check == false) {
				 cdao.edit(_cat);
				 view = "Categories?action=index";
			 }else {
				 request.setAttribute("c", _cat); 
				 request.setAttribute("err", "Update failed. Try Again !");
				 view = "admin_view/category/edit.jsp";
			 }
			break;
		case "remove":
			int r_id = Integer.parseInt(request.getParameter("r_id"));

			 boolean bl = cdao.remove(r_id);
			 if(bl) {
			 view = "Categories?action=index";
			 }else {
			 request.setAttribute("err", "Delete failed!");
			 }
			break;
			
		default:
			view = "not-found.jsp";
			break;
		}
		request.getRequestDispatcher(view).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
