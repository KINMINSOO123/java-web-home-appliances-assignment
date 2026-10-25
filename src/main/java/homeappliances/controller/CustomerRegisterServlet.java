/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homeappliances.controller;

import homeappliances.model.Customer;
import homeappliances.util.CustomerValidator;
import homeappliances.util.PasswordUtil;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.UserTransaction;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * @author ASUS
 */

@WebServlet("/CustomerRegisterServletV2")
public class CustomerRegisterServlet extends HttpServlet{
    
    //added
    @PersistenceContext(unitName = "JavaAssignmentPU") EntityManager em;
    @Resource UserTransaction utx;
    //end of added
    
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        CustomerValidator validator = new CustomerValidator(em);
        validator.clearErrors(); // Reset errors for each request
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String sAddress = req.getParameter("sAddress");
        String dob = req.getParameter("dob");
        String password = req.getParameter("password");
        String conPassword = req.getParameter("conPassword");
        boolean isValid = true;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = null;
        try {
            birthDate = sdf.parse(dob);
        } catch (ParseException ex) {
            validator.getErrors().put("dob",ex.getMessage());
            isValid = false;
        }
        
        if (!validator.checkNameisValid(name)) isValid = false;
        if (!validator.checkEmailisValid(email,null)) isValid = false;
        if (!validator.checkPhoneisValid(phone)) isValid = false;
        if (!validator.checkAddressisValid(address)) isValid = false;
        if (!validator.checksAddressisValid(sAddress)) isValid = false;
        if (!validator.checkPasswordisValid(password,conPassword)) isValid = false;
        if (!validator.checkDOBisValid(birthDate)) isValid = false;  
        
        if (!isValid) {
            // Send errors back to the JSP
            req.setAttribute("errors", validator.getErrors());
            req.getRequestDispatcher("/register.jsp").forward(req, res);
        }
     
        try {           
            String hashedPassword = PasswordUtil.hashPassword(password);
            Customer newCustomer = new Customer(generateCustomerId(),name,hashedPassword,email,phone,sAddress,address,birthDate,new Date());
            
            try {
                utx.begin();
                em.persist(newCustomer);
                utx.commit();
            } catch (Exception ex) {
                validator.getErrors().put("error", ex.getMessage());
                req.setAttribute("errors", validator.getErrors());
                req.getRequestDispatcher("/register.jsp").forward(req, res);
                return;
            }
            
            HttpSession session = req.getSession();
            session.setAttribute("customer", newCustomer);
            res.sendRedirect("index.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            validator.getErrors().put("database", "Registration failed: " + e.getMessage());
            req.setAttribute("errors", validator.getErrors());
            req.getRequestDispatcher("/register.jsp").forward(req, res);
        }
    }
    
    public String generateCustomerId() {
        String newId;
        do {
            newId = "CUS" + (1000 + (int) (Math.random() * 9000));
        } while (customerExists(newId));
        return newId;
    }

    private boolean customerExists(String customerId) {
        return em.find(Customer.class, customerId) != null;
    }
}
