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

@WebServlet("/admin/UpdateProductServlet")
@MultipartConfig
public class UpdateProductServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "C:\\Users\\ASUS\\OneDrive\\Desktop\\Java Web Application Assignment G15\\JavaWebApplicationAssignment\\src\\main\\webapp\\image\\product\\";

    @PersistenceContext(unitName = "JavaAssignmentPU")
    EntityManager em;

    @Resource
    UserTransaction utx;

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("productName");
        String description = req.getParameter("description");
        double price = Double.parseDouble(req.getParameter("price"));
        int stock = Integer.parseInt(req.getParameter("stock"));
        int rating = Integer.parseInt(req.getParameter("rating"));
        String category = req.getParameter("category");
        
        if (id == null) {
            res.sendRedirect("viewProducts.jsp?error=MissingProductId");
            return;
        }

        Product product = em.find(Product.class, id);
        if (product == null) {
            res.sendRedirect("viewProducts.jsp?error=ProductNotFound");
            return;
        }
        
        // Create directory if it doesn't exist
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Handle file upload
        Part filePart = req.getPart("productPicture");
        String webPath = null;
        String oldProductPicture = req.getParameter("oldProductPicture");
        
        // Check if filePart is not null and has content
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String filePath = UPLOAD_DIR + fileName;

            try (InputStream fileContent = filePart.getInputStream()) {
                Files.copy(fileContent, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            }

            webPath = "image/product/" + fileName;
        } else {
            webPath = oldProductPicture;
        }


        try {
            utx.begin();
            
            product.setProductName(name);
            product.setDescription(description);
            product.setPrice(BigDecimal.valueOf(price));
            product.setStock(stock);
            product.setRating(rating);
            product.setCategory(category);
            product.setProductpic(webPath);
            product.setLastUpdated(new Date());

            em.merge(product);
            utx.commit();

            res.sendRedirect("ViewProductsServlet?success=ProductUpdated");
        } catch (Exception ex) {
            ex.printStackTrace();
            req.setAttribute("error", "Failed to update product: " + ex.getMessage());
            req.getRequestDispatcher("/admin/editProduct.jsp").forward(req, res);
        }
    }
}

