package com.app.absworldxpress.dto.response;

import com.app.absworldxpress.jwt.model.User;
import com.app.absworldxpress.model.TicketModel;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomerListResponse {
    int pageSize;
    int pageNo;
    int CustomerCount;
    boolean isLastPage;
    Long totalCustomer;
    int totalPages;

    List<User> CustomerList;
}
