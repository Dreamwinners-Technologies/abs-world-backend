package com.app.absworldxpress.controller;

import com.app.absworldxpress.dto.ApiMessageResponse;
import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.dto.request.ProductEditRequest;
import com.app.absworldxpress.dto.request.ProductRequest;
import com.app.absworldxpress.dto.response.CategoryImageResponse;
import com.app.absworldxpress.dto.response.ProductImageResponse;
import com.app.absworldxpress.dto.response.ProductListResponse;
import com.app.absworldxpress.model.ProductModel;
import com.app.absworldxpress.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/product/")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    //all - by cat id - by name
    public ResponseEntity<ApiResponse<ProductListResponse>> getProductList(@RequestParam(required = false) String productName,
                                                                           String categoryId, String productId,
                                                                           @RequestParam(defaultValue = "creationTime") String sortBy,
                                                                           @RequestParam(defaultValue = "ASC") Sort.Direction orderBy,
                                                                           @RequestParam(defaultValue = "20") int pageSize,
                                                                           @RequestParam(defaultValue = "0") int pageNo ){
        return productService.getProductList(productName,categoryId,productId,sortBy,orderBy,pageSize,pageNo);
    }

    @GetMapping("/cpanel/")
    public ResponseEntity<ApiResponse<ProductListResponse>> getProductListForCpanel(@RequestHeader(name = "Authorization") String token,
                                                                                   @RequestParam(required = false) String productName,
                                                                                   String categoryId, String productId,
                                                                                   @RequestParam(defaultValue = "creationTime") String sortBy,
                                                                                   @RequestParam(defaultValue = "ASC") Sort.Direction orderBy,
                                                                                   @RequestParam(defaultValue = "20") int pageSize,
                                                                                   @RequestParam(defaultValue = "0") int pageNo ){
        return productService.getProductListForCpanel(token,productName,categoryId,productId,sortBy,orderBy,pageSize,pageNo);
    }

    @PostMapping("/cpanel/")
    public ResponseEntity<ApiResponse<ProductModel>> addProduct(@RequestHeader(name = "Authorization") String token,
                                                                @RequestBody ProductRequest productRequest){
        return productService.addProduct(token,productRequest);
    }

    @PutMapping("/cpanel/{productId}")
    public ResponseEntity<ApiResponse<ProductModel>> editProduct(@RequestHeader(name = "Authorization") String  token,
                                                                    @RequestBody ProductEditRequest productRequest,
                                                                    @PathVariable String productId){
        return productService.editProduct(token,productRequest,productId);
    }

    @DeleteMapping("/cpanel/{productId}")
    public ResponseEntity<ApiMessageResponse> deleteProduct(@RequestHeader(name = "Authorization") String  token,
                                                          @PathVariable String productId){
        return productService.deleteProduct(token,productId);
    }

    @PostMapping("/cpanel/image/{productId}")
    public ResponseEntity<ApiResponse<ProductImageResponse>> uploadProductImage(@RequestHeader(name = "Authorization") String token,
                                                                                @RequestParam(value = "image", required = true) MultipartFile aFile,
                                                                                @PathVariable String productId) {
        return productService.uploadProductImage(token,aFile,productId);
    }

    @DeleteMapping("/cpanel/image/{productId}")
    public ResponseEntity<ApiMessageResponse> deleteProductImage(@RequestHeader(name = "Authorization") String token,
                                                                  @PathVariable String productId) {
        return productService.deleteProductImage(token,productId);
    }
}
