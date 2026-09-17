package com.app.ecom_application.service;

import com.app.ecom_application.dto.CartItemRequest;
import com.app.ecom_application.model.CartItem;
import com.app.ecom_application.model.Product;
import com.app.ecom_application.model.User;
import com.app.ecom_application.repository.CartItemRepository;
import com.app.ecom_application.repository.ProductRepository;
import com.app.ecom_application.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public CartService(ProductRepository productRepository, CartItemRepository cartItemRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
    }


    public boolean addToCart(String userId, CartItemRequest cartItemRequest) {
      /// Look for product in DB


        //Check if product exists
        Optional<Product> productOpt = productRepository.findById(cartItemRequest.getProductId());
        if(productOpt.isEmpty())
            return  false;

        //Check if product has enough stock
        Product product = productOpt.get();
        if(product.getStockQuantity() < cartItemRequest.getQuantity())
            return false;

        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if(userOpt.isEmpty())
            return false;

        User user = userOpt.get();

        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(user,product);
        if(existingCartItem != null){
            //Update the Quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())) );
            cartItemRepository.save(existingCartItem);
        }else{
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItemRequest.getQuantity())));
            cartItemRepository.save(cartItem);
        }
        return true;

    }


    public boolean deleteItemFromCart(String userId, Long productId) {

        Optional<Product> productOpt = productRepository.findById(productId);
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));

        if(productOpt.isPresent() && userOpt.isPresent()){
            cartItemRepository.deleteByUserAndProduct(userOpt.get(),productOpt.get());
            return true;
        }
        return false;


    }

    public  List<CartItem> getCartItems(String userId) {
      return userRepository.findById(Long.valueOf(userId))
              .map(cartItemRepository:: findByUser)
              .orElse(List.of());
    }

    public void clearCart(String userId) {
        userRepository.findById(Long.valueOf(userId))
                .ifPresent(cartItemRepository::deleteByUser);
    }
}
