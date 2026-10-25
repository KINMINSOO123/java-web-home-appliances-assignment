/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homeappliances.util;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
/**
 *
 * @author ASUS
 */
public class CustomerValidator {
    private Map<String, String> errors = new HashMap<>();
    private EntityManager em;
    public CustomerValidator(EntityManager em){
        this.em = em;
    }
    
    public boolean checkNameisValid(String name){
        Pattern pattern = Pattern.compile("^[a-zA-Z\\s]+$");
        Matcher matcher = pattern.matcher(name);
        
        if(name.length() < 50 && matcher.matches()){
            return true;
        } else if (name == null || name.isBlank()) {
            errors.put("name", "Name is required");
            return false;
        } else if (name.length() > 50){
            errors.put("name", "Name cannot exceed 50 characters.");
            return false;
        } else{
            errors.put("name", "Name cannot contain special characters.");
            return false;
        }
    }
    
    public boolean checkEmailisValid(String email, String customerId) {
        if (email == null || email.isBlank()) {
            errors.put("email", "Email is required");
            return false;
        }

        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            errors.put("email", "Invalid email format.");
            return false;
        }

        if (isEmailAlreadyExists(email, customerId)) {
            errors.put("email","Email is already existed");
            return false;
        }

        return true;
    }

    private boolean isEmailAlreadyExists(String email, String customerId) {
        try {
            // Start with the base query
            String query = "SELECT COUNT(c) FROM Customer c WHERE c.email = :email";

            // Add the customerId condition only if it's not null
            if (customerId != null && !customerId.isEmpty()) {
                query += " AND c.customerId <> :customerId";
            }

            // Create the query and set the parameters
            jakarta.persistence.Query q = em.createQuery(query, Long.class);
            q.setParameter("email", email);

            // Set customerId parameter only if it's not null
            if (customerId != null && !customerId.isEmpty()) {
                q.setParameter("customerId", customerId);
            }

            // Execute the query
            Long count = (Long) q.getSingleResult();

            // Return true if email exists
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            errors.put("email", "Error checking email existence: " + e.getMessage());
            return true; // Return true on error to indicate a problem
        }
    }

    
    public boolean checkPhoneisValid(String phone){
        Pattern pattern = Pattern.compile("^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$");
        Matcher matcher = pattern.matcher(phone);
        
        if(matcher.matches()){
            return true;
        } else if (phone == null || phone.isBlank()){
            errors.put("phone", "Phone is required");
            return false;
        } else{
            errors.put("phone", "Phone number must be in international format (e.g., +1234567890)");
            return false;
        }
    }
    
    public boolean checkAddressisValid(String address){
        
        if(address.length() < 300 && !(address.isBlank())){
            return true;
        }else if(address.isBlank()){
            errors.put("address","Address can not be blank.");
            return false;
        }else{
            errors.put("address","Address can not be written more than 300 character");
            return false;
        }
    }
    
    public boolean checksAddressisValid(String sAddress){
        
        if(sAddress.length() < 300 && !(sAddress.isBlank())){
            return true;
        }else if(sAddress.isBlank()){
            errors.put("sAddress","Shipping Address can not be blank.");
            return false;
        }else{
            errors.put("sAddress","Shipping Address can not be written more than 300 character");
            return false;
        }
    }
    
    public boolean checkDOBisValid(Date birthDate) {
        if (birthDate == null) {
            errors.put("dob", "Date of Birth cannot be blank");
            return false;
        }

        LocalDate birthLocalDate = birthDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();
        LocalDate minAgeDate = currentDate.minusYears(13);

        if (birthLocalDate.isAfter(minAgeDate)) {
            errors.put("dob", "You must be at least 13 years old");
            return false;
        }
        return true;
    }
    
    public boolean checkPasswordisValid(String password,String conPassword){
        Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,}$");
        Matcher matcher = pattern.matcher(password);
        
        if (!password.equals(conPassword)) {
            errors.put("conPassword", "Confirm Password does not match.");
            return false;
        }
        
        if(matcher.matches()){
            return true;
        }else{
            errors.put("password","Password must have at least 8 character, an uppercase letter and a special character");
            return false;
        }
    }
    
    public Map<String, String> getErrors() {
        return this.errors;
    }
    
    public void clearErrors() {
        this.errors.clear();
    }
}
