package com.ecommerce.project.service;

import com.ecommerce.project.Repositories.CategoryRepository;
import com.ecommerce.project.Repositories.ProductRepositor;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImp implements ProductService{

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




    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));

        Product product=modelMapper.map(productDTO,Product.class);
        product.setImage("default.png");
        product.setCategory(category);
        double specialPrice= product.getPrice()-
                ((product.getDiscount() * 0.01)*product.getPrice());
        product.setSpecialPrice(specialPrice);

        Product savedProduct=productRepositor.save(product);



        return modelMapper.map(savedProduct,ProductDTO.class);
    }
    @Override
    public ProductResponse getAllProducts() {
        List<Product> products=productRepositor.findAll();
        List<ProductDTO> productDTOS=products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);


        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId) {
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));
        List<Product> products=productRepositor.findByCategoryOrderByPriceAsc(category);
        List<ProductDTO> productDTOS=products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);

        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword) {

        List<Product> products=productRepositor.findByProductNameIgnoreCaseContaining(keyword);
        List<ProductDTO> productDTOS=products.stream()
                .map(product -> modelMapper
                        .map(product,ProductDTO.class))
                .toList();
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);

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



        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product productfromDB=productRepositor.findById(productId)
                              .orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));
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
