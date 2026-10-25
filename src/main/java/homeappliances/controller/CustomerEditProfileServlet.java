package homeappliances.controller;

import homeappliances.model.Customer;
import homeappliances.util.CustomerValidator;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.UserTransaction;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/CustomerEditProfileServlet")
public class CustomerEditProfileServlet extends HttpServlet {
    
    //added
    @PersistenceContext(unitName = "JavaAssignmentPU") EntityManager em;
    @Resource UserTransaction utx;
    //end of added

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        CustomerValidator validator = new CustomerValidator(em);
        Customer customer = (Customer) req.getSession().getAttribute("customer");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String billingAddress = req.getParameter("billingAddress");
        String shippingAddress = req.getParameter("shippingAddress");
        String dob = req.getParameter("dob");
        Date birthDate = null;
        boolean isValid = true;
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            birthDate = sdf.parse(dob);
        } catch (ParseException ex) {
            validator.getErrors().put("dob",ex.getMessage());
            isValid = false;
        }
        
        if (!validator.checkNameisValid(name)) isValid = false;
        if (!validator.checkEmailisValid(email,customer.getCustomerId())) isValid = false;
        if (!validator.checkPhoneisValid(phone)) isValid = false;
        if (!validator.checkAddressisValid(billingAddress)) isValid = false;
        if (!validator.checksAddressisValid(shippingAddress)) isValid = false;
        if (!validator.checkDOBisValid(birthDate)) isValid = false;  
        
        if (!isValid) {
            req.setAttribute("errors", validator.getErrors());
            req.getRequestDispatcher("/customerEditProfile.jsp").forward(req, res);
            return;
        }

        customer.setCustomerName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setBillingAddress(billingAddress);
        customer.setShippingAddress(shippingAddress);
        customer.setDateOfBirth(birthDate);

        try {
            utx.begin();

            // 更新 Customer 实体
            Customer existingCustomer = em.find(Customer.class, customer.getCustomerId());
            if (existingCustomer != null) {
                existingCustomer.setCustomerName(customer.getCustomerName());
                existingCustomer.setEmail(customer.getEmail());
                existingCustomer.setPhone(customer.getPhone());
                existingCustomer.setBillingAddress(customer.getBillingAddress());
                existingCustomer.setShippingAddress(customer.getShippingAddress());
                existingCustomer.setDateOfBirth(customer.getDateOfBirth());

                em.merge(existingCustomer);  // 保存更新
                utx.commit();

                req.getSession().setAttribute("customer", existingCustomer);  // 更新 session 中的 customer
                res.sendRedirect("customerEditProfile.jsp");  // 跳转回编辑页面
            } else {
                validator.getErrors().put("editDatabase", "Customer not found.");
                req.setAttribute("errors", validator.getErrors());
                req.getRequestDispatcher("/customerEditProfile.jsp").forward(req, res);
            }
        } catch (Exception ex) {
            validator.getErrors().put("editError", ex.getMessage());
            req.setAttribute("errors", validator.getErrors());
            req.getRequestDispatcher("/customerEditProfile.jsp").forward(req, res);
        } 
    }
}
