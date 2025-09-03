package com.telegram.core.specification.base;

import lombok.Data;
import org.springframework.data.domain.Pageable;
@Data
public class BaseSearchFilter {
    Pageable pageable;
}
