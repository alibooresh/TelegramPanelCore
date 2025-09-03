package com.telegram.core.specification.inquiry;

import com.telegram.core.InquiryStatus;
import com.telegram.core.specification.base.BaseSearchFilter;
import lombok.Data;

import java.util.List;

@Data
public class InquirySearchFilter extends BaseSearchFilter {
    private Long id;
    private List<InquiryStatus> statuses;
}
