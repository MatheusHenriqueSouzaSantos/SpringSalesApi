package com.example.projetoApiVendasEmSpring.services;

import java.security.SecureRandom;
import java.util.UUID;

public class Utils {

    private static final SecureRandom orderCodeRadom=new SecureRandom();

    public static UUID GenerateRadomUUID(){
        return UUID.randomUUID();
    }

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
