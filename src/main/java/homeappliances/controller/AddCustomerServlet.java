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
import jakarta.transaction.UserTransaction;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/admin/AddCustomerServlet")
public class AddCustomerServlet extends HttpServlet {

    @PersistenceContext(unitName = "JavaAssignmentPU")
    private EntityManager em;

    @Resource
    private UserTransaction utx;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        CustomerValidator validator = new CustomerValidator(em);
        validator.clearErrors();

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String shippingAddress = req.getParameter("shippingAddress");
        String billingAddress = req.getParameter("billingAddress");
        String dobStr = req.getParameter("dob");
        String password = req.getParameter("password");

        boolean isValid = true;
        Date birthDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            birthDate = sdf.parse(dobStr);
        } catch (ParseException ex) {
            validator.getErrors().put("dob", "Invalid date format");
            isValid = false;
        }

        if (!validator.checkNameisValid(name)) isValid = false;
        if (!validator.checkEmailisValid(email,null)) isValid = false;
        if (!validator.checkPhoneisValid(phone)) isValid = false;
        if (!validator.checkAddressisValid(shippingAddress)) isValid = false;
        if (!validator.checksAddressisValid(billingAddress)) isValid = false;
        if (!validator.checkPasswordisValid(password, password)) isValid = false;
        if (!validator.checkDOBisValid(birthDate)) isValid = false;

        if (!isValid) {
            req.setAttribute("errors", validator.getErrors());
            req.getRequestDispatcher("/admin/addCustomer.jsp").forward(req, res); // Adjust path if needed
            return;
        }

        try {
            String hashedPassword = PasswordUtil.hashPassword(password);
            Customer customer = new Customer(
                generateCustomerId(), name, hashedPassword, email, phone,
                shippingAddress, billingAddress, birthDate, new Date()
            );

            utx.begin();
            em.persist(customer);
            utx.commit();

            req.setAttribute("success", "Customer added successfully!");
            res.sendRedirect("ViewCustomersServlet"); // Redirect to customer list or summary page

        } catch (Exception e) {
            e.printStackTrace();
            validator.getErrors().put("database", "Error: " + e.getMessage());
            req.setAttribute("errors", validator.getErrors());
            req.getRequestDispatcher("/admin/addCustomer.jsp").forward(req, res);
        }
    }

    private String generateCustomerId() {
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
