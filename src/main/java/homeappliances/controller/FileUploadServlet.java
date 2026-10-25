package homeappliances.controller;
import homeappliances.model.Customer;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.transaction.UserTransaction;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/FileUploadServlet")
@MultipartConfig
public class FileUploadServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "C:\\Users\\ASUS\\OneDrive\\Desktop\\Java Web Application Assignment G15\\JavaWebApplicationAssignment\\src\\main\\webapp\\image\\customerProfile\\";
    //added
    @PersistenceContext(unitName = "JavaAssignmentPU") EntityManager em;
    @Resource UserTransaction utx;
    //end of added
    
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Map<String,String> errors = new HashMap<String,String>();
        // Create directory if it doesn't exist
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        Part filePart = req.getPart("file");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        // Save file
        String filePath = UPLOAD_DIR + fileName;
        try (InputStream fileContent = filePart.getInputStream()) {
            Files.copy(fileContent, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        }

        // Make sure this path matches how you'll access the files from web
        String webAccessiblePath = "image/customerProfile/" + fileName;

        Customer customer = (Customer) req.getSession().getAttribute("customer");
        if (customer == null) {
            errors.put("session", "No customer found in session");
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/customerEditProfile.jsp").forward(req, res);
            return;
        }

        // JPA update
        try {
            utx.begin();
            Customer managedCustomer = em.find(Customer.class, customer.getCustomerId()); // Find by @Id

            if (managedCustomer != null) {
                managedCustomer.setProfilePic(webAccessiblePath);
                utx.commit();

                // 更新 session 中的对象（避免旧值）
                customer.setProfilePic(webAccessiblePath);
            } else {
                errors.put("notFound", "Customer not found in database.");
                req.setAttribute("errors", errors);
                req.getRequestDispatcher("/customerEditProfile.jsp").forward(req, res);
                return;
            }
        } catch (Exception ex) {
            Logger.getLogger(FileUploadServlet.class.getName()).log(Level.SEVERE, null, ex);
            errors.put("dbError", ex.getMessage());
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/customerEditProfile.jsp").forward(req, res);
            return;
        } 
        
        res.sendRedirect("customerEditProfile.jsp?upload=success&filePath=" + URLEncoder.encode(webAccessiblePath, "UTF-8"));
    }
    
}