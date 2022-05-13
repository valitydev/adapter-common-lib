package dev.vality.adapter.common.damsel.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public enum PaymentResourceType {

    RECURRENT,
    PAYMENT

}
