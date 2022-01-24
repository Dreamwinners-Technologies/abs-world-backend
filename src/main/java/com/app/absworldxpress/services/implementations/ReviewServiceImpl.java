package com.app.absworldxpress.services.implementations;

import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.dto.BasicTableInfo;
import com.app.absworldxpress.dto.request.AddReviewRequest;
import com.app.absworldxpress.jwt.model.User;
import com.app.absworldxpress.jwt.repository.UserRepository;
import com.app.absworldxpress.model.ProductModel;
import com.app.absworldxpress.model.ReviewModel;
import com.app.absworldxpress.repository.ProductRepository;
import com.app.absworldxpress.repository.ReviewRepository;
import com.app.absworldxpress.services.ReviewService;
import com.app.absworldxpress.util.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    UtilService utilService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;

    @Override
    public ResponseEntity<ApiResponse<ReviewModel>> addReview(String token, String productId, AddReviewRequest addReviewRequest) {

        BasicTableInfo basicTableInfo = utilService.generateBasicTableInfo("review", token);

        Optional<User> userOptional = userRepository.findByUsername(basicTableInfo.getCreateBy());

        Optional<ProductModel> productModelOptional = productRepository.findById(productId);

        if (userOptional.isPresent()){
            ReviewModel reviewModel = ReviewModel.builder()
                    .reviewId(basicTableInfo.getId())
                    .reviewerName(userOptional.get().getFullName())
                    .rating(Double.valueOf(new DecimalFormat("#.#").format(addReviewRequest.getRating())))
                    .comment(addReviewRequest.getComment())
                    .createdBy(basicTableInfo.getCreateBy())
                    .creationTime(basicTableInfo.getCreationTime())
                    .build();

            ProductModel productModel = productModelOptional.get();

            List<ReviewModel> reviewModelList = productModel.getReviewModelList();
            reviewModelList.add(reviewModel);

            productModel.setReviewModelList(reviewModelList);

            if (productModel.getProductRating()==0.0 || productModel.getProductRating()==null){
                productModel.setProductRating(Double.valueOf(new DecimalFormat("#.#").format(addReviewRequest.getRating())));
            }
            else {
                productModel.setProductRating(Double.valueOf(new DecimalFormat("#.#").format((productModel.getProductRating()+addReviewRequest.getRating())/2)));
            }

            productRepository.save(productModel);

            return new ResponseEntity<>(new ApiResponse<>(201,"Review Added",reviewModel), HttpStatus.CREATED);
        }
        else
            return new ResponseEntity<>(new ApiResponse<>(404,"User Not Found",null), HttpStatus.NOT_FOUND);
    }
}
