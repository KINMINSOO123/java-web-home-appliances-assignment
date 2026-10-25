/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homeappliances.model;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "CART")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cart.findAll", query = "SELECT c FROM Cart c"),
    @NamedQuery(name = "Cart.findByCartId", query = "SELECT c FROM Cart c WHERE c.cartId = :cartId"),
    @NamedQuery(name = "Cart.findByStatus", query = "SELECT c FROM Cart c WHERE c.status = :status"),
    @NamedQuery(name = "Cart.findByCreatedAt", query = "SELECT c FROM Cart c WHERE c.createdAt = :createdAt")})
public class Cart implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final BigDecimal TAX_RATE = BigDecimal.valueOf(0.06);
    private static final BigDecimal SHIPPING = BigDecimal.valueOf(10.0);
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "CART_ID")
    private String cartId;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID")
    @ManyToOne(optional = false)
    private Customer customer;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "STATUS")
    private String status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
    private Collection<CartItem> cartItemCollection;
    private List<CartItem> items = new ArrayList<>();
    
    public Cart() {
    }

    public Cart(String cartId) {
        this.cartId = cartId;
    }

    public Cart(String cartId, String status, Date createdAt) {
        this.cartId = cartId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlTransient
    public Collection<CartItem> getCartItemCollection() {
        return cartItemCollection;
    }

    public void setCartItemCollection(Collection<CartItem> cartItemCollection) {
        this.cartItemCollection = cartItemCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cartId != null ? cartId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cart)) {
            return false;
        }
        Cart other = (Cart) object;
        if ((this.cartId == null && other.cartId != null) || (this.cartId != null && !this.cartId.equals(other.cartId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "homeappliances.model.Cart[ cartId=" + cartId + " ]";
    }
    
    public void addItem(Product product, int quantity) {
        // Check if product already exists in cart
        for (CartItem item : items) {
            if (item.getProduct().getProductId().equals(product.getProductId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        // Add new item if not already in cart
        items.add(new CartItem(product, quantity));
    }
    
    public void removeItem(String productId) {
        items.removeIf(item -> item.getProduct().getProductId().equals(productId));
    }
    
    public void updateQuantity(String productId, int newQuantity) {
        items.stream()
            .filter(item -> item.getProduct().getProductId().equals(productId))
            .findFirst()
            .ifPresent(item -> item.setQuantity(newQuantity));
    }
    
    // Calculation Methods
    public BigDecimal getSubtotal() {
        return items.stream()
            .map(item -> item.getTotalPrice())
            .reduce(BigDecimal.ZERO,BigDecimal::add);
    }
    
    public BigDecimal getTax() {
        return getSubtotal().multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP); // Example 6% tax
    }
    
    public BigDecimal getTotal() {
        BigDecimal shippingFee = getSubtotal().compareTo(BigDecimal.valueOf(1000)) >= 0 ? BigDecimal.ZERO : SHIPPING;
        return getSubtotal().add(getTax()).add(shippingFee).setScale(2, RoundingMode.HALF_UP);
    }
    
    public static BigDecimal getSHIPPING(){
        return SHIPPING;
    }
    
    public List<CartItem> getItems() { 
        return items; 
    }
    
    public void setItems(List<CartItem> items) { 
        this.items = items; 
    }
    
    public int getTotalItems() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }
    
    public void clear() {
        items.clear();
    }
}
