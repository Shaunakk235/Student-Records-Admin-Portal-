package shaunak.proj.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	private StudentDbUtil studentDbUtil;
	
	@Resource(name="jdbc/web_student_tracker")
	
	private DataSource dataSource;
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		try {
			studentDbUtil=new StudentDbUtil(dataSource);
		}
		catch(Exception e)
		{
			throw new ServletException();
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    try {
	        String theCommand = request.getParameter("command");

	        if (theCommand == null) {
	            theCommand = "LIST";
	        }

	        switch (theCommand) {
	            case "LIST":
	                listStudent(request, response);
	                break;
	                
	            case "ADD":
	                addStudent(request, response);
	                break;
	                
	            case "LOAD":
	            	loadStudent(request,response);
	            	break;
	            	
	            case "UPDATE":
	            	updateStudent(request,response);
	            	break;
	            	
	            case "DELETE":
	            	deleteStudent(request,response);
	                
	            default:
	                listStudent(request, response);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new ServletException(e);
	    }
	}


	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String theStudentId=request.getParameter("studentId");
		
		studentDbUtil.deletStudent(theStudentId);
		listStudent(request,response);
		
		
		
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response)throws Exception {
		
	  int Id=Integer.parseInt(request.getParameter("studentId"));
	  String firstName=request.getParameter("firstName");
	  String lastName=request.getParameter("lastName");
	  String email=request.getParameter("email");
	  
	  Student theStudent=new Student(Id,firstName,lastName,email);
	  
	  studentDbUtil.updateStudent(theStudent);
	  
	  listStudent(request,response);
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String theStudentId =request.getParameter("studentId");
		
		Student theStudent = studentDbUtil.getStudent(theStudentId);
		
		request.setAttribute("THE_STUDENT", theStudent);
		
		RequestDispatcher dispatcher=
				request.getRequestDispatcher("/update-student-form.jsp");
		        dispatcher.forward(request, response);
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String firstName= request.getParameter("firstName");
		String lastName= request.getParameter("lastName");
		String email= request.getParameter("email");
		
		Student theStudent= new Student(firstName,lastName,email);
		
		
		studentDbUtil.addStudent(theStudent);
		
		listStudent(request,response);
		
	}

	private void listStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		List<Student> students=studentDbUtil.getStudents();
		
		request.setAttribute("STUDENTS_LIST", students);
		
		RequestDispatcher dispatcher=request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
	}
}
