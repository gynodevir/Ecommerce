package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstans;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class Categorycontroller {
    @Autowired
private CategoryService categoryService;


@RequestMapping(value = "/public/categories",method = RequestMethod.GET)
public ResponseEntity<CategoryResponse> getAllCategories(
        @RequestParam(name = "pageNumber",defaultValue = AppConstans.PAGE_NUMBER,required = false) Integer pageNumber,
        @RequestParam(name = "pageSize",defaultValue = AppConstans.PAGE_SIZE,required = false) Integer pageSize,
        @RequestParam(name = "sortBy",defaultValue = AppConstans.SORT_CATEGORY_BY,required = false) String sortBy,
        @RequestParam(name = "sortOrder",defaultValue = AppConstans.SORT_DIR,required = false) String sortOrder
){
        CategoryResponse categories = categoryService.getAllCategory(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(categories,HttpStatus.OK);
}
//@PostMapping("/public/categories")
@RequestMapping(value = "/public/categories",method = RequestMethod.POST)
public ResponseEntity<CategoryDTO> CreateCatogory(@Valid @RequestBody CategoryDTO categoryDTO){


    CategoryDTO savedCategoryDTO=categoryService.createCategory(categoryDTO);
    return new ResponseEntity<>(savedCategoryDTO,HttpStatus.CREATED);
}
//path variable help to map particular variable to that parameter.
@DeleteMapping("public/categories/{categoryid}")
public ResponseEntity<?> deleteCategory(@PathVariable  Long categoryid){
        try {
            CategoryDTO status = categoryService.deleteCategory(categoryid);
            return new ResponseEntity<>(status, HttpStatus.OK);
        }
        catch (ResponseStatusException e){
            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
        }
}
@PutMapping("admin/categories/{categoryId}")
public ResponseEntity<?> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,@PathVariable Long categoryId){
try{
   CategoryDTO updatecategory = categoryService.updateCategory(categoryDTO,categoryId);
     return new ResponseEntity<>(updatecategory, HttpStatus.OK);
}
catch (ResponseStatusException e){
return new ResponseEntity<>(e.getReason(),e.getStatusCode());
}
}

}
