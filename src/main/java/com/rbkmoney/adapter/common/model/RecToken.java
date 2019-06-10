package com.rbkmoney.adapter.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecToken {

    private Map<String, String> recTokenMap;

}
