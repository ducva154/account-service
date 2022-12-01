package vn.edu.fpt.account.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import vn.edu.fpt.account.constant.ResponseStatusEnum;
import vn.edu.fpt.account.exception.BusinessException;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static vn.edu.fpt.account.utils.CustomDateTimeFormatter.DATE_FORMATTER;
import static vn.edu.fpt.account.utils.CustomDateTimeFormatter.DATE_TIME_FORMATTER;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Authentication Service
 * @created : 31/08/2022 - 02:27
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeConverter {

    public static LocalDateTime toLocalDateTime(String localDateTime) {
        try {
            return LocalDateTime.parse(localDateTime, DATE_TIME_FORMATTER);
        }catch (Exception ex){
            throw new BusinessException(ResponseStatusEnum.VALIDATION_ERROR, "Can not convert: "+ localDateTime+" to LocalDateTime");
        }
    }

    public static LocalDate toLocaleDate(String localDate){
        try {
            return LocalDate.parse(localDate, DATE_FORMATTER);
        }catch (Exception ex){
            throw new BusinessException(ResponseStatusEnum.VALIDATION_ERROR, "Can not convert: "+localDate+ " to DateTime");
        }
    }
}
