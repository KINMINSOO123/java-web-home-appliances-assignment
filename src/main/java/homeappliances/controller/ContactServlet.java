package homeappliances.controller;

import homeappliances.model.Contact;
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

@WebServlet("/ContactServlet")
public class ContactServlet extends HttpServlet {
    //added
    @PersistenceContext(unitName = "JavaAssignmentPU") EntityManager em;
    @Resource UserTransaction utx;
    //end of added
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
    
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String message = request.getParameter("message");

        if(username == null || username.isEmpty()||email == null || email.isEmpty() || phone == null || phone.isEmpty() || message == null || message.isEmpty()){
        request.setAttribute("error", "All fields must be filled up.");
        request.getRequestDispatcher("/contactUs.jsp").forward(request, response);
        return;
        }

        Contact contact = new Contact();
        contact.setContactId(java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 20));
        contact.setUsername(username);
        contact.setEmail(email);
        contact.setPhone(phone);
        contact.setMessage(message);

        try{
            utx.begin();
            em.persist(contact);
            utx.commit();
            request.setAttribute("success", "Your message has been sent successfully!");
            request.getRequestDispatcher("/contactUs.jsp").forward(request, response);
        }catch(Exception e){
            e.printStackTrace();
            request.setAttribute("error", "An error occured while saving your message. Please try again later.");
            request.getRequestDispatcher("/contactUs.jsp").forward(request, response);
        }
    }
}

