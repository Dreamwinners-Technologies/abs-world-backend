package com.app.absworldxpress.services.implementations;

import com.app.absworldxpress.dto.ApiMessageResponse;
import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.dto.BasicTableInfo;
import com.app.absworldxpress.dto.request.OrderProductModelRequest;
import com.app.absworldxpress.dto.request.PlaceOrderRequest;
import com.app.absworldxpress.dto.response.OrderListResponse;
import com.app.absworldxpress.jwt.model.User;
import com.app.absworldxpress.jwt.repository.UserRepository;
import com.app.absworldxpress.jwt.security.jwt.JwtProvider;
import com.app.absworldxpress.jwt.services.AuthService;
import com.app.absworldxpress.model.DeliveryAreaModel;
import com.app.absworldxpress.model.OrderModel;
import com.app.absworldxpress.model.OrderProductModel;
import com.app.absworldxpress.model.ProductModel;
import com.app.absworldxpress.repository.DeliveryAreaRepository;
import com.app.absworldxpress.repository.OrderProductRepository;
import com.app.absworldxpress.repository.OrderRepository;
import com.app.absworldxpress.repository.ProductRepository;
import com.app.absworldxpress.services.OrderService;
import com.app.absworldxpress.util.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private UtilService utilService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderProductRepository orderProductRepository;
    @Autowired
    DeliveryAreaRepository deliveryAreaRepository;

    @Override
    public ResponseEntity<ApiResponse<OrderModel>> placeOrder(String token, PlaceOrderRequest placeOrderRequest) {

        Optional<User> optionalUser = userRepository.findByUsername(jwtProvider.getUserNameFromJwt(token));

        Optional<DeliveryAreaModel> deliveryAreaModelOptional = deliveryAreaRepository.findById(placeOrderRequest.getDeliveryAreaId());
        if (!deliveryAreaModelOptional.isPresent()){
            return new ResponseEntity<>(new ApiResponse<OrderModel>(400,"Delivery Area Not Found",null), HttpStatus.BAD_REQUEST);
        }

        if (optionalUser.isPresent()){

            BasicTableInfo basicTableInfo = utilService.generateBasicTableInfo("Order", token);
            User user = optionalUser.get();
            OrderProductModel orderProductModel;
            System.out.println("before getting order product");
            List<OrderProductModel> orderProductModelList = manageOrderProductList(placeOrderRequest);
            System.out.println("after getting order product");
            OrderModel orderModel = OrderModel.builder()
                    .orderId(basicTableInfo.getId())
                    .orderSlug(basicTableInfo.getSlug())
                    .orderSKU(basicTableInfo.getSKU())
                    .customerId(user.getId())
                    .customerName(user.getFullName())
                    .customerPhoneNumber(placeOrderRequest.getCustomerPhoneNumber())
                    .deliveryAddress(placeOrderRequest.getDeliveryAddress())
                    .deliveryAreaModel(deliveryAreaModelOptional.get())
                    .orderNote(placeOrderRequest.getOrderNote())
                    .paymentMethod(placeOrderRequest.getPaymentMethod())
                    .orderStatus("PENDING")
                    .paymentStatus("NOT PAID")
                    .paidAmount(0)
                    .productModelList(orderProductModelList)
                    .orderAmount(orderAmount)
                    .createdBy(basicTableInfo.getCreateBy())
                    .creationTime(basicTableInfo.getCreationTime())
                    .updatedBy(basicTableInfo.getCreateBy())
                    .updatedTime(basicTableInfo.getCreationTime())
                    .build();
            System.out.println("before save order model");

            if (orderModel.getProductModelList().isEmpty()){
                return new ResponseEntity<>(new ApiResponse<OrderModel>(400,"Order Failed! Stock Unavailable.",null), HttpStatus.BAD_REQUEST);
            }
            orderRepository.save(orderModel);

            return new ResponseEntity<>(new ApiResponse<OrderModel>(201,"Order Created!",orderModel), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ApiResponse<>(401,"Auth Failed!",null),HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ApiResponse<OrderListResponse>> getOrderList(String token, String createdBy, String orderId, String orderSKU, String customerPhoneNumber, String sortBy, Sort.Direction orderBy, int pageSize, int pageNo) {
        OrderModel example = OrderModel.builder()
                .orderId(orderId)
                .orderSKU(orderSKU)
                .createdBy(createdBy)
                .customerPhoneNumber(customerPhoneNumber)
                .build();

        Pageable pageable;
        Sort sort = Sort.by(orderBy,sortBy);
        pageable = PageRequest.of(pageNo, pageSize,sort);

        Page<OrderModel> orderModelPage;

        if (authService.isThisUser("ADMIN", token)){
            orderModelPage = orderRepository.findAll(Example.of(example),pageable);
        }
        else {
            example.setCreatedBy(jwtProvider.getUserNameFromJwt(token));
            orderModelPage = orderRepository.findAll(Example.of(example),pageable);
        }

        OrderListResponse orderListResponse = new OrderListResponse(orderModelPage.getSize(), orderModelPage.getNumber(), orderModelPage.getTotalPages(),
                orderModelPage.isLast(), orderModelPage.getTotalElements(), orderModelPage.getTotalPages(), orderModelPage.getContent());

        if (orderModelPage.isEmpty()){
            return new ResponseEntity<>(new ApiResponse<>(200,"No Order Found",null),HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(new ApiResponse<>(200,"Order Found",orderListResponse),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiMessageResponse> cancelOrder(String token, String orderId) {
        Optional<OrderModel> orderModelOptional = orderRepository.findById(orderId);
        if (orderModelOptional.isPresent()){
            OrderModel orderModel = orderModelOptional.get();
            if (authService.isThisUser("ADMIN",token) || jwtProvider.getUserNameFromJwt(token).equals(orderModel.getCreatedBy())){
                if (orderModel.getPaymentStatus().equals("NOT PAID")){
                    orderModel.setOrderStatus("CANCELED");
                    orderModel.setUpdatedBy(jwtProvider.getUserNameFromJwt(token));

                    orderRepository.save(orderModel);
                    return new ResponseEntity<>(new ApiMessageResponse(200, "Order canceled Successfully!"),HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<>(new ApiMessageResponse(400, "This Order is already Paid!"),HttpStatus.BAD_REQUEST);
                }
            }
            else {
                return new ResponseEntity<>(new ApiMessageResponse(401, "You Have no permission to cancel this order!"),HttpStatus.UNAUTHORIZED);
            }
        }
        else {
            return new ResponseEntity<>(new ApiMessageResponse(404, "Order Not found!"),HttpStatus.NOT_FOUND);
        }
    }


//    for place order - required data & methods

    Integer orderAmount = 0;

    private List<OrderProductModel> manageOrderProductList(PlaceOrderRequest placeOrderRequest) {
        List<OrderProductModel> orderProductModelList = new ArrayList<OrderProductModel>();
        orderAmount = 0;
        for (OrderProductModelRequest productRequest : placeOrderRequest.getProductList()){

            Optional<ProductModel> optionalProductModel = productRepository.findById(productRequest.getProductId());
            if (optionalProductModel.isPresent()){
                ProductModel productModel = optionalProductModel.get();
                Integer stockAvailable = productModel.getStockAvailable();
                if (stockAvailable>=productRequest.getQuantity()){
                        productModel.setStockAvailable(stockAvailable-productRequest.getQuantity());
                        orderAmount+= (productModel.getRegularPrice()-productModel.getCashBack())*productRequest.getQuantity();

                        OrderProductModel orderProductModel = OrderProductModel.builder()
                                .productId(productModel.getProductId())
                                .productName(productModel.getProductName())
                                .productSlug(productModel.getProductSlug())
                                .SKU(productModel.getProductSKU())
                                .productImage(productModel.getProductImage())
                                .productPrice(productModel.getCurrentPrice())
                                .categoryName(productModel.getCategoryModel().getCatName())
                                .quantity(productRequest.getQuantity())
                                .build();

                        System.out.println(orderProductModel.toString());

                        orderProductRepository.saveAndFlush(orderProductModel);

                        System.out.println(orderProductModel.toString());

                        orderProductModelList.add(orderProductModel);
                }
            }
        }
        return orderProductModelList;
    }
}
