package com.mycom.myapp.exceptions;

public class ExamNotFoundException extends RuntimeException{

    public ExamNotFoundException(){
        super("요청하신 시험지를 찾을 수 없습니다.");
    }

    public ExamNotFoundException(String message){
        super(message);
    }


}
