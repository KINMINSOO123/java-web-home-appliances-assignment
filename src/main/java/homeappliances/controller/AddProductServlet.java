package homeappliances.controller;

import homeappliances.model.Product;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.transaction.UserTransaction;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.Date;

@WebServlet("/admin/AddProductServlet")
@MultipartConfig
public class AddProductServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "C:\\Users\\ASUS\\OneDrive\\Desktop\\Java Web Application Assignment G15\\JavaWebApplicationAssignment\\src\\main\\webapp\\image\\product\\";

    @PersistenceContext(unitName = "JavaAssignmentPU")
    EntityManager em;

    @Resource
    UserTransaction utx;

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Create directory if it doesn't exist
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Handle file upload
        Part filePart = req.getPart("productPicture");
        String webPath = null;

        // Check if filePart is not null and has content
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String filePath = UPLOAD_DIR + fileName;

            try (InputStream fileContent = filePart.getInputStream()) {
                Files.copy(fileContent, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            }

            webPath = "image/product/" + fileName;
        } else {
            // If no file is uploaded, set a default or throw an error
            req.setAttribute("error", "Product image is required.");
            req.getRequestDispatcher("/admin/addProduct.jsp").forward(req, res);
            return;
        }

        // Get form fields
        String name = req.getParameter("productName");
        String description = req.getParameter("description");
        double price = Double.parseDouble(req.getParameter("price"));
        int stock = Integer.parseInt(req.getParameter("stock"));
        int rating = Integer.parseInt(req.getParameter("rating"));
        String category = req.getParameter("category");

        try {
            utx.begin();

            Product product = new Product();
            product.setProductId(generateProductId()); // You can change this to your own logic
            product.setProductName(name);
            product.setDescription(description);
            product.setPrice(BigDecimal.valueOf(price));
            product.setStock(stock);
            product.setRating(rating);
            product.setCategory(category);
            product.setProductpic(webPath);
            product.setLastUpdated(new Date());

            em.persist(product);
            utx.commit();

            // Successfully added product, redirect to the product view page
            res.sendRedirect("ViewProductsServlet?success=true");
        } catch (Exception ex) {
            ex.printStackTrace();
            req.setAttribute("error", "Failed to add product: " + ex.getMessage());
            req.getRequestDispatcher("/admin/addProduct.jsp").forward(req, res);
        }
    }

    public String generateProductId() {
        String newId;
        do {
            newId = "P" + (int)(Math.random() * 9999);
        } while (productExists(newId));
        return newId;
    }

    private boolean productExists(String productId) {
        return em.find(Product.class, productId) != null;
    }
}
