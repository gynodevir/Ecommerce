package com.ecommerce.project.service;

import com.ecommerce.project.Repositories.CartItemRepository;
import com.ecommerce.project.Repositories.CartRepository;
import com.ecommerce.project.Repositories.ProductRepositor;
import com.ecommerce.project.exceptions.ApiException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.Cartitem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImp implements CartService{
    @Autowired
    CartRepository cartRepository;
    @Autowired
    AuthUtil authUtil;

    @Autowired
    ProductRepositor productRepositor;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        //Find existing cart or create one

         Cart cart=createCart();
        //Retrieve the Product Details
        Product product=productRepositor.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product","ProductId",productId));
        //perform validationns

        Cartitem cartitem=cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartid(), productId);

        if(cartitem!=null){
            throw new ApiException("Product"+product.getProductName()+"already exist in the cart!!");
        }

        if(product.getQuantity()==0){
            throw new ApiException(product.getProductName()+"is out of the stock");

        }
        if(product.getQuantity()<0){
            throw new ApiException("please make an order of the "+product.getProductName()+"less than or equal to quantity"+product.getQuantity()+".");


        }

        //create cart item

        Cartitem newcartItem=new Cartitem();
        newcartItem.setProduct(product);
        newcartItem.setCart(cart);
        newcartItem.setQuantity(quantity);
        newcartItem.setDiscount(product.getDiscount());
        newcartItem.setProductPrice(product.getSpecialPrice());
        //save cart item
        cartItemRepository.save(newcartItem);
        product.setQuantity(product.getQuantity()-quantity);
        cart.setTotalPrice(cart.getTotalPrice()+(product.getSpecialPrice())*quantity);
        cartRepository.save(cart);
        //return updated cart irem
        CartDTO cartDTO=modelMapper.map(cart,CartDTO.class);
        List<Cartitem> cartitems=cart.getCartitems();
        Stream<ProductDTO> productDTOStream=cartitems.stream().map(item->{
            ProductDTO map=modelMapper.map(item.getProduct(),ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });
        cartDTO.setProducts(productDTOStream.toList());
        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts=cartRepository.findAll();
        if(carts.isEmpty()){
            throw new ApiException("No Cart exist!!!");
        }
        List<CartDTO> cartDTOS=carts.stream()
                .map(cart -> {
                    CartDTO cartDTO=modelMapper.map(cart,CartDTO.class);
                    List<ProductDTO> products=cart.getCartitems().stream()
                            .map(p->modelMapper.map(p.getProduct(),ProductDTO.class))
                            .toList();
                    cartDTO.setProducts(products);
                    return cartDTO;
                }).collect(Collectors.toList());
        return cartDTOS;
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart=cartRepository.findCardByEmailAndCartId(emailId,cartId);
        if(cart==null){
            throw new ResourceNotFoundException("Cart","cartId",cartId);
        }
        CartDTO cartDTO=modelMapper.map(cart,CartDTO.class);
        cart.getCartitems().forEach(c->c.getProduct().setQuantity(c.getQuantity()));
        List<ProductDTO> products=cart.getCartitems().stream()
                .map(p->modelMapper.map(p.getProduct(),ProductDTO.class))
                .toList();
        cartDTO.setProducts(products);
        return cartDTO;

    }

    private Cart createCart(){
        Cart userCart=cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if(userCart!=null){
            return userCart;
        }
        Cart cart=new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart=cartRepository.save(cart);
        return newCart;

    }
}
