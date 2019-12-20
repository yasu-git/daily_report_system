package controllers.employees;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.validators.EmployeeValidator;
import utils.DBUtil;
import utils.EncryptUtil;

/**
 * Servlet implementation class EmployeesCreateServlet
 */
@WebServlet("/employees/create")
public class EmployeesCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesCreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String _token = (String)request.getParameter("_token");

        if (_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            Employee e = new Employee();

            e.setCode(request.getParameter("code"));
            e.setName(request.getParameter("name"));
            //送信されたパスワード暗号化しセット
            //
            e.setPassword(
                    EncryptUtil.getPasswordEncrypt(
                            request.getParameter("password"),
                            (String) this.getServletContext().getAttribute("salt")));

            e.setAdmin_flag(Integer.parseInt(request.getParameter("admin_flag")));

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            e.setCreated_at(currentTime);
            e.setUpdated_at(currentTime);
            //新規作成のためデリーとフラグは0
            e.setDelete_flag(0);

            //エラー検証・第2引数は社員code.第3引数はパスワードフラグ
            //検証したいのでtrue
            List<String> errors = EmployeeValidator.validate(e, true, true);

            if (errors.size() > 0) {
                //エラーがあるとき
                em.close();

                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("employee", e);
                request.setAttribute("errors", errors);

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/new.jsp");
                rd.forward(request, response);
            } else {
                //エラーがないとき
                //データーベースに登録処理
                em.getTransaction().begin();
                em.persist(e);
                em.getTransaction().commit();
                em.close();

                request.getSession().setAttribute("flush", "登録が完了しました。");

                response.sendRedirect(request.getContextPath() + "/employees/index");
            }
        }
    }

}
