package com.ecommerce.project.service;

import com.ecommerce.project.CategoryRepository.CategoryRepository;
import com.ecommerce.project.exceptions.ApiException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import jakarta.validation.constraints.Null;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImp implements CategoryService{
//    private List<Category> categories=new ArrayList<>();
    private long nextId=1L;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    public ModelMapper modelMapper;
    @Override
    public CategoryResponse getAllCategory(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder){
        Sort sortByOrder=sortOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageDetails =  PageRequest.of(pageNumber,pageSize,sortByOrder);
        Page<Category> categoryPage = categoryRepository.findAll( pageDetails);
        List<Category> categories=categoryPage.getContent();
        if(categories.isEmpty()){
            throw new ApiException("Categories is empty.");
        }
        List<CategoryDTO> categoryDTOS=categories.stream()
                .map(category -> {
                    CategoryDTO dto=modelMapper.map(category,CategoryDTO.class);
//                    dto.setProductCount(10);
                    return dto;
                })
                .collect(Collectors.toList());
        CategoryResponse categoryResponse=new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;

    }
    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category=modelMapper.map(categoryDTO,Category.class);
        Category categoryfromdb = categoryRepository.findByCategoryName(category.getCategoryName());
        if (categoryfromdb != null) {
            throw new ApiException("Category with name " + category.getCategoryName() + " already exist!!");
        }
        Category savedcategory=categoryRepository.save(category);
//        CategoryDTO responseDTO = modelMapper.map(savedcategory, CategoryDTO.class);
//        int productCount=10;
//        responseDTO.setProductCount(productCount);
        return modelMapper.map(savedcategory,CategoryDTO.class);

    }

    @Override
    public CategoryDTO deleteCategory(Long categoryid) {
//        boolean ans=false;
//        for (int i = 0; i < categories.size(); i++) {
//            if (categories.get(i).getCategoryid() == categoryid) {
//                categories.remove(i);
//                ans=true;
//                break; // remove only the first match and exit the loop
//            }
//        }
//        if(ans) {
//            return "Deleted Sucessfully";
//        }
//        else{
//            return "incorrect id";
//        }
        Category category=categoryRepository.findById(categoryid)
                .orElseThrow(()->new ResourceNotFoundException("Category","categoryid",categoryid));


//        List<Category> categories=categoryRepository.findAll();
//        Category category=categories.stream()
//                .filter(c->c.getCategoryid().equals(categoryid))
//                .findFirst().
//                orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Resource not found."));


        categoryRepository.delete(category);
        return modelMapper.map(category,CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
            Optional<Category> optionalcategorie=categoryRepository.findById(categoryId);
            Category savedcategory=optionalcategorie
                    .orElseThrow(()->new ResourceNotFoundException("Category","categoryid",categoryId));
            Category category=modelMapper.map(categoryDTO,Category.class);


            category.setCategoryid(categoryId);
            savedcategory=categoryRepository.save(category);
            return modelMapper.map(savedcategory,CategoryDTO.class);

//        Optional<Category> optionalCategory=categories.stream()
//                .filter(c->c.getCategoryid().equals(categoryId))
//                .findFirst();
//        if(optionalCategory.isPresent()){
//            Category existing=optionalCategory.get();
//            existing.setCategoryname(category.getCategoryname());
//            categoryRepository.save(existing);
//            return "Updated";
//
//        }
//        else{
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found.");
//        }

    }
}
