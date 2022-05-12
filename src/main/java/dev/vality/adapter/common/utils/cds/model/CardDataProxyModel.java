package dev.vality.adapter.common.utils.cds.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
public class CardDataProxyModel {

    @ToString.Exclude
    private String pan;
    private String cardholderName;

    private byte expMonth;
    private short expYear;

}
