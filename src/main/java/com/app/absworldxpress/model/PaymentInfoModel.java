package com.app.absworldxpress.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PaymentInfoModel {
    @Id
    private String id;

    private String status;

    private String tran_date;

    private String tran_id;

    private String val_id;

    private String amount;

    private String store_amount;

    private String currency;

    private String bank_tran_id;

    private String card_type;

    private String card_no;

    private String card_issuer;

    private String card_brand;

    private String card_issuer_country;

    private String card_issuer_country_code;

    private String currency_type;

    private String currency_amount;

    private String currency_rate;

    private String base_fair;

    private String value_a;

    private String value_b;

    private String value_c;

    private String value_d;

    private String emi_instalment;

    private String emi_amount;

    private String emi_description;

    private String verify_sign;

    private String verify_key;

    private String risk_level;

    private String risk_title;

    private String store_id;
}
