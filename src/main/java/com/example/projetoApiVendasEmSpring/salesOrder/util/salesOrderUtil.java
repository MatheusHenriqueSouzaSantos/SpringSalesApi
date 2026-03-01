package com.example.projetoApiVendasEmSpring.salesOrder.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class salesOrderUtil {

    private static final SecureRandom orderCodeRadom=new SecureRandom();

    public static String GenerateOrderCode(){
        String characters="ACDEFGHJKLMNPQRTUVWXY23456789";
        int length=6;
        StringBuilder builderOrderCode=new StringBuilder(length);
        for(int i=0; i<length;i++){
            int randomIndex=orderCodeRadom.nextInt(characters.length());
            char selectedChar=characters.charAt(randomIndex);
            builderOrderCode.append(selectedChar);
        }
        return builderOrderCode.toString();
    }
}
