package ru.practicum.utility.validation;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.utility.exceptions.EwmInvalidRequestParameterException;

@Slf4j
public class CommonVaidationChecks {

    private CommonVaidationChecks() {
    }

    public static void paginationParamsValidation(int from, int size) {
        if (from < 0 || size < 1) {
            throw new EwmInvalidRequestParameterException("Invalid pagination params");
        }
    }
}
