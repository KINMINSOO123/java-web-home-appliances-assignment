package homeappliances.controller;

import homeappliances.model.Customer;
import homeappliances.util.CustomerValidator;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import jakarta.transaction.UserTransaction;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/admin/UpdateCustomerServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
                 maxFileSize = 1024 * 1024 * 10,      // 10MB
                 maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class UpdateCustomerServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "C:\\Users\\ASUS\\OneDrive\\Desktop\\Java Web Application Assignment G15\\JavaWebApplicationAssignment\\src\\main\\webapp\\image\\customerProfile\\";

    @PersistenceContext(unitName = "JavaAssignmentPU")
    EntityManager em;

    @Resource
    UserTransaction utx;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        String userType = (session != null) ? (String) session.getAttribute("userType") : null;

        if (userType == null || !userType.equalsIgnoreCase("manager")) {
            req.setAttribute("error", "You have no permission to update customer.");
            req.getRequestDispatcher("/admin/index.jsp").forward(req, res);
            return;
        }

        // Create directory if it doesn't exist
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Handle file upload
        Part filePart = req.getPart("profilePic");
        String webPath = null;
        String oldProfilePic = req.getParameter("oldProfilePic");
        
        // Check if filePart is not null and has content
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String filePath = UPLOAD_DIR + fileName;

            try (InputStream fileContent = filePart.getInputStream()) {
                Files.copy(fileContent, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            }

            webPath = "image/customerProfile/" + fileName;
        } else {
            webPath = oldProfilePic;
        }
        
        CustomerValidator validator = new CustomerValidator(em);
        String id = req.getParameter("id"); // Customer ID
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String shippingAddress = req.getParameter("shippingAddress");
        String billingAddress = req.getParameter("billingAddress");
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

        //if (!validator.checkNameisValid(name)) isValid = false;
        //if (!validator.checkEmailisValid(email,id)) isValid = false;
        //if (!validator.checkPhoneisValid(phone)) isValid = false;
        //if (!validator.checkAddressisValid(billingAddress)) isValid = false;
        //if (!validator.checksAddressisValid(shippingAddress)) isValid = false;
        //if (!validator.checkDOBisValid(birthDate)) isValid = false;  

        if (!isValid) {
            req.setAttribute("errors", validator.getErrors());
            req.getRequestDispatcher("/admin/editCustomer.jsp").forward(req, res);
            return;
        }
            
        try {
            Customer customer = em.find(Customer.class, id);
            if (customer == null) {
                req.setAttribute("error", "Customer not found.");
                req.getRequestDispatcher("/admin/viewCustomers.jsp").forward(req, res);
                return;
            }

            // Update Customer info
            customer.setCustomerName(name);
            customer.setPassword(password);
            customer.setEmail(email);
            customer.setPhone(phone);
            customer.setShippingAddress(shippingAddress);
            customer.setBillingAddress(billingAddress);
            customer.setDateOfBirth(birthDate);
            customer.setProfilePic(webPath);

            utx.begin();
            em.joinTransaction();
            em.merge(customer);
            utx.commit();

            res.sendRedirect("ViewCustomersServlet");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Error occurred while updating customer.");
            req.getRequestDispatcher("/admin/viewCustomers.jsp").forward(req, res);
        }
    }
}
