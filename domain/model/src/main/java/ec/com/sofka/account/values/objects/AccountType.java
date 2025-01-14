package ec.com.sofka.account.values.objects;

import ec.com.sofka.generics.interfaces.IValueObject;

public class AccountType implements IValueObject<String> {
    private  String value;

    public AccountType() {
    }

    private AccountType(String value) {
        this.value = validate(value);
    }

    public static AccountType of(String value) {
        return new AccountType(value);
    }

    private String validate(final String value){
        if(value == null){
            throw new IllegalArgumentException("The personal data can't be null");
        }
        if(value.isBlank()){
            throw new IllegalArgumentException("The personal data can't be empty");
        }
        return value;
    }


    @Override
    public String getValue() {
        return value;
    }
}
