package com.personaltrainer.accountdraft;

public class InvalidPaymentDayException extends RuntimeException{

    public InvalidPaymentDayException (Integer day){
        super ("Dia de pagamento indisponível: " + day);
    }
}
