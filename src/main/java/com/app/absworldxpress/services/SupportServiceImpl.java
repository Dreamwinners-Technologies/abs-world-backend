package com.app.absworldxpress.services;

import com.app.absworldxpress.dto.ApiMessageResponse;
import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.dto.BasicTableInfo;
import com.app.absworldxpress.dto.request.CreateTicketRequest;
import com.app.absworldxpress.dto.request.MessageRequest;
import com.app.absworldxpress.dto.response.TicketListResponse;
import com.app.absworldxpress.dto.response.TicketResponse;
import com.app.absworldxpress.jwt.security.jwt.JwtProvider;
import com.app.absworldxpress.jwt.services.AuthService;
import com.app.absworldxpress.model.MessageModel;
import com.app.absworldxpress.model.TicketModel;
import com.app.absworldxpress.repository.MessageRepository;
import com.app.absworldxpress.repository.TicketRepository;
import com.app.absworldxpress.util.UtilService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class SupportServiceImpl implements SupportService{

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    AuthService authService;
    @Autowired
    UtilService utilService;
    @Autowired
    JwtProvider jwtProvider;

    @Override
    public ResponseEntity<ApiResponse<TicketResponse>> createTicket(String token, CreateTicketRequest createTicketRequest) {
        if (authService.isThisUser("USER", token)){
            BasicTableInfo basicTableInfo = utilService.generateBasicTableInfo(createTicketRequest.getTicketTitle(),token);

            List<MessageModel> messageModelList = new ArrayList<>();
            MessageModel messageModel = MessageModel.builder()
                    .massageId(UUID.randomUUID().toString()).
                    massage(createTicketRequest.getYourProblem())
                    .createdBy(basicTableInfo.getCreateBy())
                    .creationTime(basicTableInfo.getCreationTime())
                    .build();

            messageModelList.add(messageModel);

            TicketModel ticketModel = TicketModel.builder()
                    .ticketId(basicTableInfo.getId())
                    .ticketSKU(basicTableInfo.getSKU())
                    .ticketTitle(createTicketRequest.getTicketTitle())
                    .ticketSlug(basicTableInfo.getSlug())
                    .messageModelList(messageModelList)
                    .createdBy(basicTableInfo.getCreateBy())
                    .creationTime(basicTableInfo.getCreationTime())
                    .updatedBy(basicTableInfo.getCreateBy())
                    .updatedTime(basicTableInfo.getCreationTime())
                    .isOpen(true)
                    .build();

            ticketRepository.save(ticketModel);

            TicketResponse ticketResponse = TicketResponse.builder()
                    .ticketId(basicTableInfo.getId())
                    .ticketSKU(basicTableInfo.getSKU())
                    .ticketTitle(createTicketRequest.getTicketTitle())
                    .ticketSlug(basicTableInfo.getSlug())
                    .messageModelList(messageModelList)
                    .createdBy(basicTableInfo.getCreateBy())
                    .creationTime(basicTableInfo.getCreationTime())
                    .updatedBy(basicTableInfo.getCreateBy())
                    .updatedTime(basicTableInfo.getCreationTime())
                    .isOpen(true)
                    .build();

            return new ResponseEntity<>(new ApiResponse<>(201,"Ticket Created",ticketResponse), HttpStatus.CREATED);
        }
        else
            return new ResponseEntity<>(new ApiResponse<>(401,"You have no permission",null), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiMessageResponse> addMessage(String token, String ticketId, MessageRequest messageRequest) {

        Optional<TicketModel> ticketModelOptional = ticketRepository.findById(ticketId);

        if (ticketModelOptional.isPresent()){
            if (authService.isThisUser("ADMIN", token) || ticketModelOptional.get().getCreatedBy().equalsIgnoreCase(jwtProvider.getUserNameFromJwt(token))){
                MessageModel messageModel = MessageModel.builder()
                        .massageId(UUID.randomUUID().toString())
                        .massage(messageRequest.getMessage())
                        .createdBy(jwtProvider.getUserNameFromJwt(token))
                        .creationTime(System.currentTimeMillis())
                        .build();

                TicketModel ticketModel = ticketModelOptional.get();
                List<MessageModel> messageModelList = ticketModel.getMessageModelList();
                messageModelList.add(messageModel);
                ticketModel.setMessageModelList(messageModelList);
                ticketModel.setUpdatedBy(messageModel.getCreatedBy());
                ticketModel.setUpdatedTime(messageModel.getCreationTime());

                ticketRepository.save(ticketModel);

                return new ResponseEntity<>(new ApiMessageResponse(200, "Messege Added"),HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(new ApiMessageResponse(401, "You have no permission"),HttpStatus.BAD_REQUEST);
        }
        else
            return new ResponseEntity<>(new ApiMessageResponse(404, "Ticket not found"),HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ApiMessageResponse> closeTicket(String token, String ticketId) {
        Optional<TicketModel> ticketModelOptional = ticketRepository.findById(ticketId);
        if (ticketModelOptional.isPresent()){
            if (authService.isThisUser("ADMIN", token)){
                TicketModel ticketModel = ticketModelOptional.get();
                ticketModel.setIsOpen(false);
                ticketModel.setUpdatedBy(jwtProvider.getUserNameFromJwt(token));
                ticketModel.setUpdatedTime(System.currentTimeMillis());

                ticketRepository.save(ticketModel);

                return new ResponseEntity<>(new ApiMessageResponse(200, "Ticket Closed"),HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(new ApiMessageResponse(401, "You have no permission"),HttpStatus.BAD_REQUEST);
        }
        else
            return new ResponseEntity<>(new ApiMessageResponse(404, "Ticket not found"),HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ApiResponse<TicketListResponse>> getTickets(String token, String ticketId, String ticketSKU, String createdBy, String sortBy, Sort.Direction orderBy, int pageSize, int pageNo) {

        TicketModel example = TicketModel.builder()
                .ticketId(ticketId)
                .ticketSKU(ticketSKU)
                .createdBy(createdBy)
                .build();

        Pageable pageable;
        Sort sort = Sort.by(orderBy,sortBy);
        pageable = PageRequest.of(pageNo, pageSize,sort);

        Page<TicketModel> ticketModelPage;

        if (authService.isThisUser("ADMIN", token)){
            ticketModelPage = ticketRepository.findAll(Example.of(example),pageable);
        }
        else {
            example.setCreatedBy(jwtProvider.getUserNameFromJwt(token));
            ticketModelPage = ticketRepository.findAll(Example.of(example),pageable);
        }

        TicketListResponse ticketListResponse = new TicketListResponse(ticketModelPage.getSize(), ticketModelPage.getNumber(), ticketModelPage.getTotalPages(),
                ticketModelPage.isLast(), ticketModelPage.getTotalElements(), ticketModelPage.getTotalPages(), ticketModelPage.getContent());

        if (ticketModelPage.isEmpty()){
            return new ResponseEntity<>(new ApiResponse<>(200,"No Ticket Found",null),HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(new ApiResponse<>(200,"Ticket Found",ticketListResponse),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse<TicketResponse>> getTicketDetails(String token, String ticketSKU) {
        Optional<TicketModel> ticketModelOptional = ticketRepository.findByTicketSKU(ticketSKU);
        if (ticketModelOptional.isPresent()){
            if (authService.isThisUser("ADMIN", token) || ticketModelOptional.get().getCreatedBy().equalsIgnoreCase(jwtProvider.getUserNameFromJwt(token))){
                TicketModel ticketModel = ticketModelOptional.get();

                TicketResponse ticketResponse = TicketResponse.builder()
                        .ticketId(ticketModel.getTicketId())
                        .ticketSKU(ticketModel.getTicketSKU())
                        .ticketTitle(ticketModel.getTicketTitle())
                        .ticketSlug(ticketModel.getTicketSlug())
                        .messageModelList(ticketModel.getMessageModelList())
                        .createdBy(ticketModel.getCreatedBy())
                        .creationTime(ticketModel.getCreationTime())
                        .updatedBy(jwtProvider.getUserNameFromJwt(token))
                        .updatedTime(System.currentTimeMillis())
                        .isOpen(ticketModel.getIsOpen())
                        .build();

                return new ResponseEntity<>(new ApiResponse<>(200,"Ticket Found",ticketResponse), HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(new ApiResponse<>(401,"You have no permission",null), HttpStatus.BAD_REQUEST);
        }
        else
            return new ResponseEntity<>(new ApiResponse<>(404,"Ticket Not Found",null), HttpStatus.BAD_REQUEST);
    }
}
