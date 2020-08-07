package com.jayud.file.exception;

import com.jayud.file.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FileException extends RuntimeException{

    ExceptionEnum exceptionEnum;

}
