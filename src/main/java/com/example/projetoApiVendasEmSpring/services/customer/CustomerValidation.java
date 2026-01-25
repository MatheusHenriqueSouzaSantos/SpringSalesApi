package com.example.projetoApiVendasEmSpring.services.customer;

import org.springframework.stereotype.Component;

@Component
public class CustomerValidation {

    public boolean validateCpf(String cpf){
        if (cpf==null){
            return false;
        }
        if(cpf.length()!=11){
            return false;
        }
        if(cpf.matches("(\\d)\\1{10}")){
            return false;
        }
        int sum=0;
        for (int i=0;i<9;i++){
            sum+=Character.getNumericValue(cpf.charAt(i))*(10-i);
        }
        int firstDigit=(sum*10) %11;
        if(firstDigit==10){
            firstDigit=0;
        }
        if(firstDigit !=Character.getNumericValue(cpf.charAt(9))){
            return false;
        }
        sum=0;
        for(int i=0;i<10;i++){
            sum+=Character.getNumericValue(cpf.charAt(i)) *(11-i);
        }
        int secondDigit=(sum*10) % 11;

        if(secondDigit==10){
            secondDigit=0;
        }

        if(secondDigit!=Character.getNumericValue(cpf.charAt(10))){
            return false;
        }

        return true;
    }

    public boolean validateCnpj(String cnpj){
        if (cnpj==null){
            return false;
        }

        if(cnpj.length()!=14){
            return false;
        }
        if(cnpj.matches("(\\d)\\1{13}")){
            return false;
        }
        int[] weight1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] weight2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int sum=0;
        for (int i=0;i<12;i++){
            sum+=Character.getNumericValue(cnpj.charAt(i))* weight1[i];
        }
        int firstDigit=sum%11;
        firstDigit=firstDigit<2?0: 11-firstDigit;
        if(firstDigit!=Character.getNumericValue(cnpj.charAt(12))){
            return false;
        }
        sum=0;
        for (int i=0;i<13;i++){
            sum+=Character.getNumericValue(cnpj.charAt(i))* weight2[i];
        }
        int secondDigit=sum%11;
        secondDigit=secondDigit<2?0: 11-secondDigit;
        if(secondDigit!=Character.getNumericValue(cnpj.charAt(13))){
            return false;
        }

        return true;
    }
}
