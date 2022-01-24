package com.app.absworldxpress.services.implementations;

import com.app.absworldxpress.dto.ApiMessageResponse;
import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.dto.BasicTableInfo;
import com.app.absworldxpress.dto.request.CategoryRequest;
import com.app.absworldxpress.dto.response.CategoryImageResponse;
import com.app.absworldxpress.dto.response.CategoryResponse;
import com.app.absworldxpress.jwt.services.AuthService;
import com.app.absworldxpress.model.CategoryModel;
import com.app.absworldxpress.repository.CategoryRepository;
import com.app.absworldxpress.services.CategoryService;
import com.app.absworldxpress.util.ImageUtilService;
import com.app.absworldxpress.util.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private UtilService utilService;

    @Autowired
    ImageUtilService imageUtilService;

    @Override
    public ResponseEntity<ApiResponse<CategoryResponse>> addCategory(String token, CategoryRequest categoryRequest) {

        if (authService.isThisUser("ADMIN",token)){
            BasicTableInfo basicTableInfo = utilService.generateBasicTableInfo(categoryRequest.getCatName(), token);

            CategoryModel category = new CategoryModel();

            category.setCatName(categoryRequest.getCatName());
            category.setCatId(basicTableInfo.getId());
            category.setCatSlug(basicTableInfo.getSlug());
            category.setCreatedBy(basicTableInfo.getCreateBy());
            category.setCreationTime(basicTableInfo.getCreationTime());

            categoryRepository.save(category);
            return new ResponseEntity<>(new ApiResponse(201,"Category Created Successfully",category),HttpStatus.CREATED);
        }
        else
            return new ResponseEntity<>(new ApiResponse(401,"You have no permission",null),HttpStatus.BAD_REQUEST);

    }

    @Override
    public ResponseEntity<ApiResponse<List<CategoryModel>>> getCategory() {
        List<CategoryModel> allCategories = categoryRepository.findAll();
        if (!allCategories.isEmpty()){
            return new ResponseEntity<>(new ApiResponse<>(200,"Category Found",allCategories),HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ApiResponse<>(200,"Category Not Found",allCategories),HttpStatus.OK);
        }
    }


    @Override
    public ResponseEntity<ApiMessageResponse> deleteCategory(String token, String categoryId) {
        if (authService.isThisUser("ADMIN", token)){
            if (categoryRepository.existsByCatId(categoryId)){

                categoryRepository.deleteById(categoryId);

                return new ResponseEntity<>(new ApiMessageResponse(200,"Category Deleted Successfully"),HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(new ApiMessageResponse(404,"Category Not Found"),HttpStatus.BAD_REQUEST);
        }
        else
            return new ResponseEntity<>(new ApiMessageResponse(401,"You have no permission"),HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ApiMessageResponse> editCategory(String token, String categoryId, CategoryRequest categoryRequest) {
        if (authService.isThisUser("ADMIN", token)){
            if (categoryRepository.existsByCatId(categoryId)){
                BasicTableInfo basicTableInfo = utilService.generateBasicTableInfo(categoryRequest.getCatName(),token);
                CategoryModel categoryModel = categoryRepository.getById(categoryId);

                categoryModel.setCatName(categoryRequest.getCatName());
                categoryModel.setCatSlug(basicTableInfo.getSlug());
                categoryModel.setUpdatedBy(basicTableInfo.getCreateBy());
                categoryModel.setUpdatedTime(basicTableInfo.getCreationTime());

                categoryRepository.save(categoryModel);
                return new ResponseEntity<>(new ApiMessageResponse(200,"Category Updated Successfully"),HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(new ApiMessageResponse(404,"Category Not Found"),HttpStatus.BAD_REQUEST);
        }
        else
           return new ResponseEntity<>(new ApiMessageResponse(401,"You have no permission"),HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ApiResponse<CategoryImageResponse>> uploadCategoryImage(String token,MultipartFile aFile, String categoryId) {

        if (authService.isThisUser("ADMIN", token)){
            if (categoryRepository.existsByCatId(categoryId)){
                CategoryModel categoryModel = categoryRepository.getById(categoryId);

                MultipartFile[] multipartFiles = new MultipartFile[1];
                multipartFiles[0] = aFile;
                List<String> categoryImageLinks = new ArrayList<>();

                try {
                    categoryImageLinks = ImageUtilService.uploadImage(multipartFiles);
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
                }

                categoryModel.setCatImage(categoryImageLinks.get(0));
                categoryRepository.save(categoryModel);

                CategoryImageResponse categoryImageResponse = new CategoryImageResponse();
                categoryImageResponse.setCatImage(categoryImageLinks.get(0));
                return new ResponseEntity<>(new ApiResponse<>(200,"CategoryImage Uploaded Successfully",categoryImageResponse),HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new ApiResponse<>(200,"Category Not Found",null),HttpStatus.OK);
            }
        }
        else
            return new ResponseEntity<>(new ApiResponse(401,"You have no permission",null),HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ApiMessageResponse> deleteCategoryImage(String token, String categoryId) {

        if (authService.isThisUser("ADMIN", token)){
            if (categoryRepository.existsByCatId(categoryId)){
                CategoryModel categoryModel = categoryRepository.getById(categoryId);

                categoryModel.setCatImage(null);
                categoryRepository.save(categoryModel);

                return new ResponseEntity<>(new ApiMessageResponse(200,"CategoryImage Deleted Successfully"),HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new ApiMessageResponse(200,"Category Not Found"),HttpStatus.OK);
            }
        }
        else
            return new ResponseEntity<>(new ApiMessageResponse(401,"You have no permission"),HttpStatus.BAD_REQUEST);
    }

}
