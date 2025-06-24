package com.ecommerce.project.service;

import com.ecommerce.project.Repositories.CartRepository;
import com.ecommerce.project.Repositories.CategoryRepository;
import com.ecommerce.project.Repositories.ProductRepositor;
import com.ecommerce.project.exceptions.ApiException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;
    @Autowired
    private ProductRepositor productRepositor;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FileServiceImp fileServiceImp;

    @Value("${project.image}")
    private String path;
    @Value("${image.base.url}")
    private String imageBaseUrl;




    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));
        boolean isProductNotPresent=true;
        List<Product> products= category.getProducts();
        for (Product value : products) {
            if (value.getProductName().equals(productDTO.getProductName())) {
                isProductNotPresent = false;
                break;
            }
        }

        if(isProductNotPresent) {
            Product product = modelMapper.map(productDTO, Product.class);
            product.setImage("default.png");
            product.setCategory(category);
            double specialPrice = product.getPrice() -
                    ((product.getDiscount() * 0.01) * product.getPrice());
            product.setSpecialPrice(specialPrice);

            Product savedProduct = productRepositor.save(product);


            return modelMapper.map(savedProduct, ProductDTO.class);
        }
        else{
            throw new ApiException("Product Already exist!!!");
        }
    }
    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sprtOrder) {
        Sort sortByAndOrder=sprtOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDteails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> pageProducts=productRepositor.findAll(pageDteails);

        List<Product> products=pageProducts.getContent();
        List<ProductDTO> productDTOS=products.stream()
                .map(product -> {
                   ProductDTO productDTO= modelMapper.map(product,ProductDTO.class);
                   productDTO.setImage(constructImageurl(product.getImage()));
                   return productDTO;
                })

                .toList();
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
        return productResponse;
    }
    public String constructImageurl(String imageName){
        return imageBaseUrl.endsWith("/") ? imageBaseUrl+imageName :imageBaseUrl+ "/"+imageName;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sprtOrder) {

        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));
        Sort sortByAndOrder=sprtOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDteails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> pageProducts=productRepositor.findByCategoryOrderByPriceAsc(category,pageDteails);

        List<Product> products=pageProducts.getContent();

        if(products.size()==0)
        {
            throw new ApiException(category.getCategoryName()+"category does not have any products");
        }
        List<ProductDTO> productDTOS=products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sprtOrder) {

        Sort sortByAndOrder=sprtOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDteails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> pageProducts=productRepositor.findByProductNameIgnoreCaseContaining(keyword,pageDteails);


        List<Product> products=pageProducts.getContent();
        List<ProductDTO> productDTOS=products.stream()
                .map(product -> modelMapper
                        .map(product,ProductDTO.class))
                .toList();
        if(products.size()==0)
        {
            throw new ApiException("Product not found with keyword : " + keyword);
        }
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);

        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product productfromDB=productRepositor.findById(productId)
                                  .orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));
        Product product=modelMapper.map(productDTO,Product.class);
        productfromDB.setProductName(product.getProductName());
        productfromDB.setDescription(product.getDescription());
        productfromDB.setQuantity(product.getQuantity());
        productfromDB.setDiscount(product.getDiscount());
        productfromDB.setPrice(product.getPrice());
        productfromDB.setSpecialPrice(product.getSpecialPrice());
        Product savedProduct=productRepositor.save(productfromDB);

         List<Cart> carts=cartRepository.findCartsByProductId(productId);
         List<CartDTO> cartDTOS=carts.stream().map(cart -> {
             CartDTO cartDTO=modelMapper.map(cart,CartDTO.class);
             List<ProductDTO> products=cart.getCartitems().stream()
                     .map(p->modelMapper.map(p.getProduct(),ProductDTO.class))
                     .collect(Collectors.toList());
             cartDTO.setProducts(products);
             return cartDTO;
         }).toList();
         cartDTOS.forEach(cart->cartService.updateProductInCarts(cart.getCartId(),productId));

        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product productfromDB=productRepositor.findById(productId)
                              .orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));
        List<Cart> carts=cartRepository.findCartsByProductId(productId);
        carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartid(),productId));
        productRepositor.delete(productfromDB);

        return modelMapper.map(productfromDB,ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        //get the product from db
        Product productfromDB=productRepositor.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));

        //upload image to server
        //Get the file name of the uploaded image

        String filename= fileServiceImp.uploadImage(path,image);
        //updating new file name to the product
        productfromDB.setImage(filename);
        //save updated product
        Product updatedProduct=productRepositor.save(productfromDB);

        //return Dto after mappinng product to DTO
        return modelMapper.map(updatedProduct,ProductDTO.class);
    }


}
