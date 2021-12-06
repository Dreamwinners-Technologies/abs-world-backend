package com.app.absworldxpress.dto.response;

import com.app.absworldxpress.model.OrderModel;
import com.app.absworldxpress.model.TicketModel;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderListResponse {
    int pageSize;
    int pageNo;
    int orderCount;
    boolean isLastPage;
    Long totalOrder;
    int totalPages;

    List<OrderModel> OrderList;
}
